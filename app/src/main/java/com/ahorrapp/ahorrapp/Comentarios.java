package com.ahorrapp.ahorrapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import java.util.ArrayList;
import java.util.HashMap;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class Comentarios extends FragmentActivity{

    private EditText Comentario;
    String Coment;
    String Id;
    ArrayList<HashMap<String,String>> productos;
    SessionManager session;
    // Clase JSONParser
    ArrayList<Lista_entrada> datos;
    JSONParser jsonParser = new JSONParser();
    JSONParser jsonParserp = new JSONParser();
    // JSON Node names establecimiento
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_COMENTARIOS = "comentario";
    private static final String TAG_NOMBRE_U = "nombre";
    private static final String TAG_ID = "IdEstablecimiento";
    //JSON Node names producto
    private static final String TAG_COMENTARIO = "Comentario";
    private static final String TAG_USUARIO = "Nombre_usuario";
    private ProgressDialog pDialog;
    JSONArray productsp ;

    class AttemptComentario extends AsyncTask<String, String, String> {
        protected String doInBackground(String... args) {
            // Building Parameters
            session = new SessionManager(getApplicationContext());
            // get user data from session
            HashMap<String, String> user = session.getUserDetails();
            String username = user.get(SessionManager.TAG_NOMBRE);
            String name = user.get(SessionManager.TAG_N_USUARIO);
            String rut = user.get(SessionManager.TAG_RUT);
            List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
            params.add(new BasicNameValuePair(TAG_COMENTARIOS, Coment));
            params.add(new BasicNameValuePair(TAG_NOMBRE_U, username));
            params.add(new BasicNameValuePair("id", Id));
            params.add(new BasicNameValuePair("rut_usuario", rut));
            // getting JSON string from URL
            JSONObject json = jsonParser.makeHttpRequest("http://ahorrapp.hol.es/BD/agregar_comentario.php", "POST", params);
            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    class AttemptCargar extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Comentarios.this);
            pDialog.setMessage("Cargando Comentarios");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected String doInBackground(String... args) {

            Bundle bundle = getIntent().getExtras();
            Id = bundle.getString("id");
            List<BasicNameValuePair> paramsp = new ArrayList<BasicNameValuePair>();
            paramsp.add(new BasicNameValuePair("idEstablecimiento", Id));
            JSONObject jsonp = jsonParserp.makeHttpRequest("http://ahorrapp.hol.es/BD/cargar_comentarios.php", "POST", paramsp);
            try {
                int success = jsonp.getInt(TAG_SUCCESS);
                if (success == 1) {
                    productsp = jsonp.getJSONArray(TAG_COMENTARIO);

                    // Recorriendo todos los comentario
                    for (int j = 0; j < productsp.length(); j++) {
                        JSONObject p = productsp.getJSONObject(j);
                        // Guardando cada comentario
                        String comentario = p.getString(TAG_COMENTARIO);
                        String usuario = p.getString(TAG_USUARIO);
                        // creando un nuevo new HashMap
                        HashMap<String, String> pro = new HashMap<String, String>();
                        // Agregando a un HashMap HashMap key => value
                        pro.put(TAG_COMENTARIO, comentario);
                        pro.put(TAG_USUARIO, usuario);
                        productos.add(pro);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }

        protected void onPostExecute(String result)
        {
           datos.clear();
            runOnUiThread(new Runnable() {
                public void run() {
                    int cont = 0;
                    HashMap<String, String> pro;
                    while (cont < productos.size()) {
                        pro = productos.get(cont);
                        datos.add(new Lista_entrada(pro.get(TAG_USUARIO), pro.get(TAG_COMENTARIO)));
                        cont++;
                    }
                    productos.clear();
                    ListView lista = (ListView) findViewById(R.id.comentarios);
                    lista.setAdapter(new Lista_adaptador(Comentarios.this, R.layout.entrada, datos) {
                        @Override
                        public void onEntrada(Object entrada, View view) {
                            TextView texto_superior_entrada = (TextView) view.findViewById(R.id.textView_superior);
                            texto_superior_entrada.setText(((Lista_entrada) entrada).get_textoEncima());
                            TextView texto_inferior_entrada = (TextView) view.findViewById(R.id.textView_inferior);
                            texto_inferior_entrada.setText(((Lista_entrada) entrada).get_textoDebajo());
                        }


                    });
                }
            });
            pDialog.dismiss();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.comentario);
        productos = new  ArrayList<HashMap<String, String>>();
        datos = new ArrayList<Lista_entrada>();
       new AttemptCargar().execute();
        Comentario = (EditText) findViewById(R.id.txtcoment);
        final Button comentar = (Button) findViewById(R.id.btncoment);  //al presionar comentar
        comentar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mensaje;
                Coment = Comentario.getText().toString();

                Comentario.setText("");
                session = new SessionManager(getApplicationContext());
                if(session.isLoggedIn()) {  //si el usuario inicio sesion
                    if (!Coment.equals("")) { //si el comentario no es vacio
                        new AttemptComentario().execute();
                        new AttemptCargar().execute();
                        hideKeyboard();
                        mensaje="Se ha agregado un comentario";
                        Alertas.mensaje_error(Comentarios.this, mensaje);
                    } else {
                        mensaje="Debe escribir un comentario primero";
                            Alertas.mensaje_error(Comentarios.this,mensaje);
                    }
                }else{
                    mensaje="Para comentar debe iniciar sesion";
                    Alertas.mensaje_error(Comentarios.this, mensaje);
                }


                Coment="";
            }
        });
    }
    private void hideKeyboard() {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Comentarios.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}

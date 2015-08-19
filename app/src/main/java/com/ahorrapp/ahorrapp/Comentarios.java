package com.ahorrapp.ahorrapp;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.ahorrapp.ahorrapp.R.id;
import com.ahorrapp.ahorrapp.R.layout;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Comentarios extends FragmentActivity {

    private EditText Comentario;
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_COMENTARIOS = "comentario";
    private static final String TAG_NOMBRE_U = "nombre";
    private static final String TAG_COMENTARIO = "Comentario";
    private static final String TAG_USUARIO = "Nombre_usuario";
    private ProgressDialog pDialog;
    ArrayList<HashMap<String,String>> productos;
    ArrayList<Lista_entrada> datos;
    SessionManager session;
    JSONParser jsonParser = new JSONParser(); //Parser agregar comentario
    JSONParser jsonParserp = new JSONParser(); //Parser para cargar comentarios
    String Coment;
    String Id;
    JSONArray productsp ;

    class AttemptComentario extends AsyncTask<String, String, String> {

        protected String doInBackground(String... args) {
            // Building Parameters
            Comentarios.this.session = new SessionManager(Comentarios.this.getApplicationContext());
            // get user data from session
            HashMap<String, String> user = Comentarios.this.session.getUserDetails();
            String username = user.get(SessionManager.TAG_NOMBRE);
            String rut = user.get(SessionManager.TAG_RUT);
            List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
            params.add(new BasicNameValuePair(Comentarios.TAG_COMENTARIOS, Comentarios.this.Coment));
            params.add(new BasicNameValuePair(Comentarios.TAG_NOMBRE_U, username));
            params.add(new BasicNameValuePair("id", Comentarios.this.Id));
            params.add(new BasicNameValuePair("rut_usuario", rut));
            // getting JSON string from URL
            JSONObject json = Comentarios.this.jsonParser.makeHttpRequest("http://ahorrapp.hol.es/BD/agregar_comentario.php", "POST", params);
            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(Comentarios.TAG_SUCCESS);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    class AttemptCargar extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();
            Comentarios.this.pDialog = new ProgressDialog(Comentarios.this);
            Comentarios.this.pDialog.setMessage("Cargando Comentarios");
            Comentarios.this.pDialog.setIndeterminate(false);
            Comentarios.this.pDialog.setCancelable(true);
            Comentarios.this.pDialog.show();
        }

        protected String doInBackground(String... args) {
            Bundle bundle = Comentarios.this.getIntent().getExtras();
            Comentarios.this.Id = bundle.getString("id");
            List<BasicNameValuePair> paramsp = new ArrayList<BasicNameValuePair>();
            paramsp.add(new BasicNameValuePair("idEstablecimiento", Comentarios.this.Id));
            JSONObject jsonp = Comentarios.this.jsonParserp.makeHttpRequest("http://ahorrapp.hol.es/BD/cargar_comentarios.php", "POST", paramsp);
            try {
                int success = jsonp.getInt(Comentarios.TAG_SUCCESS);
                if (success == 1) {

                    Comentarios.this.productsp = jsonp.getJSONArray(Comentarios.TAG_COMENTARIO);
                    // Recorriendo todos los comentario

                    for (int j = 0; j < Comentarios.this.productsp.length(); j++) {
                        JSONObject p = Comentarios.this.productsp.getJSONObject(j);
                        // Guardando cada comentario
                        String comentario = p.getString(Comentarios.TAG_COMENTARIO);
                        String usuario = p.getString(Comentarios.TAG_USUARIO);
                        // creando un nuevo new HashMap
                        HashMap<String, String> pro = new HashMap<String, String>();
                        // Agregando a un HashMap HashMap key => value
                        pro.put(Comentarios.TAG_COMENTARIO, comentario);
                        pro.put(Comentarios.TAG_USUARIO, usuario);
                        Comentarios.this.productos.add(pro);
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String result) {
            Comentarios.this.datos.clear();
            Comentarios.this.runOnUiThread(new Runnable() {
                public void run() {
                    int cont = 0;
                    HashMap<String, String> pro;
                    while (cont < Comentarios.this.productos.size()) {
                        pro = Comentarios.this.productos.get(cont);
                        Comentarios.this.datos.add(new Lista_entrada(pro.get(Comentarios.TAG_USUARIO), pro.get(Comentarios.TAG_COMENTARIO)));
                        cont++;
                    }
                    Comentarios.this.productos.clear();
                    ListView lista = (ListView) Comentarios.this.findViewById(id.comentarios);
                    lista.setAdapter(new Lista_adaptador(Comentarios.this, layout.entrada, Comentarios.this.datos) {
                        @Override
                        public void onEntrada(Object entrada, View view) {
                            TextView texto_superior_entrada = (TextView) view.findViewById(id.textView_superior);
                            texto_superior_entrada.setText(((Lista_entrada) entrada).get_textoEncima());
                            TextView texto_inferior_entrada = (TextView) view.findViewById(id.textView_inferior);
                            texto_inferior_entrada.setText(((Lista_entrada) entrada).get_textoDebajo());
                        }
                    });
                }
            });
            Comentarios.this.pDialog.dismiss(); //Cerrar dialog de carga
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(layout.comentario);
        this.productos = new  ArrayList<HashMap<String, String>>();
        this.datos = new ArrayList<Lista_entrada>();
        new AttemptCargar().execute();
        this.Comentario = (EditText) this.findViewById(id.txtcoment);
        Button comentar = (Button) this.findViewById(id.btncoment);  //al presionar comentar
        comentar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Comentarios.this.Coment = Comentarios.this.Comentario.getText().toString();
                Comentarios.this.Comentario.setText("");
                Comentarios.this.session = new SessionManager(Comentarios.this.getApplicationContext());
                if(Comentarios.this.session.isLoggedIn()) {  //si el usuario inicio sesion
                    if (!Comentarios.this.Coment.equals("")) { //si el comentario no es vacio
                        new AttemptComentario().execute();
                        new AttemptCargar().execute();
                        Comentarios.this.hideKeyboard();
                        Alertas.mensaje_error(Comentarios.this, "Se ha agregado un comentario");
                    } else {
                        Alertas.mensaje_error(Comentarios.this,"Debe escribir un comentario primero");
                    }
                }else{
                    Alertas.mensaje_error(Comentarios.this,"Para comentar debe iniciar sesion");
                }
                Comentarios.this.Coment =""; //dejar el Gettext de comentarios vacio
            }
        });
    }
    private void hideKeyboard() {
        // Check if no view has focus:
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) getSystemService(Comentarios.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
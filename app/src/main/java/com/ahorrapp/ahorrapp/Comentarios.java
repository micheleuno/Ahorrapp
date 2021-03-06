package com.ahorrapp.ahorrapp;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.ahorrapp.ahorrapp.R.id;
import com.ahorrapp.ahorrapp.R.layout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Comentarios extends AppCompatActivity {

    private EditText Comentario;
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_COMENTARIOS = "comentario";
    private static final String TAG_NOMBRE_U = "nombre";
    private static final String TAG_COMENTARIO = "Comentario";
    private static final String TAG_USUARIO = "Nombre_usuario";
    private int success;
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


            HashMap<String, String> params = new HashMap<>();
            params.put(Comentarios.TAG_COMENTARIOS, Comentarios.this.Coment);
            params.put(Comentarios.TAG_NOMBRE_U, username);
            params.put("id", Comentarios.this.Id);
            params.put("rut_usuario", rut);
            // getting JSON string from URL
            JSONObject json = Comentarios.this.jsonParser.makeHttpRequest("http://ahorrapp.hol.es/BD/agregar_comentario.php", "POST", params);
            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(Comentarios.TAG_SUCCESS);
                if(success==2)
                    Alertas.mensaje_error(Comentarios.this,"Ha ocurrido un error con la consulta");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    class AttemptCargar extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();
            Alertas.abrir_mensaje_carga(Comentarios.this, "Cargando");
        }

        protected String doInBackground(String... args) {
            Bundle bundle = Comentarios.this.getIntent().getExtras();
            Comentarios.this.Id = bundle.getString("id");


            HashMap<String, String> paramsp = new HashMap<>();
            paramsp.put("idEstablecimiento", Comentarios.this.Id);
            JSONObject jsonp = Comentarios.this.jsonParserp.makeHttpRequest("http://ahorrapp.hol.es/BD/cargar_comentarios.php", "POST", paramsp);
            try {
                 success = jsonp.getInt(Comentarios.TAG_SUCCESS);
                if (success == 1) {
                    Comentarios.this.productsp = jsonp.getJSONArray(Comentarios.TAG_COMENTARIO);
                    // Recorriendo todos los comentario
                    for (int j = 0; j < Comentarios.this.productsp.length(); j++) {
                        JSONObject p = Comentarios.this.productsp.getJSONObject(j);
                        // Guardando cada comentario
                        String comentario = p.getString(Comentarios.TAG_COMENTARIO);
                        String usuario = p.getString(Comentarios.TAG_USUARIO);
                        // creando un nuevo new HashMap
                        HashMap<String, String> pro = new HashMap<>();
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
            if (success == 2) {
                Alertas.mensaje_error(Comentarios.this, "Ha ocurrido un error con la consulta");
            }else {


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

            }
            Alertas.cerrar_mensaje_carga(); //Cerrar dialog de carga
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(layout.comentario);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_local);
        myToolbar.setTitle(Html.fromHtml("<font color='#FFFFFF'>Ahorrapp</font>"));
        myToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_36dp);
        setSupportActionBar(myToolbar);
        Alertas.cambiar_status_bar(Comentarios.this);
        this.productos = new ArrayList<>();
        this.datos = new ArrayList<>();
        new AttemptCargar().execute();
        this.Comentario = (EditText) this.findViewById(id.txtcoment);
        Comentario.getBackground().setColorFilter(getResources().getColor(R.color.primary), PorterDuff.Mode.SRC_ATOP);

        Button comentar = (Button) this.findViewById(id.btncoment);  //al presionar comentar
        comentar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Alertas.Verificar_conexion(Comentarios.this)){
                    Comentarios.this.Coment = Comentarios.this.Comentario.getText().toString();
                    Comentarios.this.Comentario.setText("");
                    Comentarios.this.session = new SessionManager(Comentarios.this.getApplicationContext());
                    if(Comentarios.this.session.isLoggedIn()) {  //si el usuario inicio sesion
                        if (!Comentarios.this.Coment.equals("")) { //si el comentario no es vacio
                            new AttemptComentario().execute();
                            Comentarios.this.hideKeyboard();
                            Alertas.mensaje_error(Comentarios.this, "Se ha agregado un comentario");
                            new AttemptCargar().execute();
                        } else {
                            Alertas.mensaje_error(Comentarios.this,"Debe escribir un comentario primero");
                        }
                    }else{
                        Alertas.mensaje_error(Comentarios.this,"Para comentar debe iniciar sesion");
                    }
                    Comentarios.this.Coment =""; //dejar el Gettext de comentarios vacio
                }

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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: //al apretar ir atras
                Intent nuevoform = new Intent(Comentarios.this, Local.class);
                finish();
                startActivity(nuevoform);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
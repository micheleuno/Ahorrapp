package com.ahorrapp.ahorrapp;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class Menu_a extends AppCompatActivity {

    private static final String LOGIN_URL = "http://ahorrapp.hol.es/BD/login.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_USUARIO = "Usuario";
    private static final String TAG_NOMBRE = "Nombre_usuario";//diego flores
    private static final String TAG_N_USUARIO = "Username";//daigosk
    private static final String TAG_RUT = "Rut_usuario";
    private static final String TAG_ID = "Id_establecimiento";
    private static final String TAG_EMAIL = "Email_usuario";
    private static final String TAG_DIRECCION = "Direccion_usuario";
    private  String username,password;
    private EditText user, pass;
    SessionManager session;
    JSONParser jsonParser = new JSONParser();
    JSONArray products ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_local);
        myToolbar.setTitle(Html.fromHtml("<font color='#FFFFFF'>Iniciar Sesión</font>"));
        myToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_36dp);
        setSupportActionBar(myToolbar);
        Alertas.cambiar_status_bar(Menu_a.this);

        session = new SessionManager(getApplicationContext());
       // session.checkLogin();
        user = (EditText) findViewById(R.id.txtUsuario);
        user.getBackground().setColorFilter(getResources().getColor(R.color.primary), PorterDuff.Mode.SRC_ATOP);
        pass = (EditText) findViewById(R.id.txtPass);
        pass.getBackground().setColorFilter(getResources().getColor(R.color.primary), PorterDuff.Mode.SRC_ATOP);
        final Button iniciar = (Button) findViewById(R.id.btnIniciarSesion);
        iniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Alertas.Verificar_conexion(Menu_a.this)&&!user.getText().toString().equals("")&&!pass.getText().toString().equals("")) {
                    new AttemptLogin().execute();
                }else{
                    Alertas.mensaje_error(Menu_a.this,"Debe ingresar todos los datos");
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent nuevoform = new Intent(Menu_a.this, MapsActivity.class);
        finish();
        startActivity(nuevoform);
    }
    public boolean onOptionsItemSelected(MenuItem item) { //al apretar atras en el toolbar
        switch (item.getItemId()) {
            case android.R.id.home: //al apretar ir atras
                Intent nuevoform = new Intent(Menu_a.this, MapsActivity.class);
                finish();
                startActivity(nuevoform);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    class AttemptLogin extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Alertas.abrir_mensaje_carga(Menu_a.this, "Ininiciando sesion");
            username = user.getText().toString();
            password = pass.getText().toString();
        }

        @Override
        protected String doInBackground(String... args) {
            int success;
            try {                // Building Parameters

                // getting product details by making HTTP request

                HashMap<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);

                JSONObject json = jsonParser.makeHttpRequest(LOGIN_URL, "POST",params);
                // check your log for json response
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    products = json.getJSONArray(TAG_USUARIO);
                    JSONObject c = products.getJSONObject(0);
                    // Storing each json item in variable
                    session.createLoginSession(c.getString(TAG_N_USUARIO), c.getString(TAG_NOMBRE), c.getString(TAG_RUT), c.getString(TAG_ID), c.getString(TAG_EMAIL), c.getString(TAG_DIRECCION));
                    Intent i = new Intent(Menu_a.this, MapsActivity.class);
                    finish();
                    startActivity(i);
                    return json.getString(TAG_MESSAGE);
                } else {
                    Log.d("Datos incorrectos", json.getString(TAG_MESSAGE));
                    return json.getString(TAG_MESSAGE);
                }
            }catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product deleted
            if (file_url != null) {
                Toast.makeText(Menu_a.this, file_url, Toast.LENGTH_LONG).show();
            }
           Alertas.cerrar_mensaje_carga();
        }

    }
}

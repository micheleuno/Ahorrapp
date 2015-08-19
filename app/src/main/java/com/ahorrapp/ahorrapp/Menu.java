package com.ahorrapp.ahorrapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Menu extends Activity {

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
    private ProgressDialog pDialog;
    SessionManager session;
    JSONParser jsonParser = new JSONParser();
    JSONArray products ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        session = new SessionManager(getApplicationContext());
        session.checkLogin();
        user = (EditText) findViewById(R.id.txtUsuario);
        pass = (EditText) findViewById(R.id.txtPass);
        final Button iniciar = (Button) findViewById(R.id.btnIniciarSesion);
        iniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AttemptLogin().execute();
            }
        });

        final Button registrar = (Button) findViewById(R.id.btnRegistrarse);
        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nuevoform = new Intent(Menu.this, Registro.class);
                finish();
                startActivity(nuevoform);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent nuevoform = new Intent(Menu.this, MapsActivity.class);
        finish();
        startActivity(nuevoform);
    }

    class AttemptLogin extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Menu.this);
            pDialog.setMessage("Intentando ingresar");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
            username = user.getText().toString();
            password = pass.getText().toString();
        }

        @Override
        protected String doInBackground(String... args) {
            int success;
            try {
                // Building Parameters
                List params = new ArrayList();
                params.add(new BasicNameValuePair("username", username));
                params.add(new BasicNameValuePair("password", password));
                // getting product details by making HTTP request
                JSONObject json = jsonParser.makeHttpRequest(LOGIN_URL, "POST",params);
                // check your log for json response
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    products = json.getJSONArray(TAG_USUARIO);
                    JSONObject c = products.getJSONObject(0);
                    // Storing each json item in variable
                    session.createLoginSession(c.getString(TAG_N_USUARIO), c.getString(TAG_NOMBRE), c.getString(TAG_RUT),c.getString(TAG_ID),c.getString(TAG_EMAIL),c.getString(TAG_DIRECCION));
                    Intent i = new Intent(Menu.this, Perfil.class);
                    startActivity(i);
                    finish();
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
                Toast.makeText(Menu.this, file_url, Toast.LENGTH_LONG).show();
            }
            pDialog.dismiss();
        }
    }
}

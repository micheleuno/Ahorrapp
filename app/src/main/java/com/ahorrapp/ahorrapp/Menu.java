package com.ahorrapp.ahorrapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
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
import java.util.HashMap;
import java.util.List;


public class Menu extends Activity {

    private EditText user, pass;
    private Button mSubmit, mRegister;

    private ProgressDialog pDialog;
    SessionManager session;
    // Clase JSONParser
    JSONParser jsonParser = new JSONParser();
    JSONArray products ;

    // si trabajan de manera local "localhost" :
    // En windows tienen que ir, run CMD > ipconfig
    // buscar su IP
    // y poner de la siguiente manera
    // "http://xxx.xxx.x.x:1234/cas/login.php";

    private static final String LOGIN_URL = "http://ahorrapp.hol.es/BD/login.php";

    // La respuesta del JSON es
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_USUARIO = "Usuario";
    private static final String TAG_NOMBRE = "Nombre_usuario";//diego flores
    private static final String TAG_N_USUARIO = "Username";//daigosk
    private static final String TAG_RUT = "Rut_usuario";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        session = new SessionManager(getApplicationContext());

        session.checkLogin();

        // setup input fields
        user = (EditText) findViewById(R.id.txtUsuario);
        pass = (EditText) findViewById(R.id.txtPass);

        final Button iniciar = (Button) findViewById(R.id.btnIniciarSesion);
        iniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AttemptLogin().execute();


            }
        });



    }



    class AttemptLogin extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Menu.this);
            pDialog.setMessage("Attempting login...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            int success;
            String username = user.getText().toString();
            String password = pass.getText().toString();
            try {
                // Building Parameters
                List params = new ArrayList();
                params.add(new BasicNameValuePair("username", username));
                params.add(new BasicNameValuePair("password", password));

                Log.d("request!", "starting");
                // getting product details by making HTTP request
                JSONObject json = jsonParser.makeHttpRequest(LOGIN_URL, "POST",params);

                // check your log for json response
                Log.d("Login attempt", json.toString());

                // json success tag
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Log.d("Login Successful!", json.toString());

                        products = json.getJSONArray(TAG_USUARIO);
                        Log.e("Usuario", json.toString());
                        JSONObject c = products.getJSONObject(0);

                        // Storing each json item in variable


                    session.createLoginSession(c.getString(TAG_N_USUARIO), c.getString(TAG_NOMBRE), c.getString(TAG_RUT));

                    // save user data
                   // SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(Menu.this);
                   // SharedPreferences.Editor edit = sp.edit();
                    //edit.putString("username", c.getString(TAG_N_USUARIO));
                    //edit.putString("nombre", c.getString(TAG_NOMBRE));
                    //edit.putString("rut", c.getString(TAG_RUT));
                    //edit.commit();
                    //Log.e("MIchele weco", sp.getString("username",null));

                    Intent i = new Intent(Menu.this, Perfil.class);
                    startActivity(i);
                    finish();
                    return json.getString(TAG_MESSAGE);
                } else {
                    Log.d("Login Failure!", json.getString(TAG_MESSAGE));
                    return json.getString(TAG_MESSAGE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;

        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product deleted
            pDialog.dismiss();
            if (file_url != null) {
                Toast.makeText(Menu.this, file_url, Toast.LENGTH_LONG).show();
            }
        }
    }
}

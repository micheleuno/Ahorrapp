package com.ahorrapp.ahorrapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Registro extends Activity{
    private EditText user, pass,email,nombre;
    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    private static final String REGISTER_URL = "http://ahorrapp.hol.es/BD/agregar_usuario.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private  String username,password,nombre_usuario,email_usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro);
        Typeface typeFace=Typeface.createFromAsset(getAssets(),"font/rockwell condensed.ttf");
        user = (EditText)findViewById(R.id.txtusername);
        user.setTypeface(typeFace);
        pass = (EditText)findViewById(R.id.txtPass);
        pass.setTypeface(typeFace);
        email = (EditText)findViewById(R.id.txtEmail);
        email.setTypeface(typeFace);
        nombre = (EditText)findViewById(R.id.txtNombrePersona);
        nombre.setTypeface(typeFace);
        TextView text1 =(TextView) findViewById(R.id.txtregistro);
        text1.setTypeface(typeFace);
        final Button  mRegister = (Button)findViewById(R.id.btnGuardar);
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CreateUser().execute();
            }
        });
    }
    class CreateUser extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Registro.this);
            pDialog.setMessage("Creando Usuario...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
            username = user.getText().toString();
            password = pass.getText().toString();
            nombre_usuario = nombre.getText().toString();
            email_usuario = email.getText().toString();
        }
        @Override
        protected String doInBackground(String... args) {

            int success;
            try {
                List params = new ArrayList();
                params.add(new BasicNameValuePair("username", username));
                params.add(new BasicNameValuePair("password", password));
                params.add(new BasicNameValuePair("Nombre_usuario", nombre_usuario));
                params.add(new BasicNameValuePair("Email_usuario", email_usuario));
                Log.d("request!", "starting");
                JSONObject json = jsonParser.makeHttpRequest(REGISTER_URL, "POST", params);
                Log.d("Registering attempt", json.toString());
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Log.d("User Created!", json.toString());
                    finish();
                    return json.getString(TAG_MESSAGE);
                }else{
                    Log.d("Registering Failure!", json.getString(TAG_MESSAGE));
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
            if (file_url != null){
                Toast.makeText(Registro.this, file_url, Toast.LENGTH_LONG).show();
            }
        }
    }
}


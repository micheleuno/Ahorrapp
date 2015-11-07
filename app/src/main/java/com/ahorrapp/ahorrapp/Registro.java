package com.ahorrapp.ahorrapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Registro extends Activity{
    private EditText user, pass,email,nombre,pass2,direccion;
    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    private static final String REGISTER_URL = "http://ahorrapp.hol.es/BD/agregar_usuario.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private  String username,password,nombre_usuario,email_usuario,direccion_usuario,password2;
    private int success;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro);
        Typeface typeFace=Typeface.createFromAsset(getAssets(),"font/rockwell condensed.ttf");
        user = (EditText)findViewById(R.id.txtusername); //nombre de la cuenta
        user.setTypeface(typeFace);
        pass = (EditText)findViewById(R.id.txtPass); //primer password
        pass.setTypeface(typeFace);
        pass2 = (EditText)findViewById(R.id.txtPass2); //segundo password
        pass2.setTypeface(typeFace);
        email = (EditText)findViewById(R.id.txtEmail);
        email.setTypeface(typeFace);
        nombre = (EditText)findViewById(R.id.txtNombrePersona); //nombre de la persona
        nombre.setTypeface(typeFace);
        direccion = (EditText)findViewById(R.id.txtDireccionPersona);
        direccion.setTypeface(typeFace);


        TextView text1 =(TextView) findViewById(R.id.txtregistro);
        text1.setTypeface(typeFace);
        final Button  mRegister = (Button)findViewById(R.id.btnGuardar);
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                username = user.getText().toString();
                password = pass.getText().toString();
                nombre_usuario = nombre.getText().toString();
                email_usuario = email.getText().toString();
                password2 = pass2.getText().toString();
                direccion_usuario = direccion.getText().toString();

                if (!username.equals("")&&!password.equals("")&&!nombre_usuario.equals("")&&!email_usuario.equals("")&&!password2.equals("")&&!direccion_usuario.equals("")) {
                    if(password2.equals(password)){
                        if(password2.length()>7) {
                            new CreateUser().execute();
                        }else {
                            Alertas.mensaje_error(Registro.this, "Las contrasenas deben tener al menos 8 caracteres");
                        }
                    }
                    else {
                        Alertas.mensaje_error(Registro.this, "Las contrasenas deben coincidir");
                    }
                }else {
                    Alertas.mensaje_error(Registro.this, "Debe introducir todos los campos");
                }
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
        }
        @Override
        protected String doInBackground(String... args) {
            try {
                HashMap<String, String> params = new HashMap<>();
                params.put("Username", username);
                params.put("Password", password);
                params.put("Nombre_usuario", nombre_usuario);
                params.put("Email_usuario", email_usuario);
                params.put("Direccion", direccion_usuario);

                Log.d("Iniciando!", "Peticion");
                JSONObject json = jsonParser.makeHttpRequest(REGISTER_URL, "POST", params);
                Log.d("Intentando registrar", json.toString());
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Log.d("Usuario creado!", json.toString());
                    return json.getString(TAG_MESSAGE);
                }else{
                    Log.d("Registro Fallido!", json.getString(TAG_MESSAGE));
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
            if(success == 1) {
                Intent nuevoform = new Intent(Registro.this, com.ahorrapp.ahorrapp.Menu.class);
                finish();
                startActivity(nuevoform);
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent nuevoform = new Intent(Registro.this, com.ahorrapp.ahorrapp.Menu.class);
        finish();
        startActivity(nuevoform);
    }

}


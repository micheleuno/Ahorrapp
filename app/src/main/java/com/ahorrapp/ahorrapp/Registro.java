package com.ahorrapp.ahorrapp;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Registro extends AppCompatActivity {
    private EditText user, pass,email,nombre,pass2,direccion;
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
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_local);
        myToolbar.setTitle(Html.fromHtml("<font color='#FFFFFF'>Registrar Usuario</font>"));
        myToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_36dp);
        setSupportActionBar(myToolbar);
        Alertas.cambiar_status_bar(Registro.this);

        user = (EditText)findViewById(R.id.txtusername); //nombre de la cuenta
        user.getBackground().setColorFilter(ContextCompat.getColor(Registro.this, R.color.primary), PorterDuff.Mode.SRC_ATOP);
        pass = (EditText)findViewById(R.id.txtPass); //primer password
        pass.getBackground().setColorFilter(ContextCompat.getColor(Registro.this, R.color.primary), PorterDuff.Mode.SRC_ATOP);
        pass2 = (EditText)findViewById(R.id.txtPass2); //segundo password
        pass2.getBackground().setColorFilter(ContextCompat.getColor(Registro.this, R.color.primary), PorterDuff.Mode.SRC_ATOP);
        email = (EditText)findViewById(R.id.txtEmail);
        email.getBackground().setColorFilter(ContextCompat.getColor(Registro.this, R.color.primary), PorterDuff.Mode.SRC_ATOP);
        nombre = (EditText)findViewById(R.id.txtNombrePersona); //nombre de la persona
        nombre.getBackground().setColorFilter(ContextCompat.getColor(Registro.this, R.color.primary), PorterDuff.Mode.SRC_ATOP);
        direccion = (EditText)findViewById(R.id.txtDireccionPersona);
        direccion.getBackground().setColorFilter(ContextCompat.getColor(Registro.this, R.color.primary), PorterDuff.Mode.SRC_ATOP);

        final Button  mRegister = (Button)findViewById(R.id.btnGuardar);
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Alertas.Verificar_conexion(Registro.this)){
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
            }
        });
    }
    class CreateUser extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Alertas.abrir_mensaje_carga(Registro.this, "Creando Usuario...");
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


           Alertas.cerrar_mensaje_carga();
            if (file_url != null){
                Toast.makeText(Registro.this, file_url, Toast.LENGTH_LONG).show();
            }
            if(success == 1) {
                Intent nuevoform = new Intent(Registro.this, MapsActivity.class);
                finish();
                startActivity(nuevoform);
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent nuevoform = new Intent(Registro.this, MapsActivity.class);
        finish();
        startActivity(nuevoform);
    }
    public boolean onOptionsItemSelected(MenuItem item) { //al apretar atras en el toolbar
        switch (item.getItemId()) {
            case android.R.id.home: //al apretar ir atras
                Intent nuevoform = new Intent(Registro.this, MapsActivity.class);
                finish();
                startActivity(nuevoform);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}


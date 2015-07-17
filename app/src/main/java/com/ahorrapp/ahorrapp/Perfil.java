package com.ahorrapp.ahorrapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;


public class Perfil extends Activity {


    SessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perfil);
        session = new SessionManager(getApplicationContext());



        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        // name
        String name = user.get(SessionManager.TAG_NOMBRE);

        // username
        String email = user.get(SessionManager.TAG_N_USUARIO);
        // Rut
        String rut = user.get(SessionManager.TAG_RUT);

        TextView nombre =(TextView) findViewById(R.id.txtNombrePersona);
        nombre.setText(name);
        TextView Rut =(TextView) findViewById(R.id.txtRut);
        Rut.setText(rut);
        TextView username =(TextView) findViewById(R.id.txtEmail);
        username.setText(email);


        final Button cerrar = (Button) findViewById(R.id.btncerrar);
        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                session.logoutUser();

            }
        });


    }
}

package com.ahorrapp.ahorrapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
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
        String nombre_pila = user.get(SessionManager.TAG_N_USUARIO);
        // Rut
        String rut = user.get(SessionManager.TAG_RUT);
        // Id
        String id = user.get(SessionManager.TAG_LOCAL);
        TextView nombre =(TextView) findViewById(R.id.txtNombrePersona);
        nombre.setText(name);
        TextView Rut =(TextView) findViewById(R.id.txtRut);
        Rut.setText(rut);
        TextView username =(TextView) findViewById(R.id.txtEmail);
        username.setText(nombre_pila);


        final Button cerrar = (Button) findViewById(R.id.btncerrar);
        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                session.logoutUser();

            }
        });

        final Button Negocio = (Button) findViewById(R.id.btnNegocio);
        if(id.equals("0")) {
            Negocio.setVisibility(View.INVISIBLE);
        }
        Negocio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent nuevoform = new Intent(Perfil.this, Negocio.class);
                finish();
                startActivity(nuevoform);

            }
        });





    }


    @Override
    public void onBackPressed() {
        Intent nuevoform = new Intent(Perfil.this, MapsActivity.class);
        finish();
        startActivity(nuevoform);
    }

}

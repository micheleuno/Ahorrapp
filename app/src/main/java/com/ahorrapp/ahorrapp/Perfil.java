package com.ahorrapp.ahorrapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.HashMap;


public class Perfil extends AppCompatActivity {

    SessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perfil);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_local);
        myToolbar.setTitle(Html.fromHtml("<font color='#FFFFFF'>Ahorrapp</font>"));
        myToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_36dp);
        setSupportActionBar(myToolbar);
        Alertas.cambiar_status_bar(Perfil.this);
        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        String name = user.get(SessionManager.TAG_NOMBRE);
        String nombre_pila = user.get(SessionManager.TAG_N_USUARIO);
        String direccion_usuario = user.get(SessionManager.TAG_DIRECCION);
        String email_usuario = user.get(SessionManager.TAG_EMAIL);

        TextView nombre = (TextView) findViewById(R.id.txtNombrePersona);

        nombre.setText(name);

        TextView direccion = (TextView) findViewById(R.id.txtDireccionPersona);
        direccion.setText(direccion_usuario);

        TextView email = (TextView) findViewById(R.id.txtemailpersona);
        email.setText(email_usuario);


        TextView username = (TextView) findViewById(R.id.txtEmail);
        username.setText(nombre_pila);
    }
    @Override
    public void onBackPressed() {
        Intent nuevoform = new Intent(Perfil.this, MapsActivity.class);
        finish();
        startActivity(nuevoform);
    }
    public boolean onOptionsItemSelected(MenuItem item) { //al apretar atras en el toolbar
        switch (item.getItemId()) {
            case android.R.id.home: //al apretar ir atras
                Intent nuevoform = new Intent(Perfil.this, MapsActivity.class);
                finish();
                startActivity(nuevoform);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

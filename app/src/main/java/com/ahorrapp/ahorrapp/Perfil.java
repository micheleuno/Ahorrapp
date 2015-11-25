package com.ahorrapp.ahorrapp;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
        String id = user.get(SessionManager.TAG_LOCAL);
        Typeface typeFace=Typeface.createFromAsset(getAssets(),"font/rockwell condensed.ttf");

        TextView nombre =(TextView) findViewById(R.id.txtNombrePersona);
        nombre.setTypeface(typeFace);
        nombre.setText(name);

        TextView direccion =(TextView) findViewById(R.id.txtDireccionPersona);
        direccion.setTypeface(typeFace);
        direccion.setText(direccion_usuario);

        TextView email =(TextView) findViewById(R.id.txtemailpersona);
        email.setTypeface(typeFace);
        email.setText(email_usuario);


        TextView username =(TextView) findViewById(R.id.txtEmail);
        username.setTypeface(typeFace);
        username.setText(nombre_pila);
        TextView text1 =(TextView) findViewById(R.id.textbienvenido);
        text1.setTypeface(typeFace);
        TextView text2 =(TextView) findViewById(R.id.textdatos);
        text2.setTypeface(typeFace);
        TextView text4 =(TextView) findViewById(R.id.textdireccion);
        text4.setTypeface(typeFace);
        TextView text6 =(TextView) findViewById(R.id.textnombre);
        text6.setTypeface(typeFace);
        TextView text7 =(TextView) findViewById(R.id.textemail);
        text7.setTypeface(typeFace);

        final Button cerrar = (Button) findViewById(R.id.btncerrar);
        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                session.logoutUser();
            }
        });

        final Button Solicitar = (Button) findViewById(R.id.btnsolicitar);
        Solicitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nuevoform = new Intent(Perfil.this, Solicitar.class);
                finish();
                startActivity(nuevoform);
            }
        });

        final Button Negocio = (Button) findViewById(R.id.btnNegocio);
        if(id.equals("0")) {
            Negocio.setVisibility(View.INVISIBLE);
        }
        Negocio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Alertas.Verificar_conexion(Perfil.this)){
                    Intent nuevoform = new Intent(Perfil.this, Negocio.class);
                    finish();
                    startActivity(nuevoform);
                }
            }
        });
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

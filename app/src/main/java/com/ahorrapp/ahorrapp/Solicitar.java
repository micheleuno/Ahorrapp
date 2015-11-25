package com.ahorrapp.ahorrapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * Created by Matías Cornejo R on 20-08-2015.
 */
public class Solicitar extends AppCompatActivity {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.solicitar_layout);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_local);
        myToolbar.setTitle(Html.fromHtml("<font color='#FFFFFF'>Ahorrapp</font>"));
        myToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_36dp);
        setSupportActionBar(myToolbar);
        Alertas.cambiar_status_bar(Solicitar.this);
        TextView Mensaje ;
        Typeface typeFace=Typeface.createFromAsset(getAssets(),"font/rockwell condensed.ttf");
        Mensaje = (TextView) findViewById(R.id.Texto);
        Mensaje.setTypeface(typeFace);
        Mensaje.setText("Para solicitar permiso para administrar su negocio, favor enviar un correo a ahorrap.tds@gmail.com indicando " +
                "sus datos y adjuntando documentos que validen la propiedad del negocio, los cuales serán verificados por el equipo de ahorrap" +
                " gracias");



    }
    public void onBackPressed() {
        Intent nuevoform = new Intent(Solicitar.this, MapsActivity.class);
        finish();
        startActivity(nuevoform);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: //al apretar ir atras
                Intent nuevoform = new Intent(Solicitar.this, MapsActivity.class);
                finish();
                startActivity(nuevoform);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}

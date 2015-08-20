package com.ahorrapp.ahorrapp;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by Matías Cornejo R on 20-08-2015.
 */
public class Solicitar extends Activity{


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.solicitar_layout);
        TextView Mensaje ;
        Typeface typeFace=Typeface.createFromAsset(getAssets(),"font/rockwell condensed.ttf");
        Mensaje = (TextView) findViewById(R.id.Texto);
        Mensaje.setTypeface(typeFace);
        Mensaje.setText("Para solicitar permiso para administrar su negocio, favor enviar un correo a ahorrap.tds@gmail.com indicando " +
                "sus datos y adjuntando documentos que validen la propiedad del negocio, los cuales serán verificados por el equipo de ahorrap" +
                " gracias");



    }

}

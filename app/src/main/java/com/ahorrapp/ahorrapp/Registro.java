package com.ahorrapp.ahorrapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class Registro extends Activity{

    Button btnVoyaMenu;

    //Crear Vista
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro);

        btnVoyaMenu= (Button) findViewById(R.id.btnGuardar);

    }

    public void onClickbtnGuardar(View view){
        Intent i = new Intent(this, Menu.class);
    }

}


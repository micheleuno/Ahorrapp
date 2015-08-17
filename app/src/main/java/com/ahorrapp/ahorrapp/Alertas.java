package com.ahorrapp.ahorrapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;

public class Alertas {

    public static void mensaje_error(Activity vista, String mensaje){
        AlertDialog.Builder helpBuilder = new AlertDialog.Builder(vista);
        helpBuilder.setMessage(mensaje);
        helpBuilder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                    }
                });
        AlertDialog helpDialog = helpBuilder.create();
        helpDialog.show();
    }

    public static void mensaje_opcion(Activity vista, String mensaje){
        AlertDialog.Builder builder = new AlertDialog.Builder(vista);
        builder.setMessage("Esta seguro que desea eliminar el producto")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                       // MyActivity.this.finish(); //Close  this Activity for example: MyActivity.java
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // some code if you want
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}

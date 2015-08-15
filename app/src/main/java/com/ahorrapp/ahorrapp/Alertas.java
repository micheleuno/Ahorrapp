package com.ahorrapp.ahorrapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;

public class Alertas {

    public static void mensaje_error(FragmentActivity vista, String mensaje){
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
}

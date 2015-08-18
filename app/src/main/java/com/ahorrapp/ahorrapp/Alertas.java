package com.ahorrapp.ahorrapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class Alertas {

    public static void mensaje_error(Activity vista, String mensaje) {
        AlertDialog.Builder helpBuilder = new AlertDialog.Builder(vista);
        helpBuilder.setMessage(mensaje);
        helpBuilder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // NO hacer nada perp cerrar el dialog
                    }
                });
        AlertDialog helpDialog = helpBuilder.create();
        helpDialog.show();
    }
}

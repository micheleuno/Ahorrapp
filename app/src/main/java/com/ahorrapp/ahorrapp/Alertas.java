package com.ahorrapp.ahorrapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class Alertas {
private static ProgressDialog pDialog;
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

    public static  void abrir_mensaje_carga(Activity vista, String mensaje){
        pDialog = new ProgressDialog(vista);
        pDialog.setMessage(mensaje);
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    public static void cerrar_mensaje_carga(){
       pDialog.dismiss();
    }

    public static boolean Verificar_conexion( Activity vista){
        boolean connected;
        ConnectivityManager connectivityManager = (ConnectivityManager)vista.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            Log.d("Alertas", "conexion existente");
            connected = true;
        }
        else{
            Log.d("Alertas", "conexion inexistente");
            mensaje_error(vista,"No se encuentra conectado a internet.");
            connected = false;
        }
        return (connected);
    }
}

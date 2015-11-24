package com.ahorrapp.ahorrapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

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
        if(connectivityManager!=null&&(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)) {
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
    public static void cambiar_status_bar(Activity vista){
        Window window = vista.getWindow();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(vista.getResources().getColor(R.color.ColorPrimaryDark));
        }
    }

}

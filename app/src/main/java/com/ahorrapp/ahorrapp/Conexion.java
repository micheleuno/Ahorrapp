package com.ahorrapp.ahorrapp;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by miche on 16-11-2015.
 */
public class Conexion {

    public static boolean Verificar_conexion( Activity vista){
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)vista.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            Log.d("Conexion", "conexion existente");
            connected = true;
        }
        else{
            Log.d("Conexion", "conexion inexistente");
            connected = false;
        }


        return (connected);

    }

}

package com.ahorrapp.ahorrapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.HashMap;

public class SessionManager {
    // Shared Preferences
    SharedPreferences pref;
    // Editor for Shared preferences
    Editor editor;
    // Context
    Context _context;
    // Shared pref mode
    int PRIVATE_MODE = 0;
    // Sharedpref file name
    private static final String PREF_NAME = "AndroidHivePref";
    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";
    // name (make variable public to access from outside)
    public static final String TAG_NOMBRE = "Nombre_usuario";
    // username address (make variable public to access from outside)
    public static final String TAG_N_USUARIO = "Username";
    // Rut  (make variable public to access from outside)
    public static final String TAG_RUT = "Rut_usuario";
    // Id local  (make variable public to access from outside) 0 si no es dueno de ninguno
    public static final String TAG_LOCAL = "Id_local";
    public static final String TAG_EMAIL = "Email";
    public static final String TAG_DIRECCION = "Direccion";
    public static final String TAG_NOMBRE_ESTA = "Nombre";
    public static final String TAG_LATITUD = "Latitud";
    public static final String TAG_LONGITUD = "Longitud";
    public static final String TAG_BUSQUEDA = "Busqueda";
    public static final String TAG_NOMBRE_LOCAL_FLAG = "Nombre_local_flag";
    public static final String TAG_DISTANCIA_FLAG= "Distancia_flag";
    public static final String TAG_RUBRO_FLAG = "Rubro_flag";
    public static final String TAG_NOMBRE_PRODUCTO_FLAG = "Nombre_producto_flag";


    // Constructor
    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }
    public void addDataFlags(String Nombre_local_flag, String Distancia_flag, String Rubro_flag,String Nombre_producto_flag){
        editor.putString(TAG_NOMBRE_LOCAL_FLAG, Nombre_local_flag);
        editor.putString(TAG_DISTANCIA_FLAG, Distancia_flag);
        editor.putString(TAG_RUBRO_FLAG, Rubro_flag);
        editor.putString(TAG_NOMBRE_PRODUCTO_FLAG, Nombre_producto_flag);
        editor.commit();
    }

    public HashMap<String, String> getDataFlags(){
        HashMap<String, String> flags = new HashMap<>();
        // user name
        flags.put(TAG_NOMBRE_LOCAL_FLAG, pref.getString(TAG_NOMBRE_LOCAL_FLAG, null));
        // user usuario
        flags.put(TAG_DISTANCIA_FLAG, pref.getString(TAG_DISTANCIA_FLAG, null));
        // user Rut TAG_LONGITUD
        flags.put(TAG_RUBRO_FLAG, pref.getString(TAG_RUBRO_FLAG, null));
        flags.put(TAG_NOMBRE_PRODUCTO_FLAG, pref.getString(TAG_NOMBRE_PRODUCTO_FLAG, null));


        return flags;
    }

        public void addDataLocal(String nombre, String latitud, String longitud){
            editor.putString(TAG_NOMBRE_ESTA, nombre);
            editor.putString(TAG_LATITUD, latitud);
            editor.putString(TAG_LONGITUD, longitud);
            editor.commit();
        }
    //todo estan mal asignados
    public void addDataBusqueda(String busqueda){

        if(busqueda.equals("")){
            editor.remove(TAG_BUSQUEDA);
        }else{
            editor.putString(TAG_BUSQUEDA, busqueda);
        }

        editor.commit();
    }
    public HashMap<String, String> getDataBusqueda(){
        HashMap<String, String> busqueda = new HashMap<>();
        // user name
        busqueda.put(TAG_BUSQUEDA, pref.getString(TAG_BUSQUEDA, null));

        return busqueda;
    }


    public HashMap<String, String> getDataLocal(){
        HashMap<String, String> local = new HashMap<>();
        // user name
        local.put(TAG_NOMBRE_ESTA, pref.getString(TAG_NOMBRE_ESTA, null));
        // user usuario
        local.put(TAG_LATITUD, pref.getString(TAG_LATITUD, null));
        // user Rut TAG_LONGITUD
        local.put(TAG_LONGITUD, pref.getString(TAG_LONGITUD, null));

        return local;
    }
    /**
     * Create login session
     * */
    public void createLoginSession(String id_usuario,String name, String username, String Rut,String Id,String email, String direccion){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing name in pref
        editor.putString(TAG_NOMBRE, name);
        editor.putString("Id_usuario", id_usuario);
        // Storing username in pref
        editor.putString(TAG_N_USUARIO, username);
        // Storing Rut in pref
        editor.putString(TAG_RUT, Rut);

        // Storing Rut in pref
        editor.putString(TAG_LOCAL, Id);
        editor.putString(TAG_EMAIL, email);
        editor.putString(TAG_DIRECCION, direccion);

        // commit changes
        editor.commit();
    }


    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */
    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<>();
        // user name

        user.put("Id_usuario", pref.getString("Id_usuario", null));
        user.put(TAG_NOMBRE, pref.getString(TAG_NOMBRE, null));
        // user usuario
        user.put(TAG_N_USUARIO, pref.getString(TAG_N_USUARIO, null));
        // user Rut id
        user.put(TAG_RUT, pref.getString(TAG_RUT, null));
        // user  id
        user.put(TAG_LOCAL, pref.getString(TAG_LOCAL, null));
        // return user
        user.put(TAG_EMAIL, pref.getString(TAG_EMAIL, null));
        user.put(TAG_DIRECCION, pref.getString(TAG_DIRECCION, null));
        return user;
    }
    /**
     * Clear session details
     * */
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context,MapsActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // Staring Login Activity
        _context.startActivity(i);
    }
    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }
}
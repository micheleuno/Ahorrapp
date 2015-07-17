package com.ahorrapp.ahorrapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.HashMap;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Comentarios extends FragmentActivity{
/**    Integer Id;
    // -+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
    private GoogleMap googleMap;  // Might be null if Google Play services APK is not available.
    private Button mSubmit;

    ArrayList<HashMap<String,String>> establedes;

    // Clase JSONParser
    JSONParser jsonParser = new JSONParser();


    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCTS = "Establecimiento";
    private static final String TAG_LATITUD = "Latitud";
    private static final String TAG_LONGITUD = "Longitud";
    private static final String TAG_DIRECCION = "Direccion";
    private static final String TAG_NOMBRE = "Nombre";

    JSONArray products ;

    class AttemptLogin extends AsyncTask<String, String, String> {

        protected String doInBackground(String... args) {
            // Building Parameters
            List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
            params.add(new BasicNameValuePair("Nombre", Nombre));
            // getting JSON string from URL
            JSONObject json = jsonParser.makeHttpRequest("http://ahorrapp.hol.es/BD/cargar_comentarios.php", "POST", params);

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1){
                    // products found
                    // Getting Array of Products
                    products = json.getJSONArray(TAG_PRODUCTS);
                    Log.e("User Created!", json.toString());
                    Log.e("User Created!", Integer.toString(products.length()));
                    // looping through All Products
                    //Log.i("ramiro", "produtos.length" + products.length());
                    for (int i = 0; i < products.length(); i++) {
                        JSONObject c = products.getJSONObject(i);

                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String result)
        {


        }


    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comentario);
        Bundle bundle = getIntent().getExtras();
        Id = bundle.getInt("id");



    }

**/
}

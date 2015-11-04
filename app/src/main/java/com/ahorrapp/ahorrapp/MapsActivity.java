package com.ahorrapp.ahorrapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MapsActivity extends FragmentActivity{

    private GoogleMap googleMap;
    ArrayList<HashMap<Double,Double>> establepos;
    ArrayList<HashMap<String,String>> establedes;
    EditText Producto;
    JSONParser jsonParser = new JSONParser();
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCTS = "Establecimiento";
    private static final String TAG_LATITUD = "Latitud";
    private static final String TAG_LONGITUD = "Longitud";
    private static final String TAG_DIRECCION = "Direccion";
    private static final String TAG_NOMBRE = "Nombre";
    private static final String TAG_ID = "idEstablecimiento";
    private  String producto;
    private int success;
    JSONArray products ;

    class AttemptLogin extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
             producto = Producto.getText().toString();
        }

        protected String doInBackground(String... args) {
            List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
            params.add(new BasicNameValuePair("Nombre",producto ));
            JSONObject json = jsonParser.makeHttpRequest("http://ahorrapp.hol.es/BD/buscar_establecimientos.php", "POST", params);

            try {
                 success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                        products = json.getJSONArray(TAG_PRODUCTS);

                    //Log.i("Testing", "produtos.length" + products.length());

                    for (int i = 0; i < products.length(); i++) {
                        JSONObject c = products.getJSONObject(i);
                        Double latitud = Double.parseDouble(c.getString(TAG_LATITUD));
                        Double longitud = Double.parseDouble(c.getString(TAG_LONGITUD));
                        String direccion = c.getString(TAG_DIRECCION);
                        String nombre = c.getString(TAG_NOMBRE);
                        String id = c.getString(TAG_ID);
                        HashMap map = new HashMap();
                        HashMap<String, String> dir = new HashMap<String, String>();
                        map.put(TAG_LATITUD, latitud);
                        map.put(TAG_LONGITUD, longitud);
                        dir.put(TAG_DIRECCION, direccion);
                        dir.put(TAG_NOMBRE, nombre);
                        dir.put(TAG_ID, id);
                        establepos.add(i,map);
                        establedes.add(i,dir);
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        return null;
        }

        protected void onPostExecute(String result){
            if(success==0)
                Alertas.mensaje_error(MapsActivity.this, "No se encontro ningun producto");
            else{
            int cont=0;
            HashMap<Double, Double> pos;
            HashMap<String, String> dir;
            while(cont<establedes.size()){
                pos=establepos.get(cont);
                dir=establedes.get(cont);
                addMarker(pos.get(TAG_LATITUD), pos.get(TAG_LONGITUD), dir.get(TAG_NOMBRE), dir.get(TAG_DIRECCION),dir.get(TAG_ID));
                cont++;
            }
            establedes.clear();
            establepos.clear();

        }}
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        establepos = new  ArrayList<HashMap<Double, Double>>();
        establedes = new  ArrayList<HashMap<String, String>>();
        Producto = (EditText) findViewById(R.id.txtProducto);
        Producto.setText("");
        createMapView();
        Mostrar_locales();
        final Button menu = (Button) findViewById(R.id.btnopciones);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nuevoform = new Intent(MapsActivity.this, com.ahorrapp.ahorrapp.Menu.class);
                finish();
                startActivity(nuevoform);
            }
        });



        final Button Productos = (Button) findViewById(R.id.buscador);
        Productos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Producto.getText();
                Mostrar_locales();
            }
        });

        googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {

            @Override
            public void onMapLongClick(LatLng latLng)

                googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(latLng.latitude, latLng.longitude))
                        .draggable(true)
                        .title("Nuevo Establencimiento")
                        .snippet("Presionar para crear establecimiento"));
            }

        });


        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent nuevoform = new Intent(MapsActivity.this, Local.class);
                Double latitud = marker.getPosition().latitude;
                Double longitud = marker.getPosition().longitude;
                nuevoform.putExtra("latitude", Double.toString(latitud));
                nuevoform.putExtra("longitude",  Double.toString(longitud));
                nuevoform.putExtra("nombre", marker.getTitle());
                startActivity(nuevoform);
            }
        });
    }



        @Override
        public void onBackPressed() {
            Intent nuevoform = new Intent(MapsActivity.this, MapsActivity.class);
            finish();
            startActivity(nuevoform);
        }

    private void createMapView(){
         final LatLng UPV = new LatLng(-33.044662, -71.612465);
        try {
            if(null == googleMap){
                googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
                if (googleMap != null) {
                    // El objeto GoogleMap ha sido referenciado correctamente
                    //ahora podemos manipular sus propiedades
                    //Seteamos el tipo de mapa
                    googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(UPV, 15));
                    //Activamos la capa o layer MyLocation
                    googleMap.setMyLocationEnabled(true);
                }
                if(null == googleMap) {
                    Toast.makeText(getApplicationContext(),
                    "Error creating map", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (NullPointerException exception){
            Log.e("mapApp", exception.toString());
        }
    }

    private void addMarker(Double Lat, Double Long, String Nombre,String Direccion, String Id){

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < (Nombre.length()-Direccion.length()); i++) {
            if (i > 0) {
                result.append(" ");
            }
        }
        result.append(Direccion);
        String snippet;
        snippet = result.toString();
        if(null != googleMap){
            googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(Lat, Long))
                .title(Nombre)
                .draggable(false)
                .snippet(snippet));
        }
    }
    private void Mostrar_locales() {
        googleMap.clear();
        new AttemptLogin().execute();
    }
}

package com.ahorrapp.ahorrapp;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;



public class MapsActivity extends FragmentActivity {

    private GoogleMap googleMap; ; // Might be null if Google Play services APK is not available.



    /**
     * Initialises the mapview
     */
    private void createMapView(){
        /**
         * Catch the null pointer exception that
         * may be thrown when initialising the map
         */
        try {
            if(null == googleMap){
                googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

                if (googleMap != null) {
                    // El objeto GoogleMap ha sido referenciado correctamente
                    //ahora podemos manipular sus propiedades

                    //Seteamos el tipo de mapa
                    googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                    //Activamos la capa o layer MyLocation
                    googleMap.setMyLocationEnabled(true);
                }

                /**
                 * If the map is still null after attempted initialisation,
                 * show an error to the user
                 */
                if(null == googleMap) {
                    Toast.makeText(getApplicationContext(),
                            "Error creating map", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (NullPointerException exception){
            Log.e("mapApp", exception.toString());
        }


    }






    /*
      Adds a marker to the map
**/
    private void addMarker(){


        /** Make sure that the map has been initialised*/


        if(null != googleMap){
            googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(0,0))
                            .title("Marker")
                            .draggable(true)
            );
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        createMapView();

        final Button marcador = (Button) findViewById(R.id.buscador);
        marcador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMarker();

            }
        });

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(final Marker marker) {
                Log.e("TESTING", "on Marker click: " + marker.getTitle());
                if (!marker.isInfoWindowShown())
                    marker.showInfoWindow();
                else
                    marker.hideInfoWindow();

                return true;
            }
        });


    }













}

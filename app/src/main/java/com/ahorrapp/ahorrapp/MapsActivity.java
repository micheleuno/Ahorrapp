package com.ahorrapp.ahorrapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class MapsActivity extends AppCompatActivity  implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap googleMap;
    ArrayList<HashMap<Double,Double>> establepos;
    ArrayList<HashMap<String,String>> establedes;
    String latitud,longitud,altura_camara;
    EditText Producto;
    JSONParser jsonParser = new JSONParser();
    Marker marker;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCTS = "Establecimiento";
    private static final String TAG_LATITUD = "Latitud";
    private static final String TAG_LONGITUD = "Longitud";
    private static final String TAG_LATITUD_CAMARA = "Latitud_camara";
    private static final String TAG_LONGITUD_CAMARA = "Longitud_camara";
    private static final String TAG_DIRECCION = "Direccion";
    private static final String TAG_NOMBRE = "Nombre";
    private static final String TAG_ID = "idEstablecimiento";
    private GoogleApiClient mGoogleApiClient;
    SessionManager session;
    private  String producto,id_marker="0",es_dueño="-1",nomb_local_flag="0",rubro_flag="0",nombre_producto_flag="1",distancia_flag="0";
    private int success;
    JSONArray products ;

    @Override
    public void onConnected(Bundle connectionHint) {
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
           latitud= String.valueOf(mLastLocation.getLatitude());
            longitud= String.valueOf(mLastLocation.getLongitude());
           // final LatLng UPV = new LatLng(Double.parseDouble(latitud), Double.parseDouble(longitud));
            //googleMap.moveCamera(CameraUpdateFactory.newLatLng(UPV));
            Mostrar_locales();
            Log.e("MapsActivity Longitud: ",longitud+" Latitud: "+latitud);
        }
    }



    @Override
    public void onConnectionSuspended(int i) {
        LatLng position=googleMap.getCameraPosition().target;
        latitud=Double.toString(position.latitude);
        longitud=Double.toString(position.longitude);
         Mostrar_locales();

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        LatLng position=googleMap.getCameraPosition().target;
        latitud=Double.toString(position.latitude);
        longitud=Double.toString(position.longitude);
         Mostrar_locales();

    }

    class AttemptLogin extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            producto = Producto.getText().toString();
            super.onPreExecute();
            Alertas.abrir_mensaje_carga(MapsActivity.this,"Buscando");
        }

        protected String doInBackground(String... args) {

            try {
                HashMap<String, String> params = new HashMap<>();
                params.put("Nombre", producto.trim());
                params.put("Latitud", latitud);
                params.put("Longitud", longitud);
                params.put("Nombre_producto_flag", nombre_producto_flag);
                params.put("Nombre_local_flag", nomb_local_flag);
                params.put("Rubro", rubro_flag);
                params.put("Distancia", distancia_flag);
                JSONObject json = jsonParser.makeHttpRequest("http://ahorrapp.hol.es/BD/buscar_establecimientos_dis.php", "POST", params);

                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    products = json.getJSONArray(TAG_PRODUCTS);

                    for (int i = 0; i < products.length(); i++) {
                        JSONObject c = products.getJSONObject(i);
                        Double latitud = Double.parseDouble(c.getString(TAG_LATITUD));
                        Double longitud = Double.parseDouble(c.getString(TAG_LONGITUD));
                        String direccion = c.getString(TAG_DIRECCION);
                        String nombre = c.getString(TAG_NOMBRE);
                        String id = c.getString(TAG_ID);
                        HashMap<Double,Double> map = new HashMap<>();
                        HashMap<String, String> dir = new HashMap<>();
                        map.put(1.0, latitud);
                        map.put(2.0, longitud);
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
            //si se hizo la consulta, pero es vacia
            if(success==0){
                Producto.setText("");
                Alertas.mensaje_error(MapsActivity.this, "No se encontró ningún producto o establecimiento");
            }
            //Si es dos es un error desde la consulta
            else if (success!=2){
                int cont=0;
                HashMap<Double, Double> pos;
                HashMap<String, String> dir;
                while(cont<establedes.size()){
                    pos=establepos.get(cont);
                    dir=establedes.get(cont);
                    addMarker(pos.get(1.0), pos.get(2.0), dir.get(TAG_NOMBRE), dir.get(TAG_DIRECCION),cont);
                    cont++;
                }
                establedes.clear();
                establepos.clear();
            }else{
                Alertas.mensaje_error(MapsActivity.this,"Ha ocurrido un error con la consulta");
            }
            Alertas.cerrar_mensaje_carga();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_mapa, menu);


        if(distancia_flag.equals("1")){
            MenuItem item =  menu.findItem(R.id.Distancia);
            item.setChecked(true);
        }
        if(rubro_flag.equals("1")){
            MenuItem item =  menu.findItem(R.id.Rubro);
            item.setChecked(true);
        }
        if(nomb_local_flag.equals("1")){
            MenuItem item =  menu.findItem(R.id.Nombre_local);
            item.setChecked(true);
        }
        if(nombre_producto_flag.equals("1")){
            MenuItem item =  menu.findItem(R.id.nombre_produto);
            item.setChecked(true);
        }

        return true;
    }

    public void onCheckboxClicked(MenuItem item) {
        // Is the view now checked?
        boolean checked = item.isChecked();
        // Check which checkbox was clicked
        switch(item.getItemId()) {
            case R.id.nombre_produto:
                item.setChecked(!item.isChecked());
                if (checked){
                    nombre_producto_flag="0";
                }else{
                    nombre_producto_flag="1";
                    nomb_local_flag=rubro_flag="0";
                }
                break;
            case R.id.Nombre_local:
                item.setChecked(!item.isChecked());
                if (checked){
                    nomb_local_flag="0";
                }else{
                    nomb_local_flag="1";
                    nombre_producto_flag=rubro_flag="0";
                }
                break;

            case R.id.Rubro:
                item.setChecked(!item.isChecked());
                if (checked){
                    rubro_flag  ="0";
                }else{
                    rubro_flag="1";
                    nombre_producto_flag=nomb_local_flag="0";
                }
                break;

            case R.id.Distancia:
                item.setChecked(!item.isChecked());
                if (checked){
                    distancia_flag="0";
                }else{
                    distancia_flag="1";
                }
               Log.e("distancia_flag: ", distancia_flag);
                break;

        }
       Log.e("Distancia_f checkboxs","asd");
        session.addDataFlags(nomb_local_flag,distancia_flag,rubro_flag,nombre_producto_flag);
        Mostrar_locales();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        String[] datos_navdraw;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        buildGoogleApiClient();

        Alertas.cambiar_status_bar(MapsActivity.this);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle(Html.fromHtml("<font color='#FFFFFF'></font>"));
        myToolbar.setNavigationIcon(R.drawable.ic_menu_white_36dp);
        setSupportActionBar(myToolbar);
        session = new SessionManager(getApplicationContext());
        if(MapsActivity.this.session.isLoggedIn()){
            HashMap<String, String> user = session.getUserDetails();
            es_dueño = user.get(SessionManager.TAG_LOCAL);
            if(es_dueño.equals("0"))     //no es dueño de local
                datos_navdraw = getResources().getStringArray(R.array.sesion_iniciada);
            else  //es dueño de un local
                datos_navdraw = getResources().getStringArray(R.array.dueño_local);
        }else{//no ha iniciado sesion
            datos_navdraw = getResources().getStringArray(R.array.inicio_sesion);
        }

        // mDrawerList.setOnItemClickListener(new DrawerItemClickListener());



        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerList.setBackgroundResource(R.drawable.color_drawer);

        mDrawerList.setAdapter(new ArrayAdapter<>(this,
                R.layout.drawer_list_item, datos_navdraw));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());


        establepos = new ArrayList<>();
        establedes = new ArrayList<>();
        Producto = (EditText) findViewById(R.id.txtProducto);
        if(!session.getDataBusqueda().isEmpty()){
            HashMap<String, String> busqueda = session.getDataBusqueda();
            Producto.setText(busqueda.get("Busqueda"));
        }else
            Producto.setText("");

            HashMap<String, String> busqueda2;
            busqueda2 = session.getDataFlags();
            if(busqueda2.get("Nombre_local_flag")!=null){
                nomb_local_flag = busqueda2.get("Nombre_local_flag");
                distancia_flag =  busqueda2.get("Distancia_flag");
                rubro_flag = busqueda2.get("Rubro_flag");
                nombre_producto_flag = busqueda2.get("Nombre_producto_flag");
                Log.e("desde con datos ", "asd");

            }



        createMapView();

            LatLng position=googleMap.getCameraPosition().target;
            latitud=Double.toString(position.latitude);
            longitud=Double.toString(position.longitude);


        Producto.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) { //ver si se presiona enter en el teclado
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                                session = new SessionManager(getApplicationContext());
                                session.addDataBusqueda(Producto.getText().toString());
                                Producto.getText();
                                Mostrar_locales();

                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });
        googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {

            @Override
            public void onMapLongClick(LatLng latLng) {

                if (MapsActivity.this.session.isLoggedIn()){
                    if(!id_marker.equals("0")){
                        marker.remove();
                    }else
                        Toast.makeText(MapsActivity.this, "Una vez posicionado, toque el pin para crear el establecimiento", Toast.LENGTH_LONG).show();

                    marker = googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(latLng.latitude, latLng.longitude))
                            .draggable(true)
                            .title("Nuevo Establecimiento")
                            .snippet("Presionar para crear establecimiento"));
                    id_marker="1";
                }else{
                    Alertas.mensaje_error(MapsActivity.this, "Para agregar establecimientos debe iniciar sesión");
                }
            }

        });


        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                if( Alertas.Verificar_conexion(MapsActivity.this)){ //Si hay conexion a la red
                    if(marker.getTitle().equals("Nuevo Establecimiento")){
                        session.addDataPosCamara(Double.toString(marker.getPosition().latitude),Double.toString(marker.getPosition().longitude),altura_camara);
                        Log.e("mapsactivity",altura_camara);
                        Intent nuevoform = new Intent(MapsActivity.this, Registrar_establecimiento.class);
                        Double latitud = marker.getPosition().latitude;
                        Double longitud = marker.getPosition().longitude;
                        nuevoform.putExtra("latitude", Double.toString(latitud));
                        nuevoform.putExtra("longitude",  Double.toString(longitud));
                        startActivity(nuevoform);

                    }
                    else{
                        Intent nuevoform = new Intent(MapsActivity.this, Local.class);
                        session.addDataPosCamara(Double.toString(marker.getPosition().latitude),Double.toString(marker.getPosition().longitude),Float.toString(googleMap.getCameraPosition().zoom  ));
                        Double latitud = marker.getPosition().latitude;
                        Double longitud = marker.getPosition().longitude;
                        nuevoform.putExtra("latitude", Double.toString(latitud));
                        nuevoform.putExtra("longitude",  Double.toString(longitud));
                        Log.e("mapsactivity", Float.toString(googleMap.getCameraPosition().zoom  ));
                        nuevoform.putExtra("nombre", marker.getTitle());
                        startActivity(nuevoform);
                    }
                }
            }
        });

    }
    protected synchronized void buildGoogleApiClient() {
         mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
        }
    }
    private void selectItem(int position) {
        // Create a new fragment and specify the planet to show based on position
        Intent nuevoform;
        switch (position){
            case 0:
                if(Alertas.Verificar_conexion(MapsActivity.this)) {
                    Log.e("Maps activity",es_dueño+" eso vale es dueño");
                    if(es_dueño.equals("0")){ //no es dueño local
                        Log.e("Maps activity","fue a solicitar");
                        nuevoform = new Intent(MapsActivity.this, Solicitar.class);
                        finish();
                        startActivity(nuevoform);
                        break;
                    }
                    if(Integer.parseInt(es_dueño)>0){   //es dueño
                        Log.e("Maps activity","fue a negocio");
                        nuevoform = new Intent(MapsActivity.this, Negocio.class);
                        finish();
                        startActivity(nuevoform);
                        break;
                    }
                    Log.e("Maps activity","fue a menu");
                    nuevoform = new Intent(MapsActivity.this, com.ahorrapp.ahorrapp.Menu_a.class);
                    finish();
                    startActivity(nuevoform);
                }
                break;


            case 1:

                if(Alertas.Verificar_conexion(MapsActivity.this)&&session.isLoggedIn()) {
                    nuevoform = new Intent(MapsActivity.this, Perfil.class);  //esta logeado
                    finish();
                    startActivity(nuevoform);
                    break;
                }
                nuevoform = new Intent(MapsActivity.this, Registro.class);
                finish();
                startActivity(nuevoform);
                break;

            case 2:
                if(Alertas.Verificar_conexion(MapsActivity.this)) {
                    if(session.isLoggedIn()){
                        session.logoutUser();
                        Toast.makeText(this, "Se ha cerrado la sesion", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
                nuevoform = new Intent(MapsActivity.this, Enviar_email.class);
                finish();
                startActivity(nuevoform);

                break;
            default:

        }
        // Highlight the selected item, update the title, and close the drawer
        mDrawerList.setItemChecked(position, true);
        mDrawerLayout.closeDrawer(mDrawerList);
    }



    private void createMapView(){
        LatLng UPV = new LatLng(-33.044662, -71.612465);
        float altura = 15;
        HashMap<String, String> posicion_camara;
        posicion_camara = session.getDataPosCamara();
        if(posicion_camara.get("Altura_camara")!=null){
            UPV = new LatLng(Double.parseDouble(posicion_camara.get("Latitud_camara")),Double.parseDouble(posicion_camara.get("Longitud_camara")));
            altura=Float.parseFloat(posicion_camara.get("Altura_camara"));
            altura_camara=Float.toString(altura);
        }else
            altura_camara=Float.toString(altura);

        try {
            if(null == googleMap){
                googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
                if (googleMap != null) {
                    // El objeto GoogleMap ha sido referenciado correctamente
                    //ahora podemos manipular sus propiedades
                    //Seteamos el tipo de mapa
                    if (ContextCompat.checkSelfPermission(MapsActivity.this,
                            Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(MapsActivity.this,
                                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                1);

                    }else{
                        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(UPV, altura));
                        //Activamos la capa o layer MyLocation
                        googleMap.setMyLocationEnabled(true);
                    }


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

    private void addMarker(Double Lat, Double Long, String Nombre,String Direccion, int Id){

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < (Nombre.length()-Direccion.length()); i++) {
            if (i > 0) {
                result.append(" ");
            }
        }
        result.append(Direccion);
        String snippet;
        snippet = result.toString();
        int[] marcadores = {R.mipmap.marker1,R.mipmap.marker2,R.mipmap.marker3,R.mipmap.marker4,R.mipmap.marker5};

        if(Id<5&& !Producto.getText().toString().equals("")) {
            googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(Lat, Long))
                            .title(Nombre)
                            .draggable(false)
                            .snippet(snippet)
                            .icon(BitmapDescriptorFactory.fromResource(marcadores[Id]))
            );
        }else{
            googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(Lat, Long))
                            .title(Nombre)
                            .draggable(false)
                            .snippet(snippet)
            );
        }



    }
    private void Mostrar_locales() {
        if( Alertas.Verificar_conexion(MapsActivity.this)){ //si hay conexion a internet
            googleMap.clear();
            new AttemptLogin().execute();
        }
    }
    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Presione atrás de nuevo para salir", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: //al apretar el menu
                mDrawerLayout.openDrawer(mDrawerList);
                return true;
            case R.id.buscar_estab:
                    session = new SessionManager(getApplicationContext());
                    session.addDataBusqueda(Producto.getText().toString());
                    Producto.getText();
                    Mostrar_locales();

                return true;
            default:
                return true;
        }
    }

}

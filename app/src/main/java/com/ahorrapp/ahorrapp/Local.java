package com.ahorrapp.ahorrapp;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.melnykov.fab.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Local extends FragmentActivity {

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCTS = "Establecimiento";
    private static final String TAG_DIRECCION = "Direccion";
    private static final String TAG_NOMBRE = "Nombre";
    private static final String TAG_CONTACTO = "Contacto";
    private static final String TAG_DESCRIPCCION = "Descripcion";
    private static final String TAG_ID = "IdEstablecimiento";
    private static final String TAG_NOMBREP = "Nombre";
    private static final String TAG_PRECIO = "Precio";
    private static final String TAG_PRODUCTO = "Producto";
    private static final String TAG_UNIDAD = "Unidad";
    SessionManager session;
    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    JSONParser jsonParserp = new JSONParser();
    JSONArray products ;
    JSONArray productsp ;
    ArrayList<HashMap<String,String>> establedes;
    ArrayList<HashMap<String,String>> productos;
    ArrayList<Lista_productos> produc;
    String Latitud,Longitud,Nombre;
    String Id;

    class AttemptLocal extends AsyncTask<String, String, String> {

        protected String doInBackground(String... args) {
            // Building Parameters

            // getting JSON string from URL
            HashMap<String, String> params = new HashMap<>();
            params.put("Latitud", Latitud);
            params.put("Longitud", Longitud);

            JSONObject json = jsonParser.makeHttpRequest("http://ahorrapp.hol.es/BD/cargar_datos_establecimiento.php", "POST", params);
            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    products = json.getJSONArray(TAG_PRODUCTS);
                    // looping through All Products
                    for (int i = 0; i < products.length(); i++) {
                        JSONObject c = products.getJSONObject(i);
                        // Storing each json item in variable
                        String address = c.getString(TAG_DIRECCION);
                        String local = c.getString(TAG_NOMBRE);
                        String contacto = c.getString(TAG_CONTACTO);
                        String descripcion = c.getString(TAG_DESCRIPCCION);
                        String id = c.getString(TAG_ID);
                        Id=id;
                        // creating new HashMap
                        HashMap<String, String> dir = new HashMap<>();
                        // adding each child node to HashMap key => value
                        dir.put(TAG_DIRECCION, address);
                        dir.put(TAG_NOMBRE, local);
                        dir.put(TAG_ID, id);
                        dir.put(TAG_CONTACTO, contacto);
                        dir.put(TAG_DESCRIPCCION, descripcion);
                        establedes.add(i,dir);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String result)
        {
            int cont=0;
            HashMap<String, String> dir;
            while(cont<establedes.size()){
                dir=establedes.get(cont);
                Typeface typeFace=Typeface.createFromAsset(getAssets(),"font/rockwell condensed.ttf"); //Cargar tipo de letra
                TextView nombre =(TextView) findViewById(R.id.txtNombreLocal);
                nombre.setTypeface(typeFace);
                nombre.setText(Nombre);
                TextView direccion =(TextView) findViewById(R.id.txtDireccionLocal);
                direccion.setTypeface(typeFace);
                direccion.setText(dir.get(TAG_DIRECCION));
                TextView descripcion =(TextView) findViewById(R.id.txtdescripcion);
                descripcion.setTypeface(typeFace);
                descripcion.setText(dir.get(TAG_DESCRIPCCION));
                TextView contacto =(TextView) findViewById(R.id.txtContactoLocal);
                contacto.setTypeface(typeFace);
                contacto.setText(dir.get(TAG_CONTACTO));
                cont++;
            }
            establedes.clear();
        }
    }


    class AttemptProducto extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Local.this);
            pDialog.setMessage("Cargando");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected String doInBackground(String... args) {

            HashMap<String, String> params = new HashMap<>();
            params.put("idEstablecimiento", Id);

            JSONObject jsonp = jsonParserp.makeHttpRequest("http://ahorrapp.hol.es/BD/cargar_productos.php", "POST", params);
            try {
                int success = jsonp.getInt(TAG_SUCCESS);
                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    productsp = jsonp.getJSONArray(TAG_PRODUCTO);
                    // looping through All Products
                    for (int j = 0; j < productsp.length(); j++) {
                        JSONObject p = productsp.getJSONObject(j);
                        // Storing each json item in variable
                        String precio = p.getString(TAG_PRECIO);
                        String Producto = p.getString(TAG_NOMBREP);
                        String Unidad = p.getString(TAG_UNIDAD);
                        // creating new HashMap
                        HashMap<String, String> pro = new HashMap<>();
                        // adding each child node to HashMap key => value
                        pro.put(TAG_PRECIO, precio);
                        pro.put(TAG_NOMBREP, Producto);
                        pro.put(TAG_UNIDAD, Unidad);
                        productos.add(pro);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String result){

            runOnUiThread(new Runnable() {
                public void run() {
                    int cont = 0;
                    HashMap<String, String> pro;
                    while (cont < productos.size()) {

                        pro = productos.get(cont);
                        produc.add(new Lista_productos(pro.get(TAG_NOMBREP), pro.get(TAG_PRECIO), pro.get(TAG_UNIDAD), pro.get(TAG_UNIDAD)));
                        cont++;
                    }
                    productos.clear();
                    ListView lista = (ListView) findViewById(R.id.productos);

                    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
                    fab.attachToListView(lista);

                    lista.setAdapter(new Lista_adaptador(Local.this, R.layout.productos, produc) {
                        @Override
                        public void onEntrada(Object entrada, View view) {
                            Typeface typeFace = Typeface.createFromAsset(getAssets(), "font/rockwell condensed.ttf");
                            TextView texto_nombre = (TextView) view.findViewById(R.id.Nombre);
                            texto_nombre.setTypeface(typeFace);
                            texto_nombre.setText(((Lista_productos) entrada).get_nombre());

                            TextView texto_precio = (TextView) view.findViewById(R.id.Precio);
                            texto_precio.setTypeface(typeFace);
                            texto_precio.setText(((Lista_productos) entrada).get_precio());

                            TextView texto_unidad = (TextView) view.findViewById(R.id.Unidad);
                            texto_unidad.setTypeface(typeFace);
                            texto_unidad.setText(((Lista_productos) entrada).get_unidad());

                            TextView id_producto = (TextView) view.findViewById(R.id.idproducto);
                            id_producto.setTypeface(typeFace);
                            id_producto.setText(((Lista_productos) entrada).get_id());
                        }

                    });
                    fab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Local.this.session = new SessionManager(Local.this.getApplicationContext());
                            if (Local.this.session.isLoggedIn()) {
                                Intent nuevoform = new Intent(Local.this, Agregar_producto.class);
                                nuevoform.putExtra("id", Id);
                                startActivity(nuevoform);
                            }else{
                                Alertas.mensaje_error(Local.this, "Para agregar productos debe iniciar sesion");
                            }
                        }
                    });


                }


            });
            pDialog.dismiss();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.establecimiento);
        SessionManager session;
        session = new SessionManager(getApplicationContext());
        Bundle bundle;
        new Bundle();
        bundle = getIntent().getExtras();
        if(bundle != null) {
            Latitud = bundle.getString("latitude");
            Longitud = bundle.getString("longitude");
            Nombre = bundle.getString("nombre");

            session.addDataLocal(Latitud,Longitud,Nombre);
        }else{

            HashMap<String, String> local = session.getDataLocal();
            Latitud  = local.get(SessionManager.TAG_NOMBRE_ESTA);
            Nombre  = local.get(SessionManager.TAG_LONGITUD);
            Longitud = local.get(SessionManager.TAG_LATITUD);

            //todo estan mal asignados
        }
        establedes = new ArrayList<>();
        productos = new ArrayList<>();
        produc = new ArrayList<>();
        Mostrar_locales();
        Typeface typeFace=Typeface.createFromAsset(getAssets(),"font/rockwell condensed.ttf");
        TextView text1 =(TextView) findViewById(R.id.txtdireccion);
        text1.setTypeface(typeFace);
        TextView text2 =(TextView) findViewById(R.id.textcontacto);
        text2.setTypeface(typeFace);
        TextView text3 =(TextView) findViewById(R.id.textproducto);
        text3.setTypeface(typeFace);
        final Button perfil = (Button) findViewById(R.id.btnComentarios);
        perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nuevoform = new Intent(Local.this, Comentarios.class);
                nuevoform.putExtra("id", Id);
                startActivity(nuevoform);
            }
        });


    }

    public void onBackPressed() {
        Intent nuevoform = new Intent(Local.this, MapsActivity.class);
        finish();
        startActivity(nuevoform);
    }

    private void Mostrar_locales() {
        new AttemptLocal().execute();
        new AttemptProducto().execute();
       // Log.e("MIchele", Integer.toString(datos.size()));
    }


}

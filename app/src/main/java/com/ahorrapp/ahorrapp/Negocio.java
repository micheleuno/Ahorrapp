package com.ahorrapp.ahorrapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Negocio extends Activity {
    // -+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
    private EditText name,precio;
    String unidad_id,names,precios;
    ArrayList<HashMap<String,String>> productos;
    ArrayList<HashMap<String,String>> unidades;
    ArrayList<Combobox> datos;
    ArrayList<Lista_productos> produc;
    // Clase JSONParser
    SessionManager session;
    Spinner lista;
    ListView lista_p;
    JSONParser jsonParserp = new JSONParser();
    JSONParser jsonParser = new JSONParser();

    // JSON Node names establecimiento
    private static final String TAG_SUCCESS = "success";
    //JSON Node names producto
    private static final String TAG_UNIDADES = "Unidades";
    private static final String TAG_UNIDAD = "Unidad";
    private static final String TAG_ID_UNIDAD = "Id";

    //JSON Node names producto
    private static final String TAG_NOMBREP = "Nombre";
    private static final String TAG_PRECIO = "Precio";
    private static final String TAG_PRODUCTO = "Producto";
    private ProgressDialog pDialog;
    JSONArray unidad ;
    JSONArray productsp ;
    //agregar producto
    private static final String TAG_Id_establecimiento = "Id_establecimiento";


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perfil_negocio);
        Typeface typeFace=Typeface.createFromAsset(getAssets(),"font/rockwell condensed.ttf");

        unidades = new  ArrayList<HashMap<String, String>>();
        productos = new  ArrayList<HashMap<String, String>>();
        datos = new ArrayList<Combobox>();
        produc = new ArrayList<Lista_productos>();
        name = (EditText) findViewById(R.id.editnombre);
        name.setTypeface(typeFace);
        precio = (EditText) findViewById(R.id.editprecio);
        precio.setTypeface(typeFace);
        lista = (Spinner) findViewById(R.id.Unidades);
        lista_p = (ListView)findViewById(R.id.listProductos);
        new AttemptUnidad().execute();
        new AttemptProducto().execute();

        lista.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Combobox hola = (Combobox) lista.getItemAtPosition(position);
                unidad_id = hola.get_id();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        lista_p.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,int position, long id) {

                Lista_productos productos = (Lista_productos) lista_p.getItemAtPosition(position);
                Intent nuevoform = new Intent(Negocio.this, Opciones_producto.class);
                nuevoform.putExtra("nombre", productos.get_nombre());
                nuevoform.putExtra("precio", productos.get_precio());
                nuevoform.putExtra("unidad", productos.get_unidad());
                startActivity(nuevoform);
            }
        });


        final Button agregar = (Button) findViewById(R.id.btnagregar);
        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                names = name.getText().toString();
                precios = precio.getText().toString();
                new AttemptAgregar().execute();

            }
        });





}

    @Override
    public void onBackPressed() {
        Intent nuevoform = new Intent(Negocio.this, MapsActivity.class);
        finish();
        startActivity(nuevoform);
    }

    class AttemptUnidad extends AsyncTask<String, String, String> {

        protected String doInBackground(String... args) {
            List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
            params.add(new BasicNameValuePair("IdEstablecimiento", "0" ));
            JSONObject json = jsonParserp.makeHttpRequest("http://ahorrapp.hol.es/BD/cargar_unidades.php", "POST", params);
            try {

                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {

                    // products found
                    // Getting Array of Products
                    unidad = json.getJSONArray(TAG_UNIDADES);

                    // looping through All Products
                    //Log.i("ramiro", "produtos.length" + products.length());

                    for (int j = 0; j < unidad.length(); j++) {
                        JSONObject p = unidad.getJSONObject(j);

                        // Storing each json item in variable
                        String unidad = p.getString(TAG_UNIDAD);
                        String id = p.getString(TAG_ID_UNIDAD);
                        // creating new HashMap
                        HashMap<String, String> pro = new HashMap<String, String>();


                        // adding each child node to HashMap key => value
                        pro.put(TAG_UNIDAD, unidad);
                        pro.put(TAG_ID_UNIDAD,id);
                        unidades.add(pro);

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String result)
        {

            runOnUiThread(new Runnable() {
                public void run() {

                    int cont = 0;

                    HashMap<String, String> pro;
                    while (cont < unidades.size()) {

                        pro = unidades.get(cont);
                        datos.add(new Combobox(pro.get(TAG_UNIDAD), pro.get(TAG_ID_UNIDAD)));

                        cont++;
                    }
                    unidades.clear();


                    lista.setAdapter(new Lista_adaptador(Negocio.this, R.layout.combobox, datos) {
                        @Override
                        public void onEntrada(Object entrada, View view) {
                            Typeface typeFace=Typeface.createFromAsset(getAssets(),"font/rockwell condensed.ttf");
                            TextView texto = (TextView) view.findViewById(R.id.unidad);
                            texto.setTypeface(typeFace);
                            texto.setText(((Combobox) entrada).get_texto());

                            TextView texto_id = (TextView) view.findViewById(R.id.id_unidad);
                            texto_id.setTypeface(typeFace);
                            texto_id.setText(((Combobox) entrada).get_id());

                        }
                    });

                }


            });

        }

    }


    class AttemptProducto extends AsyncTask<String, String, String> {
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Negocio.this);
            pDialog.setMessage("Cargando");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected String doInBackground(String... args) {
            List<BasicNameValuePair> paramsp = new ArrayList<BasicNameValuePair>();
            session = new SessionManager(getApplicationContext());
            // get user data from session
            HashMap<String, String> user = session.getUserDetails();

            paramsp.add(new BasicNameValuePair("idEstablecimiento", user.get(SessionManager.TAG_LOCAL)));
            JSONObject jsonp = jsonParserp.makeHttpRequest("http://ahorrapp.hol.es/BD/cargar_productos.php", "POST", paramsp);
            try {

                int success = jsonp.getInt(TAG_SUCCESS);

                if (success == 1) {

                    // products found
                    // Getting Array of Products
                    productsp = jsonp.getJSONArray(TAG_PRODUCTO);

                    // looping through All Products
                    //Log.i("ramiro", "produtos.length" + products.length());
                    for (int j = 0; j < productsp.length(); j++) {
                        JSONObject p = productsp.getJSONObject(j);

                        // Storing each json item in variable
                        String precio = p.getString(TAG_PRECIO);
                        String Producto = p.getString(TAG_NOMBREP);
                        String Unidad =p.getString(TAG_UNIDAD);

                        // creating new HashMap
                        HashMap<String, String> pro = new HashMap<String, String>();


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

        protected void onPostExecute(String result)
        {

            runOnUiThread(new Runnable() {
                public void run() {

                    int cont=0;

                    HashMap<String, String> pro;
                    while(cont<productos.size()){

                        pro=productos.get(cont);
                        produc.add(new Lista_productos(pro.get(TAG_NOMBREP), pro.get(TAG_PRECIO),pro.get(TAG_UNIDAD)));

                        cont++;
                    }
                    productos.clear();


                    lista_p.setAdapter(new Lista_adaptador(Negocio.this, R.layout.productos, produc) {
                        @Override
                        public void onEntrada(Object entrada, View view) {
                            Typeface typeFace=Typeface.createFromAsset(getAssets(),"font/rockwell condensed.ttf");
                            TextView texto_nombre = (TextView) view.findViewById(R.id.Nombre);
                            texto_nombre.setTypeface(typeFace);
                            texto_nombre.setText(((Lista_productos) entrada).get_nombre());

                            TextView texto_precio = (TextView) view.findViewById(R.id.Precio);
                            texto_precio.setTypeface(typeFace);
                            texto_precio.setText(((Lista_productos) entrada).get_precio());

                            TextView texto_unidad = (TextView) view.findViewById(R.id.Unidad);
                            texto_unidad.setTypeface(typeFace);
                            texto_unidad.setText(((Lista_productos) entrada).get_unidad());
                        }
                    });

                }



            });
            pDialog.dismiss();
        }

    }

    class AttemptAgregar extends AsyncTask<String, String, String> {

        protected String doInBackground(String... args) {
            session = new SessionManager(getApplicationContext());
            // get user data from session
            HashMap<String, String> user = session.getUserDetails();

            List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
            params.add(new BasicNameValuePair(TAG_UNIDAD, unidad_id));
            params.add(new BasicNameValuePair(TAG_PRODUCTO, names));
            params.add(new BasicNameValuePair(TAG_Id_establecimiento, user.get(SessionManager.TAG_LOCAL)));
            params.add(new BasicNameValuePair(TAG_PRECIO, precios));
            // getting JSON string from URL
            JSONObject json = jsonParser.makeHttpRequest("http://ahorrapp.hol.es/BD/agregar_producto.php", "POST", params);

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }


    }

}
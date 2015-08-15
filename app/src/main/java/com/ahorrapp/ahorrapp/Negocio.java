package com.ahorrapp.ahorrapp;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
    ArrayList<Lista_entrada> produc;
    // Clase JSONParser
    SessionManager session;
    Spinner lista;
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
    JSONArray unidad ;
    JSONArray productsp ;
    //agregar producto
    private static final String TAG_Id_establecimiento = "Id_establecimiento";


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perfil_negocio);
        unidades = new  ArrayList<HashMap<String, String>>();
        productos = new  ArrayList<HashMap<String, String>>();
        datos = new ArrayList<Combobox>();
        produc = new ArrayList<Lista_entrada>();
        name = (EditText) findViewById(R.id.editnombre);
        precio = (EditText) findViewById(R.id.editprecio);
        lista = (Spinner) findViewById(R.id.Unidades);
        new AttemptUnidad().execute();
        new AttemptProducto().execute();

        lista.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Combobox hola = (Combobox) lista.getItemAtPosition(position);
                String unidad_id = hola.get_id();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
                            TextView texto = (TextView) view.findViewById(R.id.unidad);
                            texto.setText(((Combobox) entrada).get_texto());

                            TextView texto_id = (TextView) view.findViewById(R.id.id_unidad);
                            texto_id.setText(((Combobox) entrada).get_id());

                        }
                    });

                }


            });

        }

    }


    class AttemptProducto extends AsyncTask<String, String, String> {

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

                        // creating new HashMap
                        HashMap<String, String> pro = new HashMap<String, String>();


                        // adding each child node to HashMap key => value
                        pro.put(TAG_PRECIO, precio);
                        pro.put(TAG_NOMBREP, Producto);
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
                        produc.add(new Lista_entrada(pro.get(TAG_NOMBREP), pro.get(TAG_PRECIO)));

                        cont++;
                    }
                    productos.clear();

                    ListView lista = (ListView) findViewById(R.id.listProductos);
                    lista.setAdapter(new Lista_adaptador(Negocio.this, R.layout.entrada, produc) {
                        @Override
                        public void onEntrada(Object entrada, View view) {
                            TextView texto_superior_entrada = (TextView) view.findViewById(R.id.textView_superior);
                            texto_superior_entrada.setText(((Lista_entrada) entrada).get_textoEncima());

                            TextView texto_inferior_entrada = (TextView) view.findViewById(R.id.textView_inferior);
                            texto_inferior_entrada.setText(((Lista_entrada) entrada).get_textoDebajo());

                        }
                    });

                }



            });

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

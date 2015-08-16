package com.ahorrapp.ahorrapp;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
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

public class Opciones_producto extends Activity {

    private EditText name,precio;
    String Unidad;
    ArrayList<HashMap<String,String>> unidades;
    ArrayList<Combobox> datos;
    // Clase JSONParser
    SessionManager session;
    Spinner lista;
    ListView lista_p;

    JSONParser jsonParser = new JSONParser();
    JSONParser jsonParserp = new JSONParser();
    // JSON Node names establecimiento
    private static final String TAG_SUCCESS = "success";
    //JSON Node names producto
    private static final String TAG_UNIDADES = "Unidades";
    private static final String TAG_UNIDAD = "Unidad";
    private static final String TAG_ID_UNIDAD = "Id";

    //JSON Node names producto
    private static final String TAG_NOMBREP = "Nombre_producto";
    private static final String TAG_PRECIO = "Precio";
    private static final String TAG_PRODUCTO = "Producto";
    private ProgressDialog pDialog;
    private static final String TAG_Id_establecimiento = "Establecimiento_idEstablecimiento";
    JSONArray unidad ;


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.editar_productos);

        Typeface typeFace=Typeface.createFromAsset(getAssets(),"font/rockwell condensed.ttf");


        unidades = new  ArrayList<HashMap<String, String>>();
        datos = new ArrayList<Combobox>();

        name = (EditText) findViewById(R.id.Nombre);
        precio = (EditText) findViewById(R.id.Precio);
        Bundle bundle = getIntent().getExtras();
        name.setText(bundle.getString("nombre"));
        name.setTypeface(typeFace);
        precio.setText(bundle.getString("precio"));
        precio.setTypeface(typeFace);
        TextView text1 =(TextView) findViewById(R.id.textnombreproducto);
        text1.setTypeface(typeFace);
        TextView text2 =(TextView) findViewById(R.id.textprecio);
        text2.setTypeface(typeFace);
        TextView text3 =(TextView) findViewById(R.id.textunidad);
        text3.setTypeface(typeFace);
        lista = (Spinner) findViewById(R.id.Unidades);
        lista_p = (ListView)findViewById(R.id.listProductos);
        new AttemptUnidad().execute();


        lista.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Combobox hola = (Combobox) lista.getItemAtPosition(position);
                Unidad = hola.get_id();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });


        final Button Eliminar = (Button) findViewById(R.id.btnEliminar);
        Eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AttemptEliminar().execute();

            }
        });

        final Button Modificar = (Button) findViewById(R.id.btnModificar);
        Modificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AttemptModificar().execute();

            }
        });




    }



    class AttemptModificar extends AsyncTask<String, String, String> {

        protected String doInBackground(String... args) {


            session = new SessionManager(getApplicationContext());
            // get user data from session
            HashMap<String, String> user = session.getUserDetails();

            List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
            params.add(new BasicNameValuePair(TAG_Id_establecimiento, user.get(SessionManager.TAG_LOCAL)));
            params.add(new BasicNameValuePair(TAG_NOMBREP, name.getText().toString()));
            params.add(new BasicNameValuePair(TAG_PRECIO, precio.getText().toString()));
            params.add(new BasicNameValuePair(TAG_UNIDAD, Unidad));


            // getting JSON string from URL
            JSONObject json = jsonParser.makeHttpRequest("http://ahorrapp.hol.es/BD/modificar_producto.php", "POST", params);

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

    class AttemptEliminar extends AsyncTask<String, String, String> {

        protected String doInBackground(String... args) {



            session = new SessionManager(getApplicationContext());
            // get user data from session
            HashMap<String, String> user = session.getUserDetails();

            List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
            params.add(new BasicNameValuePair("id", user.get(SessionManager.TAG_LOCAL)));
            params.add(new BasicNameValuePair("nombre", name.getText().toString()));

            JSONObject json = jsonParser.makeHttpRequest("http://ahorrapp.hol.es/BD/eliminar_producto.php", "POST", params);

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


                    lista.setAdapter(new Lista_adaptador(Opciones_producto.this, R.layout.combobox, datos) {
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



}






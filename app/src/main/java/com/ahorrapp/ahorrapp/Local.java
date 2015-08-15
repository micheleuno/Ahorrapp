package com.ahorrapp.ahorrapp;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.android.gms.common.server.converter.StringToIntConverter;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Local extends FragmentActivity {

    String Nombre;
    String Id;
    ArrayList<HashMap<String,String>> establedes;
    ArrayList<HashMap<String,String>> productos;
    ArrayList<Lista_entrada> datos;
    ListView lista;
    JSONParser jsonParser = new JSONParser();
    JSONParser jsonParserp = new JSONParser();
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCTS = "Establecimiento";
    private static final String TAG_DIRECCION = "Direccion";
    private static final String TAG_NOMBRE = "Nombre";
    private static final String TAG_ID = "IdEstablecimiento";
    private static final String TAG_NOMBREP = "Nombre";
    private static final String TAG_PRECIO = "Precio";
    private static final String TAG_PRODUCTO = "Producto";
    private ProgressDialog pDialog;
    JSONArray products ;
    JSONArray productsp ;

    class AttemptLocal extends AsyncTask<String, String, String> {

        protected String doInBackground(String... args) {
            // Building Parameters
            List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
            params.add(new BasicNameValuePair("Nombre", Nombre));
            // getting JSON string from URL
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
                        String id = c.getString(TAG_ID);
                        Id=id;
                        // creating new HashMap
                        HashMap<String, String> dir = new HashMap<String, String>();
                        // adding each child node to HashMap key => value
                        dir.put(TAG_DIRECCION, address);
                        dir.put(TAG_NOMBRE, local);
                        dir.put(TAG_ID, id);
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
                TextView nombre =(TextView) findViewById(R.id.txtNombreLocal);
                nombre.setText(Nombre);
                TextView direccion =(TextView) findViewById(R.id.txtDireccionLocal);
                direccion.setText(dir.get(TAG_DIRECCION));
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
            List<BasicNameValuePair> paramsp = new ArrayList<BasicNameValuePair>();
            paramsp.add(new BasicNameValuePair("idEstablecimiento", Id));
            JSONObject jsonp = jsonParserp.makeHttpRequest("http://ahorrapp.hol.es/BD/cargar_productos.php", "POST", paramsp);
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
            pDialog.dismiss();
           runOnUiThread(new Runnable() {
                public void run() {
                int cont=0;
                HashMap<String, String> pro;
                while(cont<productos.size()){
                    pro=productos.get(cont);
                    datos.add(new Lista_entrada(pro.get(TAG_NOMBREP), pro.get(TAG_PRECIO)));
                    cont++;
                }
                productos.clear();

                ListView lista = (ListView) findViewById(R.id.productos);
                lista.setAdapter(new Lista_adaptador(Local.this, R.layout.entrada, datos) {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.establecimiento);
        Bundle bundle = getIntent().getExtras();
        Nombre = bundle.getString("nombre");
        establedes = new  ArrayList<HashMap<String, String>>();
        productos = new  ArrayList<HashMap<String, String>>();
        datos = new ArrayList<Lista_entrada>();
        Mostrar_locales();
        final Button perfil = (Button) findViewById(R.id.btnComentarios);
        perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nuevoform = new Intent(Local.this, Comentarios.class);
                nuevoform.putExtra("id",Id);
                startActivity(nuevoform);
            }
        });
    }

    private void Mostrar_locales() {

        new AttemptLocal().execute();
        new AttemptProducto().execute();
       // Log.e("MIchele", Integer.toString(datos.size()));
    }
}

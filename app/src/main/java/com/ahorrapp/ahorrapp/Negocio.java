package com.ahorrapp.ahorrapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class Negocio extends Activity {
    // -+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
    private EditText name,precio;
    String unidad_id,names,precios;
    ArrayList<HashMap<String,String>> productos;
    ArrayList<HashMap<String,String>> unidades;
    ArrayList<Combobox> datos;
    ArrayList<Lista_productos> produc;
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
    private static final String TAG_IDPRODUCTO ="idUbicacion";
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
        unidades = new ArrayList<>();
        productos = new ArrayList<>();
        datos = new ArrayList<>();
        produc = new ArrayList<>();
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
                nuevoform.putExtra("id_producto", productos.get_id());
                finish();
                startActivity(nuevoform);
            }
        });

        final Button agregar = (Button) findViewById(R.id.btnagregar);
        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            names = name.getText().toString();
            precios = precio.getText().toString();
            if(!names.equals("")&&!precios.equals("")) {
                name.setText("");
                precio.setText("");
                new AttemptAgregar().execute();
                new AttemptProducto().execute();
                hideKeyboard();
                Alertas.mensaje_error(Negocio.this, "Se ha agregado un producto");
            }else {
                Alertas.mensaje_error(Negocio.this, "Debe llenar todos los campos");
            }
            }
        });
    }

    class AttemptUnidad extends AsyncTask<String, String, String> {

        protected String doInBackground(String... args) {
                       HashMap<String, String> params = new HashMap<>();
            params.put("IdEstablecimiento", "0" );

            JSONObject json = jsonParserp.makeHttpRequest("http://ahorrapp.hol.es/BD/cargar_unidades.php", "POST", params);
            try {
                int success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    unidad = json.getJSONArray(TAG_UNIDADES);
                    //Log.i("Testing", "produtos.length" + products.length());
                    for (int j = 0; j < unidad.length(); j++) {
                        JSONObject p = unidad.getJSONObject(j);
                        // Storing each json item in variable
                        String unidad = p.getString(TAG_UNIDAD);
                        String id = p.getString(TAG_ID_UNIDAD);
                        // creating new HashMap
                        HashMap<String, String> pro = new HashMap<>();
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


            HashMap<String, String> params = new HashMap<>();


            session = new SessionManager(getApplicationContext());
            // get user data from session
            HashMap<String, String> user = session.getUserDetails();

            params.put("idEstablecimiento", user.get(SessionManager.TAG_LOCAL));

            JSONObject jsonp = jsonParserp.makeHttpRequest("http://ahorrapp.hol.es/BD/cargar_productos.php", "POST", params);
            try {
                int success = jsonp.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    productsp = new JSONArray(new ArrayList<String>());
                    productsp = jsonp.getJSONArray(TAG_PRODUCTO);
                    produc.clear();
                    productos.clear();
                    for (int j = 0; j < productsp.length(); j++) {

                        JSONObject p = productsp.getJSONObject(j);
                        String precio = p.getString(TAG_PRECIO);
                        String Producto = p.getString(TAG_NOMBREP);
                        String Unidad =p.getString(TAG_UNIDAD);
                        String id_producto=p.getString(TAG_IDPRODUCTO);
                        HashMap<String, String> pro = new HashMap<>();
                        pro.put(TAG_PRECIO, precio);
                        pro.put(TAG_NOMBREP, Producto);
                        pro.put(TAG_UNIDAD, Unidad);
                        pro.put(TAG_IDPRODUCTO,id_producto);
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
                        produc.add(new Lista_productos(pro.get(TAG_NOMBREP), pro.get(TAG_PRECIO), pro.get(TAG_UNIDAD), pro.get(TAG_IDPRODUCTO)));
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

                            TextView idproducto = (TextView) view.findViewById(R.id.idproducto);
                            idproducto.setTypeface(typeFace);
                            idproducto.setText(((Lista_productos) entrada).get_id());
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
            HashMap<String, String> params = new HashMap<>();
            params.put(TAG_UNIDAD, unidad_id);
            params.put(TAG_PRODUCTO, names);
            params.put(TAG_Id_establecimiento, user.get(SessionManager.TAG_LOCAL));
            params.put(TAG_PRECIO, precios);
            // getting JSON string from URL

            JSONObject json = jsonParser.makeHttpRequest("http://ahorrapp.hol.es/BD/agregar_producto.php", "POST", params);
            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    return null;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    private void hideKeyboard() {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Comentarios.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    @Override
    public void onBackPressed() {
        Intent nuevoform = new Intent(Negocio.this, Perfil.class);
        finish();
        startActivity(nuevoform);
    }
}

package com.ahorrapp.ahorrapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Opciones_producto extends Activity {
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_UNIDADES = "Unidades";
    private static final String TAG_UNIDAD = "Unidad";
    private static final String TAG_ID_UNIDAD = "Id";
    private static final String TAG_NOMBREP = "Nombre_producto";
    private static final String TAG_PRECIO = "Precio";
    private static final String TAG_Id_establecimiento = "Establecimiento_idEstablecimiento";
    private  String id_prod,nombre,valor;
    private EditText name,precio,id_producto;
    String Unidad;
    ArrayList<HashMap<String,String>> unidades;
    ArrayList<Combobox> datos;
    SessionManager session;
    Spinner lista;
    ListView lista_p;
    JSONParser jsonParser = new JSONParser();
    JSONParser jsonParserp = new JSONParser();
    String names,precios;
    JSONArray unidad ;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.editar_productos);
        Typeface typeFace=Typeface.createFromAsset(getAssets(),"font/rockwell condensed.ttf"); //elegir fuente del texto
        unidades = new  ArrayList<HashMap<String, String>>();
        datos = new ArrayList<Combobox>();
        name = (EditText) findViewById(R.id.Nombre);
        precio = (EditText) findViewById(R.id.Precio);
        id_producto = (EditText) findViewById(R.id.idproducto);
        Bundle bundle = getIntent().getExtras();
        id_producto.setText(bundle.getString("id_producto"));
        id_producto.setTypeface(typeFace);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(Opciones_producto.this);
                builder.setMessage("¿Esta seguro de que quiere eliminar el producto?")
                        .setCancelable(false)
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                new AttemptEliminar().execute();
                                Alertas.mensaje_error(Opciones_producto.this, "Se ha eliminado el producto");

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
                new AttemptEliminar().execute();
            }
        });

        final Button Modificar = (Button) findViewById(R.id.btnModificar);
        Modificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                names = name.getText().toString();
                precios = precio.getText().toString();
                if(!names.equals("")&&!precios.equals("")){
                    new AttemptModificar().execute();
                    hideKeyboard();
                    Alertas.mensaje_error(Opciones_producto.this, "Se ha modificado un producto");
                }else{
                    Alertas.mensaje_error(Opciones_producto.this, "Debe llenar todos los campos");
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent nuevoform = new Intent(Opciones_producto.this, Negocio.class);
        finish();
        startActivity(nuevoform);
    }
    class AttemptModificar extends AsyncTask<String, String, String> {
        protected void onPreExecute() {
            id_prod = id_producto.getText().toString();
            nombre = name.getText().toString();
            valor = precio.getText().toString();
        }

        protected String doInBackground(String... args) {

            List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
            params.add(new BasicNameValuePair(TAG_Id_establecimiento, id_prod));
            params.add(new BasicNameValuePair(TAG_NOMBREP, nombre));
            params.add(new BasicNameValuePair(TAG_PRECIO,valor));
            params.add(new BasicNameValuePair(TAG_UNIDAD, Unidad));
            JSONObject json = jsonParser.makeHttpRequest("http://ahorrapp.hol.es/BD/modificar_producto.php", "POST", params);
            try {
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

        protected void onPreExecute() {
            id_prod = id_producto.getText().toString();
            nombre = name.getText().toString();
        }
        protected String doInBackground(String... args) {
            session = new SessionManager(getApplicationContext());
            List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
            params.add(new BasicNameValuePair("id", id_prod));
            params.add(new BasicNameValuePair("nombre", nombre));
            JSONObject json = jsonParser.makeHttpRequest("http://ahorrapp.hol.es/BD/eliminar_producto.php", "POST", params);
            try {
                int success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        return null;
        }
    }
    private int getIndex(Spinner spinner, String myString){

        int index = 0;
        for (int i=0;i<spinner.getCount();i++){
            Combobox hola = (Combobox) lista.getItemAtPosition(i);
            if ( hola.get_texto().equals(myString)){
                index = i;
            }
        }
        return index;
    }

    class AttemptUnidad extends AsyncTask<String, String, String> {

        protected String doInBackground(String... args) {
            List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
            params.add(new BasicNameValuePair("IdEstablecimiento", "0" ));
            JSONObject json = jsonParserp.makeHttpRequest("http://ahorrapp.hol.es/BD/cargar_unidades.php", "POST", params);
            try {
                int success = json.getInt(TAG_SUCCESS);
                if (success == 1) { //Se encontraror unidades
                    unidad = json.getJSONArray(TAG_UNIDADES);
                    for (int j = 0; j < unidad.length(); j++) {
                        JSONObject p = unidad.getJSONObject(j);
                        String unidad = p.getString(TAG_UNIDAD);
                        String id = p.getString(TAG_ID_UNIDAD);
                        HashMap<String, String> pro = new HashMap<String, String>();
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

        protected void onPostExecute(String result) {

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
                            Typeface typeFace = Typeface.createFromAsset(getAssets(), "font/rockwell condensed.ttf");
                            TextView texto = (TextView) view.findViewById(R.id.unidad);
                            texto.setTypeface(typeFace);
                            texto.setText(((Combobox) entrada).get_texto());
                            TextView texto_id = (TextView) view.findViewById(R.id.id_unidad);
                            texto_id.setTypeface(typeFace);
                            texto_id.setText(((Combobox) entrada).get_id());
                        }
                    });
                    Bundle bundle = getIntent().getExtras();
                    int i= ( getIndex(lista, bundle.getString("unidad")));
                    lista.setSelection(i);
                }
            });
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
}






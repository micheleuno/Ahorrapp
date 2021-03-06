package com.ahorrapp.ahorrapp;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class Agregar_producto extends AppCompatActivity {

    private EditText name,precio;
    String unidad_id,names,precios;
    ArrayList<HashMap<String,String>> unidades;
    ArrayList<Combobox> datos;
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
    private static final String TAG_PRECIO = "Precio";
    private static final String TAG_PRODUCTO = "Producto";
    private int success;
    String Id_establecimiento;
    JSONArray unidad ;

    private static final String TAG_Id_establecimiento = "Id_establecimiento";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.agregar_producto);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_local);
        myToolbar.setTitle(Html.fromHtml("<font color='#FFFFFF'>Agregar Producto</font>"));
        myToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_36dp);
        setSupportActionBar(myToolbar);
        Alertas.cambiar_status_bar(Agregar_producto.this);
        unidades = new ArrayList<>();
        datos = new ArrayList<>();
        name = (EditText) findViewById(R.id.editnombre_prod);
        name.getBackground().setColorFilter(getResources().getColor(R.color.primary), PorterDuff.Mode.SRC_ATOP);
        precio = (EditText) findViewById(R.id.editprecio_prod);
        precio.getBackground().setColorFilter(getResources().getColor(R.color.primary), PorterDuff.Mode.SRC_ATOP);
        lista = (Spinner) findViewById(R.id.Unidades_prod);
        new AttemptUnidad().execute();

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
        final Button agregar = (Button) findViewById(R.id.btnagregar_prod);
        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                names = name.getText().toString();
                precios = precio.getText().toString();
                if(Alertas.Verificar_conexion(Agregar_producto.this)&&!names.equals("")&&!precios.equals("")) { //si se rellenan todos los datos y hay internet
                    name.setText("");
                    precio.setText("");
                    new AttemptAgregar().execute();

                    hideKeyboard();
                    Alertas.mensaje_error(Agregar_producto.this, "Se ha agregado un producto");
                }else {
                    Alertas.mensaje_error(Agregar_producto.this, "Debe llenar todos los campos");
                }
            }
        });


    }
    class AttemptUnidad extends AsyncTask<String, String, String> {

        protected String doInBackground(String... args) {


            HashMap<String, String> params = new HashMap<>();
            params.put("IdEstablecimiento", "0");

            JSONObject json = jsonParserp.makeHttpRequest("http://ahorrapp.hol.es/BD/cargar_unidades.php", "POST", params);
            try {
                 success = json.getInt(TAG_SUCCESS);
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
                    if (success == 2) {
                        Alertas.mensaje_error(Agregar_producto.this, "Ha ocurrido un error con la consulta");
                    }

                    int cont = 0;
                    HashMap<String, String> pro;
                    while (cont < unidades.size()) {
                        pro = unidades.get(cont);
                        datos.add(new Combobox(pro.get(TAG_UNIDAD), pro.get(TAG_ID_UNIDAD)));
                        cont++;
                    }
                    unidades.clear();
                    lista.setAdapter(new Lista_adaptador(Agregar_producto.this, R.layout.combobox, datos) {
                        @Override
                        public void onEntrada(Object entrada, View view) {
                            TextView texto = (TextView) view.findViewById(R.id.unidad);
                            texto.setTextColor(getResources().getColor(R.color.primary));
                            texto.setTextSize(18);
                            texto.setText(((Combobox) entrada).get_texto());
                            TextView texto_id = (TextView) view.findViewById(R.id.id_unidad);
                            texto_id.setText(((Combobox) entrada).get_id());
                        }
                    });
                }
            });
        }
    }

    class AttemptAgregar extends AsyncTask<String, String, String> {

        protected String doInBackground(String... args) {
            Bundle bundle = Agregar_producto.this.getIntent().getExtras();
            Agregar_producto.this.Id_establecimiento = bundle.getString("id");
            HashMap<String, String> params = new HashMap<>();
            params.put(TAG_UNIDAD, unidad_id);
            params.put(TAG_PRODUCTO, names);
            params.put(TAG_Id_establecimiento, Id_establecimiento);
            //obtenido por sesion
            params.put(TAG_PRECIO, precios);

            JSONObject json = jsonParser.makeHttpRequest("http://ahorrapp.hol.es/BD/agregar_producto.php", "POST", params);
            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);
                if (success == 1)
                    return null;
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
    public void onBackPressed() {
        Intent nuevoform = new Intent(Agregar_producto.this, Local.class);
        finish();
        startActivity(nuevoform);
    }
    public boolean onOptionsItemSelected(MenuItem item) { //al apretar atras en el toolbar
        switch (item.getItemId()) {
            case android.R.id.home: //al apretar ir atras
                Intent nuevoform = new Intent(Agregar_producto.this, Local.class);
                finish();
                startActivity(nuevoform);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}

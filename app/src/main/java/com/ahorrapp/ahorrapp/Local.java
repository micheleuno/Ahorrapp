package com.ahorrapp.ahorrapp;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.melnykov.fab.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Local extends AppCompatActivity {

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
    private static final String TAG_IDPRODUCTO ="idUbicacion";
    private static final String TAG_ID_LOCAL_USUARIO ="Id_usuario_estab";
    private int success,success2;
    ListView lista_p;
    SessionManager session;
    JSONParser jsonParser = new JSONParser();
    JSONParser jsonParserp = new JSONParser();
    JSONArray products ;
    JSONArray productsp ;
    ArrayList<HashMap<String,String>> establedes;
    ArrayList<HashMap<String,String>> productos;
    ArrayList<Lista_productos> produc;
    String Latitud,Longitud,Nombre,id_usuario_estab;
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
                success = json.getInt(TAG_SUCCESS);
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
                         id_usuario_estab = c.getString(TAG_ID_LOCAL_USUARIO);
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
            if (success == 2) {
                Alertas.mensaje_error(Local.this, "Ha ocurrido un error con la consulta");
            }else {
                int cont = 0;
                HashMap<String, String> dir;
                while (cont < establedes.size()) {
                    dir = establedes.get(cont);
                    TextView nombre = (TextView) findViewById(R.id.txtNombreLocal);
                    nombre.setText(Nombre);
                    TextView direccion = (TextView) findViewById(R.id.txtDireccionLocal);
                    direccion.setText(dir.get(TAG_DIRECCION));
                    TextView descripcion = (TextView) findViewById(R.id.txtdescripcion);
                    descripcion.setText(dir.get(TAG_DESCRIPCCION));
                    TextView contacto = (TextView) findViewById(R.id.txtContactoLocal);
                    contacto.setText(dir.get(TAG_CONTACTO));
                    cont++;
                }
            }
            establedes.clear();
        }
    }


    class AttemptProducto extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();
            Alertas.abrir_mensaje_carga(Local.this, "Cargando");
        }

        protected String doInBackground(String... args) {

            HashMap<String, String> params = new HashMap<>();
            params.put("idEstablecimiento", Id);

            JSONObject jsonp = jsonParserp.makeHttpRequest("http://ahorrapp.hol.es/BD/cargar_productos.php", "POST", params);
            try {
                success2 = jsonp.getInt(TAG_SUCCESS);
                if (success2 == 1) {
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
                        String id_producto=p.getString(TAG_IDPRODUCTO);
                        // creating new HashMap
                        HashMap<String, String> pro = new HashMap<>();
                        // adding each child node to HashMap key => value
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

        protected void onPostExecute(String result){

            runOnUiThread(new Runnable() {
                public void run() {

                    if (success2 == 2) {
                        Alertas.mensaje_error(Local.this, "Ha ocurrido un error con la consulta");
                    }else {
                    int cont = 0;
                    HashMap<String, String> pro;
                    while (cont < productos.size()) {

                        pro = productos.get(cont);
                        produc.add(new Lista_productos(pro.get(TAG_NOMBREP), pro.get(TAG_PRECIO), pro.get(TAG_UNIDAD), pro.get(TAG_IDPRODUCTO)));
                        cont++;
                    }
                    productos.clear();

                    ListView lista = (ListView) findViewById(R.id.productos);

                    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
                        if(id_usuario_estab.equals("null")) {
                            Log.e("local", id_usuario_estab);
                            fab.attachToListView(lista);
                        }else
                        fab.hide();
                    lista.setAdapter(new Lista_adaptador(Local.this, R.layout.productos, produc) {
                        @Override
                        public void onEntrada(Object entrada, View view) {
                            TextView texto_nombre = (TextView) view.findViewById(R.id.Nombre);
                            texto_nombre.setText(((Lista_productos) entrada).get_nombre());

                            TextView texto_precio = (TextView) view.findViewById(R.id.Precio);
                            texto_precio.setText(((Lista_productos) entrada).get_precio());

                            TextView texto_unidad = (TextView) view.findViewById(R.id.Unidad);
                            texto_unidad.setText(((Lista_productos) entrada).get_unidad());

                            TextView id_producto = (TextView) view.findViewById(R.id.idproducto);
                            id_producto.setText(((Lista_productos) entrada).get_id());
                        }

                    });
                    fab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (Alertas.Verificar_conexion(Local.this)) {
                                Local.this.session = new SessionManager(Local.this.getApplicationContext());
                                if (Local.this.session.isLoggedIn()) {
                                    if (id_usuario_estab.equals("null")) { //si no tiene dueño
                                        Intent nuevoform = new Intent(Local.this, Agregar_producto.class);
                                        nuevoform.putExtra("id", Id);
                                        startActivity(nuevoform);
                                    } else {
                                        Alertas.mensaje_error(Local.this, "Este establecimiento es administrado por otro usuario");
                                    }

                                } else {
                                    Alertas.mensaje_error(Local.this, "Para agregar productos debe iniciar sesion");
                                }
                            }
                        }
                    });


                        if(id_usuario_estab.equals("null")) {
                            lista_p.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
                                    Local.this.session = new SessionManager(Local.this.getApplicationContext());
                                    if (Local.this.session.isLoggedIn()) {
                                        if (Alertas.Verificar_conexion(Local.this)) {

                                            Lista_productos productos = (Lista_productos) lista_p.getItemAtPosition(position);
                                            Intent nuevoform = new Intent(Local.this, Opciones_producto.class);
                                            nuevoform.putExtra("nombre", productos.get_nombre());
                                            nuevoform.putExtra("precio", productos.get_precio());
                                            nuevoform.putExtra("unidad", productos.get_unidad());
                                            nuevoform.putExtra("vista_anterior", "local");
                                            nuevoform.putExtra("id_producto", productos.get_id());
                                            finish();
                                            startActivity(nuevoform);

                                        }
                                    } else {
                                        Alertas.mensaje_error(Local.this, "Para editar productos debe iniciar sesion");
                                    }
                                }
                            });
                        }

                }
            }


        });
            Alertas.cerrar_mensaje_carga();
        }
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_local, menu);
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.establecimiento);
        SessionManager session;
        lista_p = (ListView)findViewById(R.id.productos);
        //toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_local);
        myToolbar.setTitle(Html.fromHtml("<font color='#FFFFFF'>Ahorrapp</font>"));
        myToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_36dp);
        setSupportActionBar(myToolbar);
        Alertas.cambiar_status_bar(Local.this);
        //toolbar
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
        if(Alertas.Verificar_conexion(Local.this))
        Mostrar_locales();
        final Button perfil = (Button) findViewById(R.id.btnComentarios);
        perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Alertas.Verificar_conexion(Local.this)) {
                    Intent nuevoform = new Intent(Local.this, Comentarios.class);
                    nuevoform.putExtra("id", Id);
                    startActivity(nuevoform);
                }
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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: //al apretar ir atras
                Intent nuevoform = new Intent(Local.this, MapsActivity.class);
                finish();
                startActivity(nuevoform);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}

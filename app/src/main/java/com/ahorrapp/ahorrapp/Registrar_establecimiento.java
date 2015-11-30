package com.ahorrapp.ahorrapp;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class Registrar_establecimiento extends AppCompatActivity {


    JSONParser jsonParser = new JSONParser();
    private static final String REGISTER_URL = "http://ahorrapp.hol.es/BD/agregar_establecimiento.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private int success;
    private  String nombre_es,direccion_es,descripcion_es,contacto_es,Latitud,Longitud,rubro_es;
    private EditText descripcion, contacto,nombre,direccion,rubro;

    class CreateEstablecimiento extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Alertas.abrir_mensaje_carga(Registrar_establecimiento.this, "Creando Estableciento...");
        }
        @Override
        protected String doInBackground(String... args) {
            try {
                HashMap<String, String> params = new HashMap<>();
                params.put("Descripcion", descripcion_es);
                params.put("Nombre", nombre_es);
                params.put("Latitud", Latitud);
                params.put("Longitud", Longitud);
                params.put("Contacto", contacto_es);
                params.put("Direccion", direccion_es);
                params.put("Rubro", rubro_es);
                Log.d("Iniciando!", "Peticion");
                JSONObject json = jsonParser.makeHttpRequest(REGISTER_URL, "POST", params);
                Log.d("Intentando registrar", json.toString());
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Log.d("Establecimiento creado!", json.toString());
                    return json.getString(TAG_MESSAGE);
                }else{
                    Log.d("Registro Fallido!", json.getString(TAG_MESSAGE));
                    return json.getString(TAG_MESSAGE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(String file_url) {

            // dismiss the dialog once product deleted
           Alertas.cerrar_mensaje_carga();
            if (file_url != null){
                Toast.makeText(Registrar_establecimiento.this, file_url, Toast.LENGTH_LONG).show();
            }
            if(success == 1) {
                Toast.makeText(Registrar_establecimiento.this, "Se ha creado un nuevo establecimiento", Toast.LENGTH_LONG).show();
                Intent nuevoform = new Intent(Registrar_establecimiento.this, MapsActivity.class);
                finish();
                startActivity(nuevoform);
            }
        }
    }
    public boolean onOptionsItemSelected(MenuItem item) { //al apretar atras en el toolbar
        switch (item.getItemId()) {
            case android.R.id.home: //al apretar ir atras
                Intent nuevoform = new Intent(Registrar_establecimiento.this, MapsActivity.class);
                finish();
                startActivity(nuevoform);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registrar_establecimiento);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_local);
        myToolbar.setTitle(Html.fromHtml("<font color='#FFFFFF'>Ahorrapp</font>"));
        myToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_36dp);
        setSupportActionBar(myToolbar);
        Alertas.cambiar_status_bar(Registrar_establecimiento.this);
        Bundle bundle = getIntent().getExtras();
        Latitud = bundle.getString("latitude");
        Longitud = bundle.getString("longitude");


        descripcion = (EditText)findViewById(R.id.txtDescripcion); //nombre de la cuenta
        descripcion.getBackground().setColorFilter(ContextCompat.getColor(Registrar_establecimiento.this, R.color.primary), PorterDuff.Mode.SRC_ATOP);
        contacto = (EditText)findViewById(R.id.txtContacto);
        contacto.getBackground().setColorFilter(ContextCompat.getColor(Registrar_establecimiento.this, R.color.primary), PorterDuff.Mode.SRC_ATOP);
        nombre = (EditText)findViewById(R.id.txtNombreEstablecimiento); //nombre de la persona
        nombre.getBackground().setColorFilter(ContextCompat.getColor(Registrar_establecimiento.this, R.color.primary), PorterDuff.Mode.SRC_ATOP);
        direccion = (EditText)findViewById(R.id.txtDireccionEstablecimiento);
        direccion.getBackground().setColorFilter(ContextCompat.getColor(Registrar_establecimiento.this, R.color.primary), PorterDuff.Mode.SRC_ATOP);
        rubro = (EditText)findViewById(R.id.txtRubro);
        rubro.getBackground().setColorFilter(ContextCompat.getColor(Registrar_establecimiento.this, R.color.primary), PorterDuff.Mode.SRC_ATOP);

        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(Double.parseDouble(Latitud) ,Double.parseDouble(Longitud) , 1);
            String address = addresses.get(0).getAddressLine(0);
            direccion.setText(address);
        } catch (IOException e) {
            e.printStackTrace();
        }

        final Button  mRegister = (Button)findViewById(R.id.btnGuardar_es);
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Alertas.Verificar_conexion(Registrar_establecimiento.this)){
                    contacto_es = contacto.getText().toString();
                    nombre_es = nombre.getText().toString();
                    descripcion_es = descripcion.getText().toString();
                    direccion_es = direccion.getText().toString();
                    rubro_es = rubro.getText().toString();
                    if (!contacto_es.equals("")&&!nombre_es.equals("")&&!descripcion_es.equals("")&&!direccion_es.equals("")&&!rubro_es.equals("")){

                        new CreateEstablecimiento().execute();
                    }else {
                        Alertas.mensaje_error(Registrar_establecimiento.this, "Debe introducir todos los campos");
                    }
                }
            }
        });
    }

}

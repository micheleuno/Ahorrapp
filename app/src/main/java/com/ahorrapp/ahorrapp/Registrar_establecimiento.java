package com.ahorrapp.ahorrapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class Registrar_establecimiento extends Activity{

    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    private static final String REGISTER_URL = "http://ahorrapp.hol.es/BD/agregar_establecimiento.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private int success;
    private  String nombre_es,direccion_es,descripcion_es,contacto_es,Latitud,Longitud;
    private EditText descripcion, contacto,nombre,direccion;

    class CreateEstablecimiento extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Registrar_establecimiento.this);
            pDialog.setMessage("Creando Establecimiento...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
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
            pDialog.dismiss();
            if (file_url != null){
                Toast.makeText(Registrar_establecimiento.this, file_url, Toast.LENGTH_LONG).show();
            }
            if(success == 1) {
                Intent nuevoform = new Intent(Registrar_establecimiento.this, MapsActivity.class);
                finish();
                startActivity(nuevoform);
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registrar_establecimiento);

        Bundle bundle = getIntent().getExtras();
        Latitud = bundle.getString("latitude");
        Longitud = bundle.getString("longitude");

        Typeface typeFace=Typeface.createFromAsset(getAssets(),"font/rockwell condensed.ttf");
        descripcion = (EditText)findViewById(R.id.txtDescripcion); //nombre de la cuenta
        descripcion.setTypeface(typeFace);
        contacto = (EditText)findViewById(R.id.txtContacto);
        contacto.setTypeface(typeFace);
        nombre = (EditText)findViewById(R.id.txtNombreEstablecimiento); //nombre de la persona
        nombre.setTypeface(typeFace);
        direccion = (EditText)findViewById(R.id.txtDireccionEstablecimiento);
        direccion.setTypeface(typeFace);


        TextView text1 =(TextView) findViewById(R.id.txtregistro_es);
        text1.setTypeface(typeFace);
        final Button  mRegister = (Button)findViewById(R.id.btnGuardar_es);
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                contacto_es = contacto.getText().toString();
                nombre_es = nombre.getText().toString();
                descripcion_es = descripcion.getText().toString();
                direccion_es = direccion.getText().toString();
                new CreateEstablecimiento().execute();

            }
        });
    }



}

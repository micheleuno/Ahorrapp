package com.ahorrapp.ahorrapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class Solicitar extends AppCompatActivity implements View.OnClickListener {


    JSONParser jsonParser = new JSONParser();
    Session session = null;
    SessionManager sessions;
    ProgressDialog pdialog = null;
    EditText reciep,nombre,rut,direccion,telefono;
    Context context = null;
    String nom,ru,dire,tele,rec,contrasena="",mail;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.solicitar_layout);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_local);
        myToolbar.setTitle(Html.fromHtml("<font color='#FFFFFF'>Darse de alta como dueño</font>"));
        myToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_36dp);
        setSupportActionBar(myToolbar);
        Alertas.cambiar_status_bar(this);
        sessions = new SessionManager(getApplicationContext());
        context = this;
        Button login = (Button) findViewById(R.id.btnSoliciud);
        HashMap<String, String> user = sessions.getUserDetails();
        reciep = (EditText) findViewById(R.id.txtemail);
        reciep.getBackground().setColorFilter(getResources().getColor(R.color.primary), PorterDuff.Mode.SRC_ATOP);
        reciep.setText(user.get("Email"));
        nombre =(EditText) findViewById(R.id.txtNombrePropietario);
        nombre.getBackground().setColorFilter(getResources().getColor(R.color.primary), PorterDuff.Mode.SRC_ATOP);
        nombre.setText(user.get("Username"));
        telefono = (EditText) findViewById(R.id.txtTelefono);
        telefono.getBackground().setColorFilter(getResources().getColor(R.color.primary), PorterDuff.Mode.SRC_ATOP);
        direccion = (EditText) findViewById(R.id.txtDEstableciemiento);
        direccion.getBackground().setColorFilter(getResources().getColor(R.color.primary), PorterDuff.Mode.SRC_ATOP);
        direccion.setText(user.get("Direccion"));
        rut = (EditText) findViewById(R.id.txtRut);
        rut.getBackground().setColorFilter(getResources().getColor(R.color.primary), PorterDuff.Mode.SRC_ATOP);

        login.setOnClickListener(this);

    }





    @Override
    public void onClick(View v) {
        contrasena = "";
        if(Alertas.Verificar_conexion(Solicitar.this)&&!reciep.getText().toString().equals("")&&!nombre.getText().toString().equals("")&&!rut.getText().toString().equals("")&&
                !direccion.getText().toString().equals("")&&!telefono.getText().toString().equals("")){
            mail=reciep.getText().toString();
            nom=nombre.getText().toString();
            ru=rut.getText().toString();
            dire=direccion.getText().toString();
            tele=telefono.getText().toString();
            new RetreiveFeedTask().execute();
            rec = reciep.getText().toString();
            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.socketFactory.port", "465");
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.port", "465");
            session = Session.getDefaultInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("ahorrapp.tds@gmail.com", "micheleuno");
                }
            });

            pdialog = ProgressDialog.show(context, "", "Enviando correo...", true);
            RetreiveFeedTask task = new RetreiveFeedTask();
            task.execute();

        }else{
            Alertas.mensaje_error(Solicitar.this,"Debe ingresar todos los datos");
        }
    }

    class RetreiveFeedTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            try{
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(mail));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("ahorrapp.tds@gmail.com"));
                message.setSubject("Solicitud Establecimiento");
                message.setContent("Yo "+ nom + " solicito el establecimiento cuyo rut y direccion es "+ ru +"   " +dire+ ". Porfavor llamar a la brevedad para confirmar solicitud "+ tele, "text/html; charset=utf-8");
                Transport.send(message);
            } catch(Exception e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(String result) {
            pdialog.dismiss();
            reciep.setText("");
            Toast.makeText(getApplicationContext(), "Se ha enviado un correo con sus datos", Toast.LENGTH_LONG).show();
        }
    }


    public void onBackPressed() {
        Intent nuevoform = new Intent(Solicitar.this, MapsActivity.class);
        finish();
        startActivity(nuevoform);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: //al apretar ir atras
                Intent nuevoform = new Intent(Solicitar.this, MapsActivity.class);
                finish();
                startActivity(nuevoform);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}

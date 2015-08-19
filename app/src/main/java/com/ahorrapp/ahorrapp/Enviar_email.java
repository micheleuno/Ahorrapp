package com.ahorrapp.ahorrapp;

        import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.List;
        import java.util.Properties;

        import javax.mail.Address;
        import javax.mail.Authenticator;
        import javax.mail.Message;
        import javax.mail.MessagingException;
        import javax.mail.PasswordAuthentication;
        import javax.mail.Session;
        import javax.mail.Transport;
        import javax.mail.internet.InternetAddress;
        import javax.mail.internet.MimeMessage;

        import android.app.Activity;
        import android.app.ProgressDialog;
        import android.content.Context;
        import android.graphics.Typeface;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.view.View.OnClickListener;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ListView;
        import android.widget.TextView;
        import android.widget.Toast;

        import org.apache.http.message.BasicNameValuePair;
        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

/**
 * Created by miche on 19-08-2015.
 */
public class Enviar_email extends Activity implements OnClickListener{

    private static final String LOGIN_URL = "http://ahorrapp.hol.es/BD/login.php";
    private static final String TAG_EMAIL = "Email_usuario";
    private static final String TAG_SUCCESS = "success";
    JSONArray usuario ;
    JSONParser jsonParser = new JSONParser();
    Session session = null;
    ProgressDialog pdialog = null;
    Context context = null;
    EditText reciep, sub, msg;
    String rec, subject, textMessage,contrasena,user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enviar_email);

        context = this;

        Button login = (Button) findViewById(R.id.btn_submit);
        reciep = (EditText) findViewById(R.id.et_to);

        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        new Attemptusuario().execute();

    }

    class RetreiveFeedTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            try{
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress("ahorrapp.tds@gmail.com"));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(rec));
                message.setSubject("Recuperación de contraseña");
                message.setContent("Su contraseña es:  "+contrasena +"Y su usuario es: "+ user, "text/html; charset=utf-8");
                Transport.send(message);
            } catch(MessagingException e) {
                e.printStackTrace();
            } catch(Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            pdialog.dismiss();
            reciep.setText("");
            Toast.makeText(getApplicationContext(), "Se le ha enviado un correo con sus datos", Toast.LENGTH_LONG).show();
        }
    }

    class Attemptusuario extends AsyncTask<String, String, String> {

        protected String doInBackground(String... args) {
            List<BasicNameValuePair> paramsp = new ArrayList<BasicNameValuePair>();
            paramsp.add(new BasicNameValuePair("Email_usuario", reciep.getText().toString()));
            JSONObject jsonp = jsonParser.makeHttpRequest("http://ahorrapp.hol.es/BD/cargar_contrasena.php", "POST", paramsp);
            try {
                int success = jsonp.getInt(TAG_SUCCESS);
                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    usuario = jsonp.getJSONArray("usuario");
                    // looping through All Products
                    for (int j = 0; j < usuario.length(); j++) {
                        JSONObject p = usuario.getJSONObject(j);
                        // Storing each json item in variable
                         contrasena = p.getString("password");
                         user = p.getString("username");
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String result){
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

        }
    }
}
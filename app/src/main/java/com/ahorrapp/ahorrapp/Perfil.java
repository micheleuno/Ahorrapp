package com.ahorrapp.ahorrapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;


public class Perfil extends AppCompatActivity {
    private static int TAKE_PICTURE = 1;
    private static int SELECT_PICTURE = 2;
    private String name = "";
    SessionManager session;
    ImageView img;
    Button btn;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perfil);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_local);
        myToolbar.setTitle(Html.fromHtml("<font color='#FFFFFF'>Ahorrapp</font>"));
        myToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_36dp);
        setSupportActionBar(myToolbar);
        Alertas.cambiar_status_bar(Perfil.this);
        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        String name = user.get(SessionManager.TAG_NOMBRE);
        String nombre_pila = user.get(SessionManager.TAG_N_USUARIO);
        String direccion_usuario = user.get(SessionManager.TAG_DIRECCION);
        String email_usuario = user.get(SessionManager.TAG_EMAIL);

        TextView nombre = (TextView) findViewById(R.id.txtNombrePersona);

        nombre.setText(name);

        TextView direccion = (TextView) findViewById(R.id.txtDireccionPersona);
        direccion.setText(direccion_usuario);

        TextView email = (TextView) findViewById(R.id.txtemailpersona);
        email.setText(email_usuario);


        TextView username = (TextView) findViewById(R.id.txtEmail);
        username.setText(nombre_pila);

        img = (ImageView)findViewById(R.id.foto_perfil);
        btn = (Button)findViewById(R.id.tomar_foto);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 01);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bmp = (Bitmap)data.getExtras().get("data");
       
        Bitmap circleBitmap = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), Bitmap.Config.ARGB_8888);

        BitmapShader shader = new BitmapShader(bmp,  Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setShader(shader);
        paint.setAntiAlias(true);
        Canvas c = new Canvas(circleBitmap);
        c.drawCircle(bmp.getWidth()/2, bmp.getHeight()/2, bmp.getWidth()/2, paint);

        img.setImageBitmap(circleBitmap);

    }





    @Override
    public void onBackPressed() {
        Intent nuevoform = new Intent(Perfil.this, MapsActivity.class);
        finish();
        startActivity(nuevoform);
    }
    public boolean onOptionsItemSelected(MenuItem item) { //al apretar atras en el toolbar
        switch (item.getItemId()) {
            case android.R.id.home: //al apretar ir atras
                Intent nuevoform = new Intent(Perfil.this, MapsActivity.class);
                finish();
                startActivity(nuevoform);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

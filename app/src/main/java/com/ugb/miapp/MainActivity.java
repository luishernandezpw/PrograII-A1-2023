package com.ugb.miapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    DB db_agenda;
    String accion="nuevo";
    String id="";
    Button btn;
    TextView temp;
    FloatingActionButton fab;
    ImageView imgFotoAmigo;
    Intent tomarFotoAmigoIntent;
    String urlCompletaImg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            btn = findViewById(R.id.btnGuardar);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    guardar_agenda();
                }
            });
            fab = findViewById(R.id.fabRegresarListaAmigos);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    regresarListaAmigos();
                }
            });
            imgFotoAmigo = findViewById(R.id.imgFotoAmigo);
            imgFotoAmigo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tomarFotoAmigo();
                }
            });
        }catch (Exception e){
            Toast.makeText(this, "Error al cargar: "+ e.getMessage(), Toast.LENGTH_LONG).show();
        }
        mostrar_datos_amigos();
    }
    private void tomarFotoAmigo(){
        try{
            tomarFotoAmigoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if( tomarFotoAmigoIntent.resolveActivity(getPackageManager())!=null ){
                File fotoAmigo =null;
                fotoAmigo = crearImagenAmigo();
                if(fotoAmigo!=null){
                    Uri urlFotoAmigo = FileProvider.getUriForFile(MainActivity.this, "com.ugb.miapp.fileprovider", fotoAmigo);
                    tomarFotoAmigoIntent.putExtra(MediaStore.EXTRA_OUTPUT, urlCompletaImg);
                    startActivityForResult(tomarFotoAmigoIntent, 1);
                }else{
                    Toast.makeText(this, "Error no pude crear la foto del amigo...", Toast.LENGTH_SHORT).show();
                }
            }
        }catch (Exception ex){
            Toast.makeText(this, "Error al tomar Foto: "+ ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    private File crearImagenAmigo() throws Exception{
        String fechaHoraMs = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = "imagen_"+ fechaHoraMs +"_";
        File dirAlmacenamiento = getExternalFilesDir(Environment.DIRECTORY_DCIM);
        if( dirAlmacenamiento.exists()==false ){
            dirAlmacenamiento.mkdirs();
        }
        File image = File.createTempFile(fileName, ".jpg", dirAlmacenamiento);
        urlCompletaImg = image.getAbsolutePath();
        return image;
    }
    void mostrar_datos_amigos(){
        try {
            Bundle parametros = getIntent().getExtras();
            accion = parametros.getString("accion");
            if (accion.equals("modificar")) {
                String amigos[] = parametros.getStringArray("amigos");
                id = amigos[0];

                temp = findViewById(R.id.txtnombre);
                temp.setText(amigos[1]);

                temp = findViewById(R.id.txtdireccion);
                temp.setText(amigos[2]);

                temp = findViewById(R.id.txtTelefono);
                temp.setText(amigos[3]);

                temp = findViewById(R.id.txtemail);
                temp.setText(amigos[4]);
            }
        }catch (Exception e){
            Toast.makeText(this, "Error al mostrar datos: "+ e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    void guardar_agenda(){
        try {
            temp = (TextView) findViewById(R.id.txtnombre);
            String nombre = temp.getText().toString();

            temp = (TextView) findViewById(R.id.txtdireccion);
            String direccion = temp.getText().toString();

            temp = (TextView) findViewById(R.id.txtTelefono);
            String telefono = temp.getText().toString();

            temp = (TextView) findViewById(R.id.txtemail);
            String email = temp.getText().toString();

            db_agenda = new DB(MainActivity.this, "",null,1);
            String result = db_agenda.administrar_agenda(id, nombre, direccion, telefono, email, accion);
            String msg = result;
            if( result.equals("ok") ){
                msg = "Registro guardado con exito";
                regresarListaAmigos();
            }
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        }catch (Exception e){
            Toast.makeText(this, "Error en guardar agenda: "+ e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    void regresarListaAmigos(){
        Intent iListaAmigos = new Intent(MainActivity.this, lista_amigos.class);
        startActivity(iListaAmigos);
    }
}
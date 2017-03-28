package com.micompany.culturapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class Anadir2 extends AppCompatActivity {

    public static Anadir2 anadir2;

    private ImageButton flecha;
    private FloatingActionButton alante;
    private Button boton;
    private ImageView imagen;
    private TextView nombre;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private String mCurrentPhotoPath;
    private Bitmap imageBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anadir2);

        anadir2 = this;

        //Obtener latitud y longitud
        Intent intent = getIntent();
        final String latitud = intent.getStringExtra("LATITUD");
        final String longitud = intent.getStringExtra("LONGITUD");

        //Añadir accion: Finalizar actividad al ir para atras
        flecha = (ImageButton) findViewById(R.id.flecha);
        flecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                killActivity();
            }
        });

        //Añadir accion: Abrir camera al seleccionar cambiar foto
        boton = (Button) findViewById(R.id.botonimagen);
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    //TODO seleccionar de galeria???????
                    FromCamera();
                } catch (IOException e) {
                    //
                }
            }
        });

        //Añadir accion: Ir a añadir3 al pulsar continuar
        nombre = (TextView) findViewById(R.id.nombre);
        imagen = (ImageView) findViewById(R.id.imagen);
        alante = (FloatingActionButton) findViewById(R.id.forward);
        alante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Anadir2.this, Anadir3.class);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                intent.putExtra("NOMBRE", nombre.getText().toString());
                intent.putExtra("LATITUD", latitud);
                intent.putExtra("LONGITUD", longitud);
                intent.putExtra("IMAGEN", imageBitmap);
                startActivity(intent);
            }
        });

    }

    //funcion finalizar actividad
    private void killActivity() {
        finish();
    }

    //Funcion abrir camara
    private void FromCamera() throws IOException {
        File file;
        file = File.createTempFile("temporalfoto", null, this.getCacheDir());
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.parse(file.getPath()));

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            imagen.setImageBitmap(imageBitmap);
        }
    }
}

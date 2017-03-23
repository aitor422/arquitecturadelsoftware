package com.micompany.culturapp;

import android.content.Intent;

import android.graphics.Bitmap;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Anadir2 extends AppCompatActivity {
    private ImageButton flecha;
    private FloatingActionButton alante;
    private Button boton;
    private ImageView imagen;
    private TextView nombre;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private String mCurrentPhotoPath;
    private Bitmap imageBitmap;



    public static Anadir2 anadir2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        anadir2 = this;
        setContentView(R.layout.activity_anadir2);
        Intent intent = getIntent();
        final String latitud = intent.getStringExtra("LATITUD");
        final String longitud = intent.getStringExtra("LONGITUD");

        nombre = (TextView) findViewById(R.id.nombre);
        flecha = (ImageButton) findViewById(R.id.flecha);
        boton = (Button) findViewById(R.id.botonimagen);
        imagen = (ImageView) findViewById(R.id.imagen);

        alante = (FloatingActionButton) findViewById(R.id.forward);
        flecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                killActivity();
            }
        });
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    FromCamera();
                } catch (IOException e) {
                    //
                }
            }
        });

        alante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Anadir2.this, Anadir3.class);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                intent.putExtra("NOMBRE", nombre.getText());
                intent.putExtra("LATITUD", latitud);
                intent.putExtra("LONGITUD", longitud);
                intent.putExtra("IMAGEN", imageBitmap);
                startActivity(intent);
            }
        });

    }

    private void killActivity() {
        finish();
    }

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

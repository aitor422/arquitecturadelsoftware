package com.micompany.culturapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Contestar extends AppCompatActivity {

    private ImageButton flecha;
    List<RadioButton> botones;
    int opc_correcta;
    RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contestar);

        Intent intent = getIntent();

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        opc_correcta = intent.getIntExtra("OPC_CORRECTA", -1);

        flecha = (ImageButton) findViewById(R.id.flecha);
        flecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                killActivity();
            }
        });
        FloatingActionButton okay = (FloatingActionButton) findViewById(R.id.okay);
        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String FILENAME = "puntuacion";
                File fichero = getFileStreamPath(FILENAME);

                try {
                    BufferedReader in = new BufferedReader(new FileReader(fichero));
                    String score = in.readLine();
                    String[] tok = score.split("-");
                    in.close();
                    double aciertosd = Double.parseDouble(tok[0]);
                    double intentosd = Double.parseDouble(tok[1]);
                    intentosd++;
                    int opc_elegida = radioGroup.indexOfChild(findViewById(radioGroup.getCheckedRadioButtonId()));
                    if (opc_correcta==opc_elegida){
                        aciertosd++;
                        Toast.makeText(
                                getBaseContext(),
                                "Opción correcta!",
                                Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(
                                getBaseContext(),
                                "Opción incorrecta!",
                                Toast.LENGTH_SHORT).show();
                    }

                    FileOutputStream fos = new FileOutputStream(fichero);
                    fos.write(((int)aciertosd + "-" + (int)intentosd).getBytes());
                    fos.close();
                }

                catch (IOException e) {
                    Toast.makeText(
                            getBaseContext(),
                            "IOException" + e.getLocalizedMessage(),
                            Toast.LENGTH_SHORT).show();
                }

                killActivity();
            }
        });



        botones = new ArrayList<RadioButton>();
        botones.add( (RadioButton) findViewById(R.id.radioButton0));
        botones.add( (RadioButton) findViewById(R.id.radioButton1));
        botones.add( (RadioButton) findViewById(R.id.radioButton2));
        botones.add( (RadioButton) findViewById(R.id.radioButton3));

        TextView pregunta = (TextView) findViewById(R.id.pregunta);
        pregunta.setText(intent.getStringExtra("PREGUNTA"));
        TextView nombre = (TextView) findViewById(R.id.nombre);
        nombre.setText(intent.getStringExtra("NOMBRE"));
        ImageView imageView = (ImageView) findViewById(R.id.imagen);
        byte [] encodeByte = Base64.decode(intent.getStringExtra("IMAGEN"), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        imageView.setImageBitmap(bitmap);
        double latitud = intent.getDoubleExtra("LATITUD", -1);
        double longitud = intent.getDoubleExtra("LONGITUD", -1);

        ParseQuery<Opcion> query = ParseQuery.getQuery(Opcion.class)
                .whereEqualTo("longitud", longitud).whereEqualTo("latitud", latitud).orderByAscending("num");
        query.findInBackground(new FindCallback<Opcion>() {
            public void done(List<Opcion> objects, ParseException e) {
                    if (e == null){
                        if (objects.size()>2) botones.get(2).setVisibility(View.VISIBLE);
                        if (objects.size()>3) botones.get(3).setVisibility(View.VISIBLE);
                        for (int i=0; i<objects.size(); i++){
                            botones.get(i).setText(objects.get(i).getTexto());
                        }
                    }else{
                        Toast.makeText(
                                getBaseContext(),
                                "Error en la conexión",
                                Toast.LENGTH_SHORT).show();
                        killActivity();
                    }
            }
        });


    }
    //funcion finalizar actividad
    private void killActivity() {
        finish();
    }
}

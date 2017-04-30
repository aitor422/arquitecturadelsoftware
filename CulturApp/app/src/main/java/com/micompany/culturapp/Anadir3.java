package com.micompany.culturapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class Anadir3 extends AppCompatActivity {

    private ImageButton flecha, mas, menos;
    private FloatingActionButton okay;
    List<EditText> opciones;
    List<RadioButton> botones;
    private RadioGroup radioGroup;
    int numOpciones;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anadir3);

        //Añadir accion: Finalizar actividadd al pinchar la flecha
        flecha = (ImageButton) findViewById(R.id.flecha);
        flecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                killActivity();
            }
        });

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        opciones = new ArrayList<EditText>();
        opciones.add( (EditText) findViewById(R.id.opcion0));
        opciones.add( (EditText) findViewById(R.id.opcion1));
        opciones.add( (EditText) findViewById(R.id.opcion2));
        opciones.add( (EditText) findViewById(R.id.opcion3));
        botones = new ArrayList<RadioButton>();
        botones.add( (RadioButton) findViewById(R.id.radioButton0));
        botones.add( (RadioButton) findViewById(R.id.radioButton1));
        botones.add( (RadioButton) findViewById(R.id.radioButton2));
        botones.add( (RadioButton) findViewById(R.id.radioButton3));
        numOpciones=2;

        //Añadir accion: Añadir opcion al darle a mas
        mas = (ImageButton) findViewById(R.id.mas);
        mas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (numOpciones==2){
                    botones.get(2).setVisibility(View.VISIBLE);
                    opciones.get(2).setVisibility(View.VISIBLE);
                    numOpciones++;
                }else if (numOpciones==3){
                    botones.get(3).setVisibility(View.VISIBLE);
                    opciones.get(3).setVisibility(View.VISIBLE);
                    numOpciones++;
                }
            }
        });
        //Añadir accion: Eliminar opcion al darle a menos
        menos = (ImageButton) findViewById(R.id.menos);
        menos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (numOpciones==3){
                    botones.get(2).setVisibility(View.GONE);
                    opciones.get(2).setVisibility(View.GONE);
                    if (botones.get(2).isChecked()){
                        radioGroup.check(botones.get(1).getId());
                    }
                    numOpciones--;
                }else if (numOpciones==4){
                    botones.get(3).setVisibility(View.GONE);
                    opciones.get(3).setVisibility(View.GONE);
                    if (botones.get(3).isChecked()){
                        radioGroup.check(botones.get(2).getId());
                    }
                    numOpciones--;
                }
            }
        });

        //Añadir accion: Insertar bd y finalizar al pinchar okay
        okay = (FloatingActionButton) findViewById(R.id.okay);
        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(
                        getBaseContext(),
                        "Añadiendo marcador...",
                        Toast.LENGTH_SHORT).show();

                //Obtener datos marcador de la actividad anterior
                Intent intent = getIntent();
                final double latitud = Anadir.anadir1.latitud;
                final double longitud = Anadir.anadir1.longitud;
                final String nombre = Anadir2.anadir2.nombre.getText().toString();
                final Bitmap bitmap = ((BitmapDrawable) Anadir2.anadir2.imagen.getDrawable()).getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                String imagen = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
                //Obtener datos de la actividad actual
                EditText epregunta = (EditText) findViewById(R.id.pregunta);
                final String pregunta = epregunta.getText().toString();
                String[] textos = new String[numOpciones];
                for (int i=0; i<numOpciones; i++){
                    textos[i]=opciones.get(i).getText().toString();
                }

                int opccorrecta  = radioGroup.indexOfChild(findViewById(radioGroup.getCheckedRadioButtonId()));


                //insertar en la base de dar
                Marcador marcador = new Marcador();
                marcador.setup(latitud, longitud, nombre, imagen, pregunta, opccorrecta, numOpciones);

                marcador.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Toast.makeText(
                                    getBaseContext(),
                                    "Marcador añadido",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(
                                    getBaseContext(),
                                    "Error al añadir marcador: "+ e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                });


                //Añadir opciones
                for (int i=0; i<numOpciones; i++) {
                    //insertar en la base de datos
                    Opcion opcion = new Opcion();
                    opcion.setup(latitud, longitud, i, opciones.get(i).getText().toString());

                    opcion.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                            } else {
                                Toast.makeText(
                                        getBaseContext(),
                                        "Error al añadir Opción: " + e.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }

                    });
                }

                //Finalizar añadir marcador
                Anadir.anadir1.finish();
                Anadir2.anadir2.finish();
                killActivity();
            }
        });
    }

    //funcion finalizar actividad
    private void killActivity() {
        finish();
    }


}

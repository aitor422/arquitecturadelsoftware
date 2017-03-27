package com.micompany.culturapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

public class Anadir3 extends AppCompatActivity {

    private ImageButton flecha, mas, menos;
    private FloatingActionButton okay;
    List<EditText> opciones;
    List<RadioButton> botones;
    private RadioGroup radioGroup;
    private int numOpciones;
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

                //Obtener datos marcador de la actividad anterior
                Intent intent = getIntent();
                final String latitud = intent.getStringExtra("LATITUD");
                final String longitud = intent.getStringExtra("LONGITUD");
                final String nombre = intent.getStringExtra("NOMBRE");
                //final Bitmap bitMap = (Bitmap) intent.getParcelableArrayExtra("IMAGEN")[0];
                //final int resp_correcta = radioGroup.getCheckedRadioButtonId();''


                //TODO insertar en la base de datos
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

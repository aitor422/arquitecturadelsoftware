package com.micompany.culturapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;

import java.util.ArrayList;
import java.util.List;

public class Contestar extends AppCompatActivity {
    private ImageButton flecha;
    List<RadioButton> botones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contestar);
        flecha = (ImageButton) findViewById(R.id.flecha);
        flecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                killActivity();
            }
        });

        botones = new ArrayList<RadioButton>();
        botones.add( (RadioButton) findViewById(R.id.radioButton0));
        botones.add( (RadioButton) findViewById(R.id.radioButton1));
        botones.add( (RadioButton) findViewById(R.id.radioButton2));
        botones.add( (RadioButton) findViewById(R.id.radioButton3));

/*
        Intent intent = getIntent();
        TextView pregunta = (TextView) findViewById(R.id.pregunta);
        pregunta.setText(intent.getStringExtra("PREGUNTA"));
        TextView nombre = (TextView) findViewById(R.id.nombre);
        nombre.setText(intent.getStringExtra("NOMBRE"));
        ImageView imageView = (ImageView) findViewById(R.id.imagen);
        imageView.setImageBitmap( (Bitmap) intent.getParcelableExtra("IMAGEN"));

        double latitud = intent.getDoubleExtra("LATITUD", -1);
        double longitud = intent.getDoubleExtra("LONGITUD", -1);
/*
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Opcion")
                .whereEqualTo("longitud", longitud).whereEqualTo("latitud", latitud);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                    if (e == null){
                        if (objects.size()>2) botones.get(2).setVisibility(View.VISIBLE);
                        if (objects.size()>3) botones.get(3).setVisibility(View.VISIBLE);
                        for (int i=0; i<objects.size(); i++){
                            botones.get(i).setText((String)objects.get(i).get("texto"));
                        }
                    }else{


                    }
            }
        });*/
    }
    //funcion finalizar actividad
    private void killActivity() {
        finish();
    }
}

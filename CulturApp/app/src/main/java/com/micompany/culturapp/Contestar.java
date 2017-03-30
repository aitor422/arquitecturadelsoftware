package com.micompany.culturapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class Contestar extends AppCompatActivity {
    private ImageButton flecha;

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

        Intent intent = getIntent();
        TextView pregunta = (TextView) findViewById(R.id.pregunta);
        Toast.makeText(
                getBaseContext(),
                intent.getStringExtra("PREGUNTA"),
                Toast.LENGTH_SHORT).show();
        pregunta.setText(intent.getStringExtra("PREGUNTA"));

    }

    //funcion finalizar actividad
    private void killActivity() {
        finish();
    }
}

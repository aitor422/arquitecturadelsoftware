package com.micompany.culturapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class Puntuacion extends AppCompatActivity {

    ImageButton flecha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        byte[] bytes;
        String score;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puntuacion);

        flecha = (ImageButton) findViewById(R.id.flecha);

        flecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                killActivity();
            }
        });
        String FILENAME = "puntuacion";
        File fichero = getFileStreamPath(FILENAME);
        if (fichero.exists()) {
            try {
                BufferedReader in = new BufferedReader(new FileReader(fichero));
                score = in.readLine();
                String[] tok = score.split("-");
                TextView aciertos = (TextView) findViewById(R.id.aciertos);
                aciertos.setText(tok[0]);
                TextView intentos = (TextView) findViewById(R.id.intentos);
                intentos.setText(tok[1]);
                in.close();
            }
            catch (IOException e) {

            }
        }
        else {
            try {
                String string = "0-0";
                FileOutputStream fos = new FileOutputStream(fichero);
                fos.write(string.getBytes());
                fos.close();
            }
            catch (IOException e) {
            }
        }
    }
    private void killActivity() {
        finish();
    }
}

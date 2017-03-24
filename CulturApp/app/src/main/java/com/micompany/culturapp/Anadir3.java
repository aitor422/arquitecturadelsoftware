package com.micompany.culturapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

public class Anadir3 extends AppCompatActivity {
    private ImageButton flecha;
    private FloatingActionButton okay;

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

        //Añadir accion: Insertar bd y finalizar al pinchar okay
        okay = (FloatingActionButton) findViewById(R.id.okay);
        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

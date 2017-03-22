package com.micompany.culturapp;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

public class Anadir extends AppCompatActivity {
    private ImageButton flecha;
    private FloatingActionButton alante;
    public static Anadir anadir1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        anadir1 = this;
        setContentView(R.layout.activity_anadir1);
        flecha = (ImageButton) findViewById(R.id.flecha);
        alante = (FloatingActionButton) findViewById(R.id.forward);
        flecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                killActivity();
            }
        });

        alante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Anadir.this, Anadir2.class);
                startActivity(intent);
            }
        });

    }

    private void killActivity() {
        finish();
    }
}

package com.micompany.culturapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

public class Anadir2 extends AppCompatActivity {
    private ImageButton flecha;
    private FloatingActionButton alante;
    public static Anadir2 anadir2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        anadir2 = this;
        setContentView(R.layout.activity_anadir2);
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
                Intent intent = new Intent(Anadir2.this, Anadir3.class);
                startActivity(intent);
            }
        });

    }

    private void killActivity() {
        finish();
    }
}

package com.micompany.culturapp;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Ajustes extends AppCompatActivity {


    ImageButton flecha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajustes);

        flecha = (ImageButton) findViewById(R.id.flecha);

        flecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                killActivity();
            }
        });

        ListView lv = (ListView) findViewById(R.id.AjustesList);

        List<String> lista = new ArrayList<String>();
        lista.add("Reiniciar Puntuación");
        lista.add("Donar");
        lista.add("Acerca de");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                lista );
        lv.setAdapter(arrayAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItemFromMenu(position);
            }
        });
    }

    private void killActivity() {
        finish();
    }
    private void selectItemFromMenu(int position) {
        switch (position) {
            case 0:
                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                builder1.setMessage("¿Estás seguro de querer reiniciar la puntuación?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Sí",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                try {
                                    String FILENAME = "puntuacion";
                                    File fichero = getFileStreamPath(FILENAME);
                                    String string = "0-0";
                                    FileOutputStream fos = new FileOutputStream(fichero);
                                    fos.write(string.getBytes());
                                    fos.close();
                                }
                                catch (IOException e) {
                                }
                                dialog.cancel();
                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
                break;
            case 1:
                break;
            default:
                break;
        }
    }

}

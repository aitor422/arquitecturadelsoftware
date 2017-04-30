
package com.micompany.culturapp;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by irati on 30/04/17.
 */

@ParseClassName("Marcador")
public class Marcador extends ParseObject {

    public Marcador(){
        super();
    }

    public void setup(double latitud, double longitud, String nombre, String imagen, String pregunta, int opccorrecta, int numOpciones) {
        put("latitud",latitud);
        put("longitud",longitud);
        put("nombre",nombre);
        put("imagen",imagen);
        put("pregunta",pregunta);
        put("opc_correcta",opccorrecta);
        put("num_opciones",numOpciones);
    }
    public double getLatitud(){
        return (double) get("latitud");
    }
    public double getLongitud(){
        return (double) get("longitud");
    }
    public String getNombre() {
        return (String) get("nombre");
    }
    public String getImagen() {
        return (String) get("imagen");
    }
    public String getPregunta() {
        return (String) get("pregunta");
    }
    public int getOpccorrecta() {
        return (int) get("opc_correcta");
    }
    public int getNumopciones() {
        return (int) get("num_opciones");
    }
}



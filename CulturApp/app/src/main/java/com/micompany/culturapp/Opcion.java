
package com.micompany.culturapp;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by irati on 30/04/17.
 */

@ParseClassName("Opcion")
public class Opcion extends ParseObject {

    public Opcion() {
        super();
    }

    public void setup(double latitud, double longitud, int num, String texto) {
        put("latitud", latitud);
        put("longitud", longitud);
        put("num", num);
        put("texto", texto);
    }
    public double getLatitud(){
        return (double) get("latitud");
    }
    public double getLongitud(){
        return (double) get("longitud");
    }
    public int getNum(){
        return (int) get("num");
    }
    public String getTexto() {
        return (String) get("texto");
    }
}
package com.example.route4you.Model;

import java.lang.reflect.Array;

public class Ruta {


        private String uid;
        private String numRuta;
        private String inicio;
        private String llegada;
        private String controles;
        private String imagen;

        public Ruta() {
        }

        public String getUid() {
            return uid;
        }

    public String getNumRuta() {
        return numRuta;
    }

    public void setNumRuta(String numRuta) {
        this.numRuta = numRuta;
    }

    public String getInicio() {
        return inicio;
    }

    public void setInicio(String inicio) {
        this.inicio = inicio;
    }

    public String getLlegada() {
        return llegada;
    }

    public void setLlegada(String llegada) {
        this.llegada = llegada;
    }

    public String getControles() {
        return controles;
    }

    public void setControles(String controles) {
        this.controles = controles;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
    @Override
    public String toString() {
        return inicio+"-"+llegada;
    }

}

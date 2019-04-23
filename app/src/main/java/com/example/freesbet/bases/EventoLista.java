package com.example.freesbet.bases;

import java.util.ArrayList;

public class EventoLista {
    String id;
    String nombre;
    String zona;
    String urlImagen;
    String fecha;
    int numeroJugadores;
    /*String tipo;
    ArrayList <String> competidores;
    String cuota1;
    String cuota2;*/



    public EventoLista(String id, String nombre, String zona, String urlImagen, String fecha, int numeroJugadores) {
        this.id = id;
        this.nombre = nombre;
        this.zona = zona;
        this.urlImagen = urlImagen;
        this.fecha = fecha;
        this.numeroJugadores = numeroJugadores;
    }
}

package com.example.freesbet.bases;

public class Evento {
    int id;
    String batalla;
    String nombre;
    String urlImagen;
    String fecha;
    int numeroJugadores;

    public Evento(int id, String batalla, String nombre, String urlImagen, String fecha, int numeroJugadores) {
        this.id = id;
        this.batalla = batalla;
        this.nombre = nombre;
        this.urlImagen = urlImagen;
        this.fecha = fecha;
        this.numeroJugadores = numeroJugadores;
    }
}

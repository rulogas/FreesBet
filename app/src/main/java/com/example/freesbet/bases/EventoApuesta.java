package com.example.freesbet.bases;

import java.util.ArrayList;

public class EventoApuesta {

    String id;
    String nombre;
    String zona;
    String fecha;
    int numeroJugadores;
    String tipo;
    ArrayList<String> competidores;
    double cuota1;
    double cuota2;
    int boteAcumulado;

    public EventoApuesta(String id, String nombre, String zona, String fecha, int numeroJugadores, String tipo, ArrayList<String> competidores, double cuota1, double cuota2, int boteAcumulado) {
        this.id = id;
        this.nombre = nombre;
        this.zona = zona;
        this.fecha = fecha;
        this.numeroJugadores = numeroJugadores;
        this.tipo = tipo;
        this.competidores = competidores;
        this.cuota1 = cuota1;
        this.cuota2 = cuota2;
        this.boteAcumulado = boteAcumulado;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getZona() {
        return zona;
    }

    public String getFecha() {
        return fecha;
    }

    public int getNumeroJugadores() {
        return numeroJugadores;
    }

    public String getTipo() {
        return tipo;
    }

    public ArrayList<String> getCompetidores() {
        return competidores;
    }

    public double getCuota1() {
        return cuota1;
    }

    public double getCuota2() {
        return cuota2;
    }

    public int getBoteAcumulado() {
        return boteAcumulado;
    }
}

package com.example.freesbet.bases;

public class Actividad {

    int id;
    String nombre;
    String resultado;
    String tipo;

    public Actividad(int id, String nombre, String resultado, String tipo) {
        this.id = id;
        this.nombre = nombre;
        this.resultado = resultado;
        this.tipo = tipo;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getResultado() {
        return resultado;
    }

    public String getTipo() {
        return tipo;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}

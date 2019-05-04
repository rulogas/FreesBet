package com.example.freesbet.bases;

import java.util.Date;

public class Actividad {

    String id;
    String nombre;
    String resultado;
    String tipo;
    Date fecha;

    public Actividad(String id, String nombre, String resultado, String tipo, Date fecha) {
        this.id = id;
        this.nombre = nombre;
        this.resultado = resultado;
        this.tipo = tipo;
        this.fecha = fecha;
    }

    public String getId() {
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

    public Date getFecha() {
        return fecha;
    }

    public void setId(String id) {
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

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}

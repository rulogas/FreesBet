package com.example.freesbet.bases;

public class Premio {
    private String id;
    private String nombre;
    private String urlImagen;
    private int costeCoins;


    public Premio(String id, String nombre, String urlImagen, int costeCoins) {
        this.id = id;
        this.nombre = nombre;
        this.urlImagen = urlImagen;
        this.costeCoins = costeCoins;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getUrlImagen() {
        return urlImagen;
    }

    public int getCosteCoins() {
        return costeCoins;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }

    public void setCosteCoins(int costeCoins) {
        this.costeCoins = costeCoins;
    }
}

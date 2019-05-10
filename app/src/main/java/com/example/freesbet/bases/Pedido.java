package com.example.freesbet.bases;

public class Pedido {
    String idPedido;
    String nombre;
    String email;
    String direccion;
    String ciudad;
    String provincia;
    String cp;
    String telefono;
    String idPremio;
    String nombrePremio;
    String idUsuario;
    String estado;

    public Pedido(String idPedido, String nombre, String email, String direccion, String ciudad, String provincia, String cp, String telefono, String idPremio, String nombrePremio, String idUsuario, String estado) {
        this.idPedido = idPedido;
        this.nombre = nombre;
        this.email = email;
        this.direccion = direccion;
        this.ciudad = ciudad;
        this.provincia = provincia;
        this.cp = cp;
        this.telefono = telefono;
        this.idPremio = idPremio;
        this.nombrePremio = nombrePremio;
        this.idUsuario = idUsuario;
        this.estado = estado;
    }

    public Pedido(){

    }

    public String getIdPedido() {
        return idPedido;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getCiudad() {
        return ciudad;
    }

    public String getProvincia() {
        return provincia;
    }

    public String getCp() {
        return cp;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getIdPremio() {
        return idPremio;
    }

    public String getNombrePremio() {
        return nombrePremio;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public String getEstado() {
        return estado;
    }

    public void setIdPedido(String idPedido) {
        this.idPedido = idPedido;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setIdPremio(String idPremio) {
        this.idPremio = idPremio;
    }

    public void setNombrePremio(String nombrePremio) {
        this.nombrePremio = nombrePremio;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}

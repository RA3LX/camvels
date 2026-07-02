package com.camvels.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Usuario {
    private int id;
    private String usuario;
    private String password;
    private String nombre;
    private String rol;

    public Usuario() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }
    @JsonIgnore
    public String getPassword() { return password; }
    @JsonProperty
    public void setPassword(String password) { this.password = password; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public boolean tieneRol(String rolEsperado) {
        return rol != null && rol.equals(rolEsperado);
    }

    public boolean puedeGestionarMovimientos() {
        return tieneRol(RolUsuario.ADMIN.getValor()) || tieneRol(RolUsuario.SUPERVISOR.getValor());
    }
}

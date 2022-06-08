package com.example.route4you.model;

/**
 * Clase que se encarga de modelar la empresa
 *
 * @author Legions
 * @version 1.1
 */
public class Empresa {
    private String uid;
    private String nombre;
    private String email;
    private String telefono;
    private String contraseña;
    private String direccion;
    private String numeroControles;

    public Empresa() {
    }

    public String getNombre() {
        return nombre;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getNumeroControles() {
        return numeroControles;
    }

    public void setNumeroControles(String numeroControles) {
        this.numeroControles = numeroControles;
    }

    /**
     * Al listar las empresas muestra el nombre
     * @return el nombre de la empresa
     */
    @Override
    public String toString(){
        return nombre;
    }
}

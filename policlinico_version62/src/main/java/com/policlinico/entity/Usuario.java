package com.policlinico.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "usuario")
public class Usuario {

    // ─── Atributos ────────────────────────────────────────────────────────────

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String dni;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellido;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column
    private String telefono;

    @Column(nullable = false)
    private String rol;

    @Column(name = "activo", nullable = false)
    private Boolean activo;

    // ─── Constructores ────────────────────────────────────────────────────────

    public Usuario() {
    }

    public Usuario(String dni, String nombre, String apellido, String email,
                   String password, String telefono, String rol, Boolean activo) {
        this.dni           = dni;
        this.nombre        = nombre;
        this.apellido      = apellido;
        this.email         = email;
        this.password      = password;
        this.telefono      = telefono;
        this.rol           = rol;
        this.activo        = activo;
    }

    // ─── Getters & Setters ────────────────────────────────────────────────────

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    // ─── toString ─────────────────────────────────────────────────────────────

    @Override
    public String toString() {
        return "Usuario{" +
                "id="               + id             +
                ", dni='"           + dni            + '\'' +
                ", nombre='"        + nombre         + '\'' +
                ", apellido='"      + apellido       + '\'' +
                ", email='"         + email          + '\'' +
                ", telefono='"      + telefono       + '\'' +
                ", rol='"           + rol            + '\'' +
                ", activo="         + activo         +
                '}';
    }
}

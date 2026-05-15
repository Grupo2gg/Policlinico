package com.policlinico.model;

public class EstadoCita {

    public static final int PENDIENTE_ID = 1;
    public static final int CONFIRMADA_ID = 2;
    public static final int CANCELADA_ID = 3;

    private int id;
    private String nombre;

    public EstadoCita() {
    }

    public EstadoCita(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String toString() {
        return "EstadoCita{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}

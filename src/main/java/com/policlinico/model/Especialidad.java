package com.policlinico.model;

public class Especialidad {

    private int id;
    private String nombre;
    private String descripcion;
    private String medico;
    private String horarioDisponible;
    private boolean activa;

    public Especialidad() {
    }

    public Especialidad(int id, String nombre, String descripcion, String medico,
                        String horarioDisponible, boolean activa) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.medico = medico;
        this.horarioDisponible = horarioDisponible;
        this.activa = activa;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getMedico() {
        return medico;
    }

    public void setMedico(String medico) {
        this.medico = medico;
    }

    public String getHorarioDisponible() {
        return horarioDisponible;
    }

    public void setHorarioDisponible(String horarioDisponible) {
        this.horarioDisponible = horarioDisponible;
    }

    public boolean isActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }

    @Override
    public String toString() {
        return "Especialidad{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", medico='" + medico + '\'' +
                ", horarioDisponible='" + horarioDisponible + '\'' +
                ", activa=" + activa +
                '}';
    }
}

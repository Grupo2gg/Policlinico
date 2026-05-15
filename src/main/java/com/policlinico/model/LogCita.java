package com.policlinico.model;

public class LogCita {

    private int id;
    private int citaId;
    private String fecha;
    private String accion;

    public LogCita() {
    }

    public LogCita(int id, int citaId, String fecha, String accion) {
        this.id = id;
        this.citaId = citaId;
        this.fecha = fecha;
        this.accion = accion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCitaId() {
        return citaId;
    }

    public void setCitaId(int citaId) {
        this.citaId = citaId;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public String toString() {
        return "LogCita{" +
                "id=" + id +
                ", citaId=" + citaId +
                ", fecha='" + fecha + '\'' +
                ", accion='" + accion + '\'' +
                '}';
    }
}

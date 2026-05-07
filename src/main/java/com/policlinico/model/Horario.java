package com.policlinico.model;

public class Horario {

    private int id;
    private int disponibilidadId;
    private String hora;
    private boolean disponible;

    public Horario() {
    }

    public Horario(int id, int disponibilidadId, String hora, boolean disponible) {
        this.id = id;
        this.disponibilidadId = disponibilidadId;
        this.hora = hora;
        this.disponible = disponible;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDisponibilidadId() {
        return disponibilidadId;
    }

    public void setDisponibilidadId(int disponibilidadId) {
        this.disponibilidadId = disponibilidadId;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public String toString() {
        return "Horario{" +
                "id=" + id +
                ", disponibilidadId=" + disponibilidadId +
                ", hora='" + hora + '\'' +
                ", disponible=" + disponible +
                '}';
    }
}

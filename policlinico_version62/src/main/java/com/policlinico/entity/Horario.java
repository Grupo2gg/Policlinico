package com.policlinico.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalTime;

@Entity
@Table(name = "horario")
public class Horario {

    // ─── Atributos ────────────────────────────────────────────────────────────

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "disponibilidad_id", nullable = false)
    private Disponibilidad disponibilidad;

    @Column(nullable = false)
    private LocalTime horaInicio;

    @Column(nullable = false)
    private LocalTime horaFin;

    @Column(nullable = false)
    private Boolean disponible;

    @Column(nullable = false)
    private String estado;

    // ─── Constructores ────────────────────────────────────────────────────────

    public Horario() {
    }

    public Horario(Disponibilidad disponibilidad, LocalTime horaInicio,
                   LocalTime horaFin, Boolean disponible) {
        this.disponibilidad = disponibilidad;
        this.horaInicio     = horaInicio;
        this.horaFin        = horaFin;
        this.disponible     = disponible;
        this.estado         = Boolean.TRUE.equals(disponible) ? "DISPONIBLE" : "RESERVADO";
    }

    // ─── Getters & Setters ────────────────────────────────────────────────────

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Disponibilidad getDisponibilidad() {
        return disponibilidad;
    }

    public void setDisponibilidad(Disponibilidad disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(LocalTime horaFin) {
        this.horaFin = horaFin;
    }

    public Boolean getDisponible() {
        return disponible;
    }

    public void setDisponible(Boolean disponible) {
        this.disponible = disponible;
        this.estado = Boolean.TRUE.equals(disponible) ? "DISPONIBLE" : "RESERVADO";
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
        this.disponible = "DISPONIBLE".equals(estado);
    }

    // ─── toString ─────────────────────────────────────────────────────────────

    @Override
    public String toString() {
        return "Horario{" +
                "id="                + id +
                ", disponibilidad="  + (disponibilidad != null ? disponibilidad.getId() : "null") +
                ", horaInicio="      + horaInicio +
                ", horaFin="         + horaFin +
                ", disponible="      + disponible +
                ", estado='"         + estado + '\'' +
                '}';
    }
}

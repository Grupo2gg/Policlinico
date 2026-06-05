package com.policlinico.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "disponibilidad")
public class Disponibilidad {

    // ─── Atributos ────────────────────────────────────────────────────────────

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "medico_id", nullable = false)
    private Medico medico;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(nullable = false)
    private String diaSemana;

    @Column(name = "activo", nullable = false)
    private Boolean activo;

    // ─── Constructores ────────────────────────────────────────────────────────

    public Disponibilidad() {
    }

    public Disponibilidad(Medico medico, LocalDate fecha,
                          String diaSemana, Boolean activo) {
        this.medico      = medico;
        this.fecha       = fecha;
        this.diaSemana   = diaSemana;
        this.activo      = activo;
    }

    // ─── Getters & Setters ────────────────────────────────────────────────────

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(String diaSemana) {
        this.diaSemana = diaSemana;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public String getEstado() {
        return Boolean.TRUE.equals(activo) ? "ACTIVO" : "INACTIVO";
    }

    // ─── toString (opcional) ──────────────────────────────────────────────────

    @Override
    public String toString() {
        return "Disponibilidad{" +
                "id="             + id +
                ", medico="       + (medico      != null ? medico.getId()       : "null") +
                ", fecha="        + fecha +
                ", diaSemana='"   + diaSemana    + '\'' +
                ", activo="       + activo +
                '}';
    }
}

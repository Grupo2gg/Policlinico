package com.policlinico.entity;

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
@Table(name = "atencion")
public class Atencion {

    // ─── Atributos ────────────────────────────────────────────────────────────

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cita_id", nullable = false)
    private Cita cita;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "medico_id", nullable = false)
    private Medico medico;

    @Column(nullable = false)
    private String diagnostico;

    @Column
    private String observaciones;

    @Column(nullable = false)
    private LocalDate fechaAtencion;

    @Column(nullable = false)
    private String estado;

    // ─── Constructores ────────────────────────────────────────────────────────

    public Atencion() {
    }

    public Atencion(Cita cita, Medico medico, String diagnostico,
                    String observaciones, LocalDate fechaAtencion, String estado) {
        this.cita           = cita;
        this.medico         = medico;
        this.diagnostico    = diagnostico;
        this.observaciones  = observaciones;
        this.fechaAtencion  = fechaAtencion;
        this.estado         = estado;
    }

    // ─── Getters & Setters ────────────────────────────────────────────────────

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cita getCita() {
        return cita;
    }

    public void setCita(Cita cita) {
        this.cita = cita;
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public LocalDate getFechaAtencion() {
        return fechaAtencion;
    }

    public void setFechaAtencion(LocalDate fechaAtencion) {
        this.fechaAtencion = fechaAtencion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    // ─── toString (opcional) ──────────────────────────────────────────────────

    @Override
    public String toString() {
        return "Atencion{" +
                "id="              + id +
                ", cita="          + (cita   != null ? cita.getId()   : "null") +
                ", medico="        + (medico != null ? medico.getId() : "null") +
                ", diagnostico='"  + diagnostico   + '\'' +
                ", observaciones='" + observaciones + '\'' +
                ", fechaAtencion=" + fechaAtencion +
                ", estado='" + estado + '\'' +
                '}';
    }
}

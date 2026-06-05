package com.policlinico.entity;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "cita")
public class Cita {

    // ─── Atributos ────────────────────────────────────────────────────────────

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "paciente_id", nullable = false)
    private Usuario paciente;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "medico_id", nullable = false)
    private Medico medico;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "horario_id", nullable = false)
    private Horario horario;

    @Column(nullable = false)
    private String estado;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column
    private String motivo;

    // ─── Constructores ────────────────────────────────────────────────────────

    public Cita() {
    }

    public Cita(Usuario paciente, Medico medico, Horario horario,
                String estado, LocalDate fecha, String motivo) {
        this.paciente   = paciente;
        this.medico     = medico;
        this.horario    = horario;
        this.estado     = estado;
        this.fecha      = fecha;
        this.motivo     = motivo;
    }

    // ─── Getters & Setters ────────────────────────────────────────────────────

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getPaciente() {
        return paciente;
    }

    public void setPaciente(Usuario paciente) {
        this.paciente = paciente;
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    public Horario getHorario() {
        return horario;
    }

    public void setHorario(Horario horario) {
        this.horario = horario;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    // ─── toString (opcional) ──────────────────────────────────────────────────

    @Override
    public String toString() {
        return "Cita{" +
                "id="           + id +
                ", paciente="   + (paciente   != null ? paciente.getId()   : "null") +
                ", medico="     + (medico     != null ? medico.getId()     : "null") +
                ", horario="    + (horario    != null ? horario.getId()    : "null") +
                ", estado='"     + estado + '\'' +
                ", fecha="      + fecha +
                ", motivo='"    + motivo + '\'' +
                '}';
    }
}

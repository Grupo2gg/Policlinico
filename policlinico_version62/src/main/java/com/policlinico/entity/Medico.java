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

@Entity
@Table(name = "medico")
public class Medico {

    // ─── Atributos ────────────────────────────────────────────────────────────

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "especialidad_id", nullable = false)
    private Especialidad especialidad;

    @Column(unique = true)
    private String cmp;

    // Único campo de estado: true = activo, false = inactivo
    @Column(name = "activo", nullable = false)
    private Boolean activo;

    // ─── Constructores ────────────────────────────────────────────────────────

    public Medico() {
    }

    public Medico(Usuario usuario, Especialidad especialidad, Boolean activo) {
        this.usuario      = usuario;
        this.especialidad = especialidad;
        this.activo       = activo;
    }

    // ─── Getters & Setters ────────────────────────────────────────────────────

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Especialidad getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(Especialidad especialidad) {
        this.especialidad = especialidad;
    }

    public String getCmp() {
        return cmp;
    }

    public void setCmp(String cmp) {
        this.cmp = cmp;
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
        return "Medico{" +
                "id="              + id +
                ", usuario="       + (usuario      != null ? usuario.getId()      : "null") +
                ", especialidad="  + (especialidad != null ? especialidad.getId() : "null") +
                ", cmp='"          + cmp + '\'' +
                ", activo="        + activo +
                '}';
    }
}

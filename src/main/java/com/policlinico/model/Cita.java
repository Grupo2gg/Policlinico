package com.policlinico.model;

public class Cita {

    private int id;
    private int usuarioId;
    private String nombrePaciente;
    private String especialidad;
    private String medico;
    private String fecha;
    private String hora;
    private String estado = "PENDIENTE";
    private String motivo;
    private String fechaCreacion;

    public Cita() {
    }

    public Cita(int id, int usuarioId, String nombrePaciente, String especialidad,
                String medico, String fecha, String hora, String estado,
                String motivo, String fechaCreacion) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.nombrePaciente = nombrePaciente;
        this.especialidad = especialidad;
        this.medico = medico;
        this.fecha = fecha;
        this.hora = hora;
        this.estado = estado;
        this.motivo = motivo;
        this.fechaCreacion = fechaCreacion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getNombrePaciente() {
        return nombrePaciente;
    }

    public void setNombrePaciente(String nombrePaciente) {
        this.nombrePaciente = nombrePaciente;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getMedico() {
        return medico;
    }

    public void setMedico(String medico) {
        this.medico = medico;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    @Override
    public String toString() {
        return "Cita{" +
                "id=" + id +
                ", usuarioId=" + usuarioId +
                ", nombrePaciente='" + nombrePaciente + '\'' +
                ", especialidad='" + especialidad + '\'' +
                ", medico='" + medico + '\'' +
                ", fecha='" + fecha + '\'' +
                ", hora='" + hora + '\'' +
                ", estado='" + estado + '\'' +
                ", motivo='" + motivo + '\'' +
                ", fechaCreacion='" + fechaCreacion + '\'' +
                '}';
    }
}

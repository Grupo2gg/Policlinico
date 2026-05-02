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

    /** @return ID único de la cita */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /** @return ID del usuario propietario de la cita */
    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    /** @return Nombre completo del paciente */
    public String getNombrePaciente() {
        return nombrePaciente;
    }

    public void setNombrePaciente(String nombrePaciente) {
        this.nombrePaciente = nombrePaciente;
    }

    /** @return Especialidad médica de la cita */
    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    /** @return Nombre del médico asignado */
    public String getMedico() {
        return medico;
    }

    public void setMedico(String medico) {
        this.medico = medico;
    }

    /** @return Fecha de la cita en formato yyyy-MM-dd */
    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    /** @return Hora de la cita en formato HH:mm */
    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    /** @return Estado actual: PENDIENTE, CONFIRMADA o CANCELADA */
    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    /** @return Motivo de consulta ingresado por el paciente */
    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    /** @return Fecha en que se registró la cita */
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

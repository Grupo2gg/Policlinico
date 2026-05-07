package com.policlinico.model;

public class Medico {

    public static final String ESTADO_ACTIVO = "ACTIVO";
    public static final String ESTADO_INACTIVO = "INACTIVO";
    public static final String GENERO_MASCULINO = "MASCULINO";
    public static final String GENERO_FEMENINO = "FEMENINO";

    private int id;
    private String nombre;
    private String genero;  // MASCULINO, FEMENINO
    private String cedula;
    private String telefono;
    private String email;
    private String direccion;
    private int especialidadId;
    private String especialidad;
    private String estado = ESTADO_ACTIVO;

    public Medico() {
    }

    public Medico(int id, String nombre, int especialidadId) {
        this.id = id;
        this.nombre = nombre;
        this.especialidadId = especialidadId;
    }

    public Medico(int id, String nombre, String especialidad, String estado) {
        this.id = id;
        this.nombre = nombre;
        this.especialidad = especialidad;
        this.estado = estado;
    }

    public Medico(int id, String nombre, int especialidadId, String especialidad, String estado) {
        this.id = id;
        this.nombre = nombre;
        this.especialidadId = especialidadId;
        this.especialidad = especialidad;
        this.estado = estado;
    }

    // ...existing code...
    
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

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public int getEspecialidadId() {
        return especialidadId;
    }

    public void setEspecialidadId(int especialidadId) {
        this.especialidadId = especialidadId;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public boolean isActivo() {
        return ESTADO_ACTIVO.equalsIgnoreCase(estado);
    }

    /**
     * Retorna el nombre con el título (Dr. o Dra.) basado en el género
     */
    public String getNombreConTitulo() {
        if (nombre == null || nombre.trim().isEmpty()) {
            return nombre;
        }
        if (GENERO_FEMENINO.equalsIgnoreCase(genero)) {
            return "Dra. " + nombre;
        } else {
            return "Dr. " + nombre;
        }
    }

    @Override
    public String toString() {
        return "Medico{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", genero='" + genero + '\'' +
                ", cedula='" + cedula + '\'' +
                ", telefono='" + telefono + '\'' +
                ", email='" + email + '\'' +
                ", direccion='" + direccion + '\'' +
                ", especialidadId=" + especialidadId +
                ", especialidad='" + especialidad + '\'' +
                ", estado='" + estado + '\'' +
                '}';
    }
}

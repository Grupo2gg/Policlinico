package com.policlinico.model;

public class UsuarioRol {

    private int usuarioId;
    private int rolId;

    public UsuarioRol() {
    }

    public UsuarioRol(int usuarioId, int rolId) {
        this.usuarioId = usuarioId;
        this.rolId = rolId;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public int getRolId() {
        return rolId;
    }

    public void setRolId(int rolId) {
        this.rolId = rolId;
    }

    public String toString() {
        return "UsuarioRol{" +
                "usuarioId=" + usuarioId +
                ", rolId=" + rolId +
                '}';
    }
}

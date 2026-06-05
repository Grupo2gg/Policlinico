package com.policlinico.adapter;

import com.policlinico.entity.Usuario;
import com.policlinico.model.UsuarioModelo;

public class UsuarioAdapter {
    public static UsuarioModelo aModelo(Usuario entidad) {
        if (entidad == null) return null;
        UsuarioModelo modelo = new UsuarioModelo();
        modelo.setId(entidad.getId());
        modelo.setNombre(entidad.getNombre());
        modelo.setApellido(entidad.getApellido());
        modelo.setEmail(entidad.getEmail());
        modelo.setDni(entidad.getDni());
        modelo.setPassword(entidad.getPassword());
        modelo.setTelefono(entidad.getTelefono());
        modelo.setActivo(entidad.getActivo());
        modelo.setRol(entidad.getRol());
        return modelo;
    }

    public static Usuario aEntidad(UsuarioModelo modelo) {
        if (modelo == null) return null;
        Usuario entidad = new Usuario();
        entidad.setId(modelo.getId());
        entidad.setNombre(modelo.getNombre());
        entidad.setApellido(modelo.getApellido());
        entidad.setEmail(modelo.getEmail());
        entidad.setDni(modelo.getDni());
        entidad.setPassword(modelo.getPassword());
        entidad.setTelefono(modelo.getTelefono());
        entidad.setActivo(modelo.getActivo() == null || modelo.getActivo());
        entidad.setRol(modelo.getRol() == null || modelo.getRol().isBlank() ? "PACIENTE" : modelo.getRol());
        return entidad;
    }
}

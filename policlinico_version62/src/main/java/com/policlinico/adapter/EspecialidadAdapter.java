package com.policlinico.adapter;

import com.policlinico.entity.Especialidad;
import com.policlinico.model.EspecialidadModelo;

public class EspecialidadAdapter {
    public static EspecialidadModelo aModelo(Especialidad entidad) {
        if (entidad == null) return null;
        EspecialidadModelo modelo = new EspecialidadModelo();
        modelo.setId(entidad.getId());
        modelo.setNombre(entidad.getNombre());
        modelo.setDescripcion(entidad.getDescripcion());
        modelo.setEstado(entidad.getEstado() != null ? entidad.getEstado() : "ACTIVA");
        return modelo;
    }

    public static Especialidad aEntidad(EspecialidadModelo modelo) {
        if (modelo == null) return null;
        Especialidad entidad = new Especialidad();
        entidad.setId(modelo.getId());
        entidad.setNombre(modelo.getNombre());
        entidad.setDescripcion(modelo.getDescripcion());
        entidad.setEstado(modelo.getEstado() == null || modelo.getEstado().isBlank() ? "ACTIVA" : modelo.getEstado());
        return entidad;
    }
}

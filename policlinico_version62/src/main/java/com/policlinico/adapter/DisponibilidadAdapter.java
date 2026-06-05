package com.policlinico.adapter;

import com.policlinico.entity.Disponibilidad;
import com.policlinico.entity.Medico;
import com.policlinico.model.DisponibilidadModelo;

public class DisponibilidadAdapter {
    public static DisponibilidadModelo aModelo(Disponibilidad entidad) {
        if (entidad == null) return null;
        DisponibilidadModelo modelo = new DisponibilidadModelo();
        modelo.setId(entidad.getId());
        modelo.setMedicoId(entidad.getMedico().getId());
        modelo.setFecha(entidad.getFecha());
        modelo.setDiaSemana(entidad.getDiaSemana());
        modelo.setActivo(entidad.getActivo());
        modelo.setNombreMedico(entidad.getMedico().getUsuario().getNombre());
        return modelo;
    }

    public static Disponibilidad aEntidad(DisponibilidadModelo modelo) {
        if (modelo == null) return null;
        Medico medico = new Medico();
        medico.setId(modelo.getMedicoId());
        Disponibilidad entidad = new Disponibilidad();
        entidad.setId(modelo.getId());
        entidad.setMedico(medico);
        entidad.setFecha(modelo.getFecha());
        entidad.setDiaSemana(modelo.getDiaSemana());
        entidad.setActivo(modelo.getActivo() == null || modelo.getActivo());
        return entidad;
    }
}

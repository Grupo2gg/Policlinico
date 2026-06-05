package com.policlinico.adapter;

import com.policlinico.entity.Disponibilidad;
import com.policlinico.entity.Horario;
import com.policlinico.model.HorarioModelo;

public class HorarioAdapter {
    public static HorarioModelo aModelo(Horario entidad) {
        if (entidad == null) return null;
        HorarioModelo modelo = new HorarioModelo();
        modelo.setId(entidad.getId());
        modelo.setHoraInicio(entidad.getHoraInicio());
        modelo.setHoraFin(entidad.getHoraFin());
        modelo.setDisponible(entidad.getDisponible());
        modelo.setEstado(entidad.getEstado() != null ? entidad.getEstado() : "DISPONIBLE");

        if (entidad.getDisponibilidad() != null) {
            modelo.setDisponibilidadId(entidad.getDisponibilidad().getId());
            modelo.setFecha(entidad.getDisponibilidad().getFecha());
            modelo.setDiaSemana(entidad.getDisponibilidad().getDiaSemana());
            if (entidad.getDisponibilidad().getMedico() != null) {
                modelo.setMedicoId(entidad.getDisponibilidad().getMedico().getId());
                if (entidad.getDisponibilidad().getMedico().getUsuario() != null) {
                    modelo.setNombreMedico(entidad.getDisponibilidad().getMedico().getUsuario().getNombre() + " "
                            + entidad.getDisponibilidad().getMedico().getUsuario().getApellido());
                }
            }
        }
        return modelo;
    }

    public static Horario aEntidad(HorarioModelo modelo) {
        if (modelo == null) return null;
        Disponibilidad disponibilidad = new Disponibilidad();
        disponibilidad.setId(modelo.getDisponibilidadId());
        Horario entidad = new Horario();
        entidad.setId(modelo.getId());
        entidad.setDisponibilidad(disponibilidad);
        entidad.setHoraInicio(modelo.getHoraInicio());
        entidad.setHoraFin(modelo.getHoraFin());
        entidad.setEstado(modelo.getEstado() != null && !modelo.getEstado().isBlank()
                ? modelo.getEstado()
                : (modelo.getDisponible() == null || modelo.getDisponible() ? "DISPONIBLE" : "RESERVADO"));
        return entidad;
    }
}

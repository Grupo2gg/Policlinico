package com.policlinico.adapter;

import com.policlinico.entity.Atencion;
import com.policlinico.entity.Cita;
import com.policlinico.entity.Medico;
import com.policlinico.model.AtencionModelo;

public class AtencionAdapter {
    public static AtencionModelo aModelo(Atencion entidad) {
        if (entidad == null) return null;
        AtencionModelo modelo = new AtencionModelo();
        modelo.setId(entidad.getId());
        modelo.setCitaId(entidad.getCita().getId());
        modelo.setMedicoId(entidad.getMedico().getId());
        modelo.setDiagnostico(entidad.getDiagnostico());
        modelo.setObservaciones(entidad.getObservaciones());
        modelo.setFechaAtencion(entidad.getFechaAtencion());
        modelo.setEstado(entidad.getEstado());
        modelo.setPaciente(entidad.getCita().getPaciente().getNombre());
        if (entidad.getMedico() != null && entidad.getMedico().getUsuario() != null) {
            modelo.setNombreMedico(entidad.getMedico().getUsuario().getNombre() + " " + entidad.getMedico().getUsuario().getApellido());
        }
        return modelo;
    }

    public static Atencion aEntidad(AtencionModelo modelo) {
        if (modelo == null) return null;
        Cita cita = new Cita();
        Medico medico = new Medico();
        cita.setId(modelo.getCitaId());
        medico.setId(modelo.getMedicoId());
        Atencion entidad = new Atencion();
        entidad.setId(modelo.getId());
        entidad.setCita(cita);
        entidad.setMedico(medico);
        entidad.setEstado(modelo.getEstado());
        entidad.setDiagnostico(modelo.getDiagnostico());
        entidad.setObservaciones(modelo.getObservaciones());
        entidad.setFechaAtencion(modelo.getFechaAtencion());
        return entidad;
    }
}

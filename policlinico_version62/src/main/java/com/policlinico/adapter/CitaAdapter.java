package com.policlinico.adapter;

import com.policlinico.entity.Cita;
import com.policlinico.entity.Horario;
import com.policlinico.entity.Medico;
import com.policlinico.entity.Usuario;
import com.policlinico.model.CitaModelo;

public class CitaAdapter {
    public static CitaModelo aModelo(Cita entidad) {
        if (entidad == null) return null;
        CitaModelo modelo = new CitaModelo();
        modelo.setId(entidad.getId());
        modelo.setPacienteId(entidad.getPaciente().getId());
        modelo.setMedicoId(entidad.getMedico().getId());
        modelo.setHorarioId(entidad.getHorario().getId());
        if (entidad.getFecha() != null) {
            modelo.setFecha(entidad.getFecha());
        } else if (entidad.getHorario() != null && entidad.getHorario().getDisponibilidad() != null) {
            modelo.setFecha(entidad.getHorario().getDisponibilidad().getFecha());
        }
        modelo.setMotivo(entidad.getMotivo());
        modelo.setPaciente(entidad.getPaciente().getNombre() + " " + entidad.getPaciente().getApellido());
        modelo.setMedico(entidad.getMedico().getUsuario().getNombre() + " " + entidad.getMedico().getUsuario().getApellido());
        modelo.setEspecialidad(entidad.getMedico().getEspecialidad().getNombre());
        modelo.setEstado(entidad.getEstado());
        modelo.setHoraInicio(entidad.getHorario().getHoraInicio());
        modelo.setHoraFin(entidad.getHorario().getHoraFin());
        return modelo;
    }

    public static Cita aEntidad(CitaModelo modelo) {
        if (modelo == null) return null;
        Cita entidad = new Cita();
        entidad.setId(modelo.getId());
        entidad.setPaciente(paciente(modelo));
        entidad.setMedico(medico(modelo));
        entidad.setHorario(horario(modelo));
        entidad.setEstado(modelo.getEstado());
        entidad.setFecha(modelo.getFecha());
        entidad.setMotivo(modelo.getMotivo());
        return entidad;
    }

    private static Usuario paciente(CitaModelo modelo) {
        Usuario paciente = new Usuario();
        paciente.setId(modelo.getPacienteId());
        return paciente;
    }

    private static Medico medico(CitaModelo modelo) {
        Medico medico = new Medico();
        medico.setId(modelo.getMedicoId());
        return medico;
    }

    private static Horario horario(CitaModelo modelo) {
        Horario horario = new Horario();
        horario.setId(modelo.getHorarioId());
        return horario;
    }
}

package com.policlinico.service.impl;

import com.policlinico.model.Cita;
import com.policlinico.model.Horario;
import com.policlinico.repository.CitaDAO;
import com.policlinico.repository.HorarioDAO;
import com.policlinico.service.CitaService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CitaServiceImpl implements CitaService {

    @Autowired
    private CitaDAO citaDAO;

    @Autowired
    private HorarioDAO horarioDAO;

    public List<Cita> obtenerTodas() {
        return citaDAO.findAll();
    }

    public Cita obtenerPorId(int id) {
        return citaDAO.findById(id);
    }

    public Cita obtenerPorIdDeUsuario(int id, int usuarioId) {
        Cita cita = citaDAO.findById(id);
        if (cita == null || cita.getUsuarioId() != usuarioId) {
            return null;
        }
        return cita;
    }

    public void registrarCita(Cita cita) {
        completarDatos(cita);
        citaDAO.save(cita);
    }

    public void actualizarCita(Cita cita) {
        completarDatos(cita);
        citaDAO.update(cita);
    }

    public void eliminarCita(int id) {
        citaDAO.delete(id);
    }

    public void cancelarCita(int id, int usuarioId) {
        Cita cita = obtenerPorIdDeUsuario(id, usuarioId);
        if (cita != null) {
            cita.setEstado("CANCELADA");
            cita.setEstadoId(3);
            citaDAO.update(cita);
        }
    }

    public List<Cita> obtenerPorUsuario(int usuarioId) {
        return citaDAO.findByUsuarioId(usuarioId);
    }

    public List<Cita> obtenerPorEstado(String estado) {
        return citaDAO.findByEstado(estado);
    }

    public List<String> obtenerHorasDisponibles() {
        return horarioDAO.findDisponibles().stream()
                .map(Horario::getHora)
                .collect(Collectors.toList());
    }

    private void completarDatos(Cita cita) {
        if (cita.getEstado() == null || cita.getEstado().isBlank()) {
            cita.setEstado(estadoNombre(cita.getEstadoId()));
        }
        cita.setEstadoId(estadoId(cita.getEstado()));
        if (cita.getFechaCreacion() == null || cita.getFechaCreacion().isBlank()) {
            cita.setFechaCreacion(java.time.LocalDate.now().toString());
        }
        if (cita.getHora() != null && !cita.getHora().isBlank()) {
            Horario horario = horarioDAO.findByHora(cita.getHora());
            if (horario != null) {
                cita.setHorarioId(horario.getId());
            }
        }
    }

    private int estadoId(String estado) {
        if ("CONFIRMADA".equalsIgnoreCase(estado)) {
            return 2;
        }
        if ("CANCELADA".equalsIgnoreCase(estado)) {
            return 3;
        }
        return 1;
    }

    private String estadoNombre(int estadoId) {
        if (estadoId == 2) {
            return "CONFIRMADA";
        }
        if (estadoId == 3) {
            return "CANCELADA";
        }
        return "PENDIENTE";
    }
}

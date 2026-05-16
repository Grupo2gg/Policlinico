package com.policlinico.service.impl;

import com.policlinico.model.Cita;
import com.policlinico.model.Horario;
import com.policlinico.repository.CitaRepository;
import com.policlinico.repository.HorarioRepository;
import com.policlinico.service.CitaService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CitaServiceImpl implements CitaService {

    @Autowired
    private CitaRepository citaRepository;

    @Autowired
    private HorarioRepository horarioRepository;

    public List<Cita> obtenerTodas() {
        return citaRepository.findAll();
    }

    public Cita obtenerPorId(int id) {
        return citaRepository.findById(id).orElse(null);
    }

    public Cita obtenerPorIdDeUsuario(int id, int usuarioId) {
        Cita cita = citaRepository.findById(id).orElse(null);
        if (cita == null || cita.getUsuarioId() != usuarioId) {
            return null;
        }
        return cita;
    }

    public void registrarCita(Cita cita) {
        completarDatos(cita);
        citaRepository.save(cita);
    }

    public void actualizarCita(Cita cita) {
        completarDatos(cita);
        citaRepository.save(cita);
    }

    public void eliminarCita(int id) {
        citaRepository.deleteById(id);
    }

    public void cancelarCita(int id, int usuarioId) {
        Cita cita = obtenerPorIdDeUsuario(id, usuarioId);
        if (cita != null) {
            cita.setEstado("CANCELADA");
            cita.setEstadoId(3);
            citaRepository.save(cita);
        }
    }

    public List<Cita> obtenerPorUsuario(int usuarioId) {
        return citaRepository.findByUsuarioId(usuarioId);
    }

    public List<Cita> obtenerPorEstado(String estado) {
        return citaRepository.findByEstado(estado);
    }

    public List<String> obtenerHorasDisponibles() {
        return horarioRepository.findByDisponibleTrue().stream()
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
            Horario horario = horarioRepository.findByHora(cita.getHora());
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

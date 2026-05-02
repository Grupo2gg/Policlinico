package com.policlinico.service.impl;

import com.policlinico.model.Cita;
import com.policlinico.repository.CitaDAO;
import com.policlinico.service.CitaService;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CitaServiceImpl implements CitaService {

    private static final String ESTADO_PENDIENTE   = "PENDIENTE";
    private static final String ESTADO_CANCELADA   = "CANCELADA";

    @Autowired
    private CitaDAO citaDAO;

    @Override
    public Cita obtenerPorId(int id) {
        return citaDAO.findById(id);
    }

    @Override
    public Cita obtenerPorIdDeUsuario(int id, int usuarioId) {
        Cita cita = citaDAO.findById(id);
        if (cita == null || cita.getUsuarioId() != usuarioId) {
            return null;
        }
        return cita;
    }

    @Override
    public void registrarCita(Cita cita) {
        validarCita(cita);
        boolean existeDuplicada = citaDAO.findByUsuarioId(cita.getUsuarioId()).stream()
                .anyMatch(item -> item.getFecha().equals(cita.getFecha())
                        && item.getHora().equals(cita.getHora()));
        if (existeDuplicada) {
            throw new IllegalArgumentException("Ya tienes una cita registrada para la misma fecha y hora");
        }
        cita.setFechaCreacion(LocalDate.now().toString());
        if (cita.getEstado() == null || cita.getEstado().isBlank()) {
            cita.setEstado(ESTADO_PENDIENTE);
        }
        citaDAO.save(cita);
    }

    @Override
    public void actualizarCita(Cita cita) {
        validarCita(cita);
        Cita actual = citaDAO.findById(cita.getId());
        if (actual == null || actual.getUsuarioId() != cita.getUsuarioId()) {
            throw new IllegalArgumentException("La cita solicitada no fue encontrada.");
        }

        if (tieneCitaDuplicada(cita, cita.getId())) {
            throw new IllegalArgumentException("Ya tienes una cita registrada para la misma fecha y hora");
        }
        if (horarioOcupadoPorOtroPaciente(cita, cita.getId())) {
            throw new IllegalArgumentException("El horario seleccionado ya no está disponible");
        }

        actual.setEspecialidad(cita.getEspecialidad());
        actual.setMedico(cita.getMedico());
        actual.setFecha(cita.getFecha());
        actual.setHora(cita.getHora());
        actual.setMotivo(cita.getMotivo());
        if (actual.getNombrePaciente() == null || actual.getNombrePaciente().isBlank()) {
            actual.setNombrePaciente(cita.getNombrePaciente());
        }
        citaDAO.update(actual);
    }

    @Override
    public void cancelarCita(int id, int usuarioId) {
        Cita cita = obtenerPorIdDeUsuario(id, usuarioId);
        if (cita == null) {
            throw new IllegalArgumentException("La cita solicitada no fue encontrada.");
        }
        if (!ESTADO_PENDIENTE.equalsIgnoreCase(cita.getEstado())) {
            return;
        }
        cita.setEstado(ESTADO_CANCELADA);
        citaDAO.update(cita);
    }

    @Override
    public List<Cita> obtenerPorUsuario(int usuarioId) {
        return citaDAO.findByUsuarioId(usuarioId);
    }

    @Override
    public List<Cita> obtenerPorEstado(String estado) {
        return citaDAO.findByEstado(estado);
    }

    @Override
    public List<String> obtenerHorasDisponibles() {
        return citaDAO.findHorasDisponibles();
    }

    /**
     * Valida los campos obligatorios de una cita antes de guardarla o actualizarla.
     */
    private void validarCita(Cita cita) {
        if (cita == null) {
            throw new IllegalArgumentException("La cita es obligatoria");
        }
        if (cita.getEspecialidad() == null || cita.getEspecialidad().isBlank()) {
            throw new IllegalArgumentException("La especialidad es obligatoria");
        }
        if (cita.getMedico() == null || cita.getMedico().isBlank()) {
            throw new IllegalArgumentException("El médico es obligatorio");
        }
        if (cita.getFecha() == null || cita.getFecha().isBlank()) {
            throw new IllegalArgumentException("La fecha es obligatoria");
        }
        LocalDate fecha;
        try {
            fecha = LocalDate.parse(cita.getFecha());
        } catch (Exception ex) {
            throw new IllegalArgumentException("La fecha ingresada no es válida");
        }
        if (fecha.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha no puede ser en el pasado");
        }
        if (cita.getHora() == null || cita.getHora().isBlank()) {
            throw new IllegalArgumentException("La hora es obligatoria");
        }
        if (!citaDAO.findHorasDisponibles().contains(cita.getHora())) {
            throw new IllegalArgumentException("La hora seleccionada no está disponible");
        }
        if (cita.getMotivo() == null || cita.getMotivo().isBlank()) {
            throw new IllegalArgumentException("El motivo de consulta es obligatorio");
        }
    }

    /** Verifica si el usuario ya tiene otra cita en la misma fecha y hora. */
    private boolean tieneCitaDuplicada(Cita cita, int citaIdExcluir) {
        return citaDAO.findByUsuarioId(cita.getUsuarioId()).stream()
                .filter(item -> item.getId() != citaIdExcluir)
                .anyMatch(item -> item.getFecha().equals(cita.getFecha())
                        && item.getHora().equals(cita.getHora()));
    }

    /** Verifica si el médico ya tiene otra cita activa en el mismo horario. */
    private boolean horarioOcupadoPorOtroPaciente(Cita cita, int citaIdExcluir) {
        return citaDAO.findAll().stream()
                .filter(item -> item.getId() != citaIdExcluir)
                .filter(item -> !ESTADO_CANCELADA.equalsIgnoreCase(item.getEstado()))
                .anyMatch(item -> item.getFecha().equals(cita.getFecha())
                        && item.getHora().equals(cita.getHora())
                        && item.getMedico().equals(cita.getMedico()));
    }
}

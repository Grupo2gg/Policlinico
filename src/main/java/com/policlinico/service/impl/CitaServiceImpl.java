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

    @Autowired
    private CitaDAO citaDAO;

    @Override
    public List<Cita> obtenerTodas() {
        return citaDAO.findAll();
    }

    @Override
    public Cita obtenerPorId(int id) {
        return citaDAO.findById(id);
    }

    @Override
    public void registrarCita(Cita cita) {
        cita.setFechaCreacion(LocalDate.now().toString());
        if (cita.getEstado() == null || cita.getEstado().isBlank()) {
            cita.setEstado("PENDIENTE");
        }
        citaDAO.save(cita);
    }

    @Override
    public void actualizarCita(Cita cita) {
        citaDAO.update(cita);
    }

    @Override
    public void cancelarCita(int id) {
        Cita cita = citaDAO.findById(id);
        if (cita != null) {
            cita.setEstado("CANCELADA");
            citaDAO.update(cita);
        }
    }

    @Override
    public void confirmarCita(int id) {
        Cita cita = citaDAO.findById(id);
        if (cita != null) {
            cita.setEstado("CONFIRMADA");
            citaDAO.update(cita);
        }
    }

    @Override
    public void eliminarCita(int id) {
        citaDAO.delete(id);
    }

    @Override
    public List<Cita> obtenerPorUsuario(int usuarioId) {
        return citaDAO.findByUsuarioId(usuarioId);
    }

    @Override
    public List<Cita> obtenerPorEstado(String estado) {
        return citaDAO.findByEstado(estado);
    }
}

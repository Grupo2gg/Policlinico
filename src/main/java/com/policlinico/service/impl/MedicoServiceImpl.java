package com.policlinico.service.impl;

import com.policlinico.model.Medico;
import com.policlinico.repository.MedicoDAO;
import com.policlinico.service.MedicoService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MedicoServiceImpl implements MedicoService {

    @Autowired
    private MedicoDAO medicoDAO;

    @Override
    public List<Medico> obtenerTodos() {
        return medicoDAO.findAll();
    }

    @Override
    public List<Medico> obtenerActivos() {
        return medicoDAO.findActivos();
    }

    @Override
    public Medico obtenerPorId(int id) {
        return medicoDAO.findById(id);
    }

    @Override
    public void registrar(Medico medico) {
        validar(medico);
        medicoDAO.save(medico);
    }

    @Override
    public void actualizar(Medico medico) {
        validar(medico);
        if (medicoDAO.findById(medico.getId()) == null) {
            throw new IllegalArgumentException("El medico solicitado no fue encontrado.");
        }
        medicoDAO.update(medico);
    }

    @Override
    public void eliminar(int id) {
        medicoDAO.delete(id);
    }

    private void validar(Medico medico) {
        if (medico == null) {
            throw new IllegalArgumentException("El medico es obligatorio");
        }
        if (medico.getNombre() == null || medico.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre del medico es obligatorio");
        }
        if (medico.getEspecialidad() == null || medico.getEspecialidad().isBlank()) {
            throw new IllegalArgumentException("La especialidad del medico es obligatoria");
        }
        if (medico.getEstado() == null || medico.getEstado().isBlank()) {
            medico.setEstado(Medico.ESTADO_ACTIVO);
        }
    }
}

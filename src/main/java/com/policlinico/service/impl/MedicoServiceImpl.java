package com.policlinico.service.impl;

import com.policlinico.model.Medico;
import com.policlinico.repository.MedicoRepository;
import com.policlinico.service.MedicoService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MedicoServiceImpl implements MedicoService {

    @Autowired
    private MedicoRepository medicoRepository;

    @Override
    public List<Medico> obtenerTodos() {
        return medicoRepository.findAll();
    }

    @Override
    public List<Medico> obtenerActivos() {
        return medicoRepository.findByEstado(Medico.ESTADO_ACTIVO);
    }

    @Override
    public Medico obtenerPorId(int id) {
        return medicoRepository.findById(id).orElse(null);
    }

    @Override
    public void registrar(Medico medico) {
        validar(medico);
        medicoRepository.save(medico);
    }

    @Override
    public void actualizar(Medico medico) {
        validar(medico);
        if (medicoRepository.findById(medico.getId()).isEmpty()) {
            throw new IllegalArgumentException("El medico solicitado no fue encontrado.");
        }
        medicoRepository.save(medico);
    }

    @Override
    public void eliminar(int id) {
        medicoRepository.deleteById(id);
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

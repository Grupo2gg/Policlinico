package com.policlinico.service.impl;

import com.policlinico.model.Especialidad;
import com.policlinico.repository.EspecialidadRepository;
import com.policlinico.service.EspecialidadService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EspecialidadServiceImpl implements EspecialidadService {

    @Autowired
    private EspecialidadRepository especialidadRepository;

    @Override
    public List<Especialidad> obtenerActivas() {
        return especialidadRepository.findByActivaTrue();
    }

    @Override
    public List<Especialidad> obtenerTodas() {
        return especialidadRepository.findAll();
    }

    @Override
    public Especialidad obtenerPorId(int id) {
        return especialidadRepository.findById(id).orElse(null);
    }

    @Override
    public void registrar(Especialidad especialidad) {
        especialidadRepository.save(especialidad);
    }

    @Override
    public void actualizar(Especialidad especialidad) {
        especialidadRepository.save(especialidad);
    }

    @Override
    public void eliminar(int id) {
        especialidadRepository.deleteById(id);
    }

    @Override
    public List<String> obtenerMedicos() {
        return especialidadRepository.findAll()
                .stream()
                .map(Especialidad::getMedico)
                .distinct()
                .collect(Collectors.toList());
    }
}

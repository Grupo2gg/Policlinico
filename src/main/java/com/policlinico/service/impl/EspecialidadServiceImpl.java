package com.policlinico.service.impl;

import com.policlinico.model.Especialidad;
import com.policlinico.repository.EspecialidadDAO;
import com.policlinico.service.EspecialidadService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EspecialidadServiceImpl implements EspecialidadService {

    @Autowired
    private EspecialidadDAO especialidadDAO;

    @Override
    public List<Especialidad> obtenerActivas() {
        return especialidadDAO.findActivas();
    }

    @Override
    public Especialidad obtenerPorId(int id) {
        return especialidadDAO.findById(id);
    }

    @Override
    public List<String> obtenerMedicos() {
        return especialidadDAO.findAll()
                .stream()
                .map(Especialidad::getMedico)
                .distinct()
                .collect(Collectors.toList());
    }
}

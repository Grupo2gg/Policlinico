package com.policlinico.service.impl;

import com.policlinico.model.Atencion;
import com.policlinico.repository.AtencionRepository;
import com.policlinico.service.AtencionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AtencionServiceImpl implements AtencionService {

    @Autowired
    private AtencionRepository atencionRepository;

    @Override
    public Atencion registrar(Atencion atencion) {
        return atencionRepository.save(atencion);
    }

    @Override
    public Atencion obtenerPorCita(int citaId) {
        return atencionRepository.findByCitaId(citaId);
    }
}

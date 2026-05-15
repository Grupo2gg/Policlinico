package com.policlinico.service.impl;

import com.policlinico.model.Horario;
import com.policlinico.repository.HorarioRepository;
import com.policlinico.service.HorarioService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HorarioServiceImpl implements HorarioService {

    @Autowired
    private HorarioRepository horarioRepository;

    @Override
    public List<Horario> obtenerTodos() {
        return horarioRepository.findAll();
    }

    @Override
    public List<Horario> obtenerDisponibles() {
        return horarioRepository.findByDisponibleTrue();
    }

    @Override
    public Horario obtenerPorId(int id) {
        return horarioRepository.findById(id).orElse(null);
    }

    @Override
    public void registrar(Horario horario) {
        horarioRepository.save(horario);
    }

    @Override
    public void actualizar(Horario horario) {
        horarioRepository.save(horario);
    }

    @Override
    public void eliminar(int id) {
        horarioRepository.deleteById(id);
    }
}

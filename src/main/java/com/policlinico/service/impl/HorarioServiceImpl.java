package com.policlinico.service.impl;

import com.policlinico.model.Horario;
import com.policlinico.repository.HorarioDAO;
import com.policlinico.service.HorarioService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HorarioServiceImpl implements HorarioService {

    @Autowired
    private HorarioDAO horarioDAO;

    @Override
    public List<Horario> obtenerTodos() {
        return horarioDAO.findAll();
    }

    @Override
    public List<Horario> obtenerDisponibles() {
        return horarioDAO.findDisponibles();
    }

    @Override
    public Horario obtenerPorId(int id) {
        return horarioDAO.findById(id);
    }

    @Override
    public void registrar(Horario horario) {
        horarioDAO.save(horario);
    }

    @Override
    public void actualizar(Horario horario) {
        horarioDAO.update(horario);
    }

    @Override
    public void eliminar(int id) {
        horarioDAO.delete(id);
    }
}

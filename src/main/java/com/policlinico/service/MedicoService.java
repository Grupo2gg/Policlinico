package com.policlinico.service;

import com.policlinico.model.Medico;
import java.util.List;

public interface MedicoService {

    List<Medico> obtenerTodos();

    List<Medico> obtenerActivos();

    Medico obtenerPorId(int id);

    void registrar(Medico medico);

    void actualizar(Medico medico);

    void eliminar(int id);
}

package com.policlinico.service;

import com.policlinico.model.Horario;
import java.util.List;

public interface HorarioService {

    List<Horario> obtenerTodos();

    List<Horario> obtenerDisponibles();

    Horario obtenerPorId(int id);

    void registrar(Horario horario);

    void actualizar(Horario horario);

    void eliminar(int id);
}

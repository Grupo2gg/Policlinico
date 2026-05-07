package com.policlinico.repository;

import com.policlinico.model.Horario;
import java.util.List;

public interface HorarioDAO {

    List<Horario> findAll();

    List<Horario> findDisponibles();

    Horario findById(int id);

    Horario findByHora(String hora);

    void save(Horario horario);

    void update(Horario horario);

    void delete(int id);
}

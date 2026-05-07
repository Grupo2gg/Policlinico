package com.policlinico.repository;

import com.policlinico.model.Cita;
import java.util.List;

public interface CitaDAO {

    List<Cita> findAll();

    Cita findById(int id);

    void save(Cita cita);

    void update(Cita cita);

    void delete(int id);

    List<Cita> findByUsuarioId(int usuarioId);

    List<Cita> findByHorarioId(int horarioId);

    List<Cita> findByEstado(String estado);
}

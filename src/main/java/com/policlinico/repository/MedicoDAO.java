package com.policlinico.repository;

import com.policlinico.model.Medico;
import java.util.List;

public interface MedicoDAO {

    List<Medico> findAll();

    List<Medico> findActivos();

    Medico findById(int id);

    void save(Medico medico);

    void update(Medico medico);

    void delete(int id);
}

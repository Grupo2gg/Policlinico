package com.policlinico.repository;

import com.policlinico.model.Especialidad;
import java.util.List;

public interface EspecialidadDAO {

    List<Especialidad> findAll();

    Especialidad findById(int id);

    void save(Especialidad especialidad);

    void update(Especialidad especialidad);

    void delete(int id);

    List<Especialidad> findActivas();
}

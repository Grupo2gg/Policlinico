package com.policlinico.repository;

import com.policlinico.model.Especialidad;
import java.util.List;

public interface EspecialidadDAO {

    List<Especialidad> findAll();

    Especialidad findById(int id);

    List<Especialidad> findActivas();
}

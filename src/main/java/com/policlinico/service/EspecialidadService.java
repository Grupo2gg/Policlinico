package com.policlinico.service;

import com.policlinico.model.Especialidad;
import java.util.List;

public interface EspecialidadService {

    List<Especialidad> obtenerTodas();

    List<Especialidad> obtenerActivas();

    Especialidad obtenerPorId(int id);

    List<String> obtenerMedicos();
}

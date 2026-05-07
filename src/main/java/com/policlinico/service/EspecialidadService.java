package com.policlinico.service;

import com.policlinico.model.Especialidad;
import java.util.List;

public interface EspecialidadService {

    List<Especialidad> obtenerActivas();

    List<Especialidad> obtenerTodas();

    Especialidad obtenerPorId(int id);

    void registrar(Especialidad especialidad);

    void actualizar(Especialidad especialidad);

    void eliminar(int id);

    List<String> obtenerMedicos();
}

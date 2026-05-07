package com.policlinico.service;

import com.policlinico.model.Cita;
import com.policlinico.model.Usuario;
import java.util.List;

public interface AdminService {

    int totalUsuarios();

    int totalCitas();

    int totalEspecialidadesActivas();

    int totalMedicos();

    int totalHorarios();

    List<Usuario> obtenerUsuarios();

    List<Cita> obtenerCitas();
}

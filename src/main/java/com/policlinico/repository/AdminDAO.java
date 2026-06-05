package com.policlinico.repository;

import com.policlinico.model.Cita;
import com.policlinico.model.Usuario;
import java.util.List;

public interface AdminDAO {

    int countUsuarios();

    int countCitas();

    int countEspecialidadesActivas();

    int countMedicos();

    int countHorarios();

    List<Usuario> findUsuarios();

    List<Cita> findCitas();
}

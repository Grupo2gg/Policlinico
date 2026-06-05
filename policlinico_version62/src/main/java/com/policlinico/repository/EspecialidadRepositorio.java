package com.policlinico.repository;

import com.policlinico.entity.Especialidad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EspecialidadRepositorio extends JpaRepository<Especialidad, Long> {
    List<Especialidad> findAllByOrderByNombreAsc();
    Optional<Especialidad> findByNombreIgnoreCase(String nombre);
}

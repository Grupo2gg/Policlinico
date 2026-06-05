package com.policlinico.repository;

import com.policlinico.entity.Disponibilidad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DisponibilidadRepositorio extends JpaRepository<Disponibilidad, Long> {
    List<Disponibilidad> findByMedicoId(Long medicoId);
}

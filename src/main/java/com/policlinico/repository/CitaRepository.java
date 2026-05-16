package com.policlinico.repository;

import com.policlinico.model.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Integer> {
    List<Cita> findByUsuarioId(int usuarioId);
    List<Cita> findByHorarioId(int horarioId);
    List<Cita> findByEstado(String estado);
}

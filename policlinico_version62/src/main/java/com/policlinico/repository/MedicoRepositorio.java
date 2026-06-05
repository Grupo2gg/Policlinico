package com.policlinico.repository;

import com.policlinico.entity.Medico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MedicoRepositorio extends JpaRepository<Medico, Long> {
    List<Medico> findByActivoTrue();
    Optional<Medico> findByUsuarioId(Long usuarioId);
    boolean existsByUsuarioId(Long usuarioId);
    boolean existsByEspecialidadId(Long especialidadId);
    boolean existsByUsuarioIdAndIdNot(Long usuarioId, Long id);
}

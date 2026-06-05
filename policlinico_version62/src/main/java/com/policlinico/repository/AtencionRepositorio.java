package com.policlinico.repository;

import com.policlinico.entity.Atencion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AtencionRepositorio extends JpaRepository<Atencion, Long> {
    List<Atencion> findByMedicoId(Long medicoId);
    Optional<Atencion> findByCitaId(Long citaId);
    boolean existsByCitaId(Long citaId);
    boolean existsByCitaIdAndEstado(Long citaId, String estado);
}

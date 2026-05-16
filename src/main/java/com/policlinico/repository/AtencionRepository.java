package com.policlinico.repository;

import com.policlinico.model.Atencion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AtencionRepository extends JpaRepository<Atencion, Integer> {
    Atencion findByCitaId(int citaId);
}

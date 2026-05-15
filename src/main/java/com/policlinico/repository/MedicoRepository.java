package com.policlinico.repository;

import com.policlinico.model.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicoRepository extends JpaRepository<Medico, Integer> {
    List<Medico> findByEstado(String estado);
}

package com.policlinico.repository;

import com.policlinico.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepositorio extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmailIgnoreCaseAndPasswordAndActivoTrue(String email, String password);
}

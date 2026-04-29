package com.policlinico.repository;

import com.policlinico.model.Usuario;
public interface UsuarioDAO {

    Usuario findById(int id);

    Usuario findByEmail(String email);

    void save(Usuario usuario);

    void update(Usuario usuario);

    boolean existsByEmail(String email);
}

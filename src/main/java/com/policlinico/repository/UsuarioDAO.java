package com.policlinico.repository;

import com.policlinico.model.Usuario;
import java.util.List;

public interface UsuarioDAO {

    List<Usuario> findAll();

    Usuario findById(int id);

    Usuario findByEmail(String email);

    void save(Usuario usuario);

    void update(Usuario usuario);

    void delete(int id);

    boolean existsByEmail(String email);
}

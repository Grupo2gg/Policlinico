package com.policlinico.repository.impl;

import com.policlinico.model.Usuario;
import com.policlinico.repository.UsuarioDAO;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Repository;

@Repository
public class UsuarioDAOImpl implements UsuarioDAO {

    private static final List<Usuario> usuarios = new ArrayList<>();

    static {
        usuarios.add(new Usuario(1, "Carlos", "Mendoza", "carlos@policlinico.com", "123456", "999111222", "70112233", "1990-02-10", "ADMIN"));
        usuarios.add(new Usuario(2, "Lucia", "Paredes", "lucia@policlinico.com", "123456", "999222333", "71223344", "1995-06-18", "PACIENTE"));
        usuarios.add(new Usuario(3, "Andrea", "Salas", "andrea@policlinico.com", "123456", "999333444", "72334455", "1988-09-22", "PACIENTE"));
        usuarios.add(new Usuario(4, "Miguel", "Torres", "miguel@policlinico.com", "123456", "999444555", "73445566", "1992-01-05", "RECEPCIONISTA"));
    }

    @Override
    public Usuario findById(int id) {
        return usuarios.stream()
                .filter(usuario -> usuario.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public Usuario findByEmail(String email) {
        return usuarios.stream()
                .filter(usuario -> Objects.equals(usuario.getEmail(), email))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void save(Usuario usuario) {
        int nuevoId = usuarios.stream().mapToInt(Usuario::getId).max().orElse(0) + 1;
        usuario.setId(nuevoId);
        usuarios.add(usuario);
    }

    @Override
    public void update(Usuario usuario) {
        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getId() == usuario.getId()) {
                usuarios.set(i, usuario);
                return;
            }
        }
    }

    @Override
    public boolean existsByEmail(String email) {
        return usuarios.stream()
                .anyMatch(usuario -> usuario.getEmail() != null
                        && email != null
                        && usuario.getEmail().equalsIgnoreCase(email.trim()));
    }
}

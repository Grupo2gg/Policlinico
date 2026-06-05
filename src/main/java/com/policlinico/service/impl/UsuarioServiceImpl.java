package com.policlinico.service.impl;

import com.policlinico.model.Usuario;
import com.policlinico.repository.UsuarioDAO;
import com.policlinico.service.UsuarioService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioDAO usuarioDAO;

    public List<Usuario> obtenerTodos() {
        return usuarioDAO.findAll();
    }

    public Usuario obtenerPorId(int id) {
        return usuarioDAO.findById(id);
    }

    public boolean registrarUsuario(Usuario usuario) {
        if (usuario == null) {
            return false;
        }
        if (usuarioDAO.existsByEmail(usuario.getEmail())) {
            return false;
        }
        usuarioDAO.save(usuario);
        return true;
    }

    public boolean existeEmail(String email) {
        return usuarioDAO.existsByEmail(email);
    }

    public void actualizarUsuario(Usuario usuario) {
        usuarioDAO.update(usuario);
    }

    public void eliminarUsuario(int id) {
        usuarioDAO.delete(id);
    }

    public Usuario login(String email, String password) {
        Usuario usuario = usuarioDAO.findByEmail(email);
        if (usuario != null && usuario.getPassword() != null && usuario.getPassword().equals(password)) {
            return usuario;
        }
        return null;
    }
}

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

    @Override
    public List<Usuario> obtenerTodos() {
        return usuarioDAO.findAll();
    }

    @Override
    public Usuario obtenerPorId(int id) {
        return usuarioDAO.findById(id);
    }

    @Override
    public Usuario obtenerPorEmail(String email) {
        return usuarioDAO.findByEmail(email);
    }

    @Override
    public boolean registrarUsuario(Usuario usuario) {
        if (usuarioDAO.existsByEmail(usuario.getEmail())) {
            return false;
        }
        usuarioDAO.save(usuario);
        return true;
    }

    @Override
    public boolean existeEmail(String email) {
        return usuarioDAO.existsByEmail(email);
    }

    @Override
    public void actualizarUsuario(Usuario usuario) {
        usuarioDAO.update(usuario);
    }

    @Override
    public void eliminarUsuario(int id) {
        usuarioDAO.delete(id);
    }

    @Override
    public Usuario login(String email, String password) {
        Usuario usuario = usuarioDAO.findByEmail(email);
        if (usuario != null && usuario.getPassword() != null && usuario.getPassword().equals(password)) {
            return usuario;
        }
        return null;
    }
}

package com.policlinico.service.impl;

import com.policlinico.model.Usuario;
import com.policlinico.repository.UsuarioDAO;
import com.policlinico.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioDAO usuarioDAO;

    @Override
    public Usuario obtenerPorId(int id) {
        return usuarioDAO.findById(id);
    }

    @Override
    public boolean registrarUsuario(Usuario usuario) {
        validarUsuarioParaRegistro(usuario);
        if (usuarioDAO.existsByEmail(usuario.getEmail())) {
            return false;
        }
        usuario.setEmail(usuario.getEmail().trim());
        usuario.setPassword(usuario.getPassword().trim());
        usuarioDAO.save(usuario);
        return true;
    }

    @Override
    public boolean existeEmail(String email) {
        return usuarioDAO.existsByEmail(email);
    }

    @Override
    public void actualizarUsuario(Usuario usuario) {
        validarUsuarioParaActualizacion(usuario);
        Usuario actual = usuarioDAO.findById(usuario.getId());
        if (actual == null) {
            return;
        }
        actual.setNombre(usuario.getNombre().trim());
        actual.setApellido(usuario.getApellido().trim());
        actual.setTelefono(usuario.getTelefono() == null ? null : usuario.getTelefono().trim());
        usuarioDAO.update(actual);
    }

    @Override
    public Usuario login(String email, String password) {
        if (email == null || email.isBlank() || password == null || password.isBlank()) {
            return null;
        }
        Usuario usuario = usuarioDAO.findByEmail(email.trim());
        if (usuario != null && usuario.getPassword() != null && usuario.getPassword().equals(password.trim())) {
            return usuario;
        }
        return null;
    }

    private void validarUsuarioParaRegistro(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("Los datos del usuario son obligatorios");
        }
        if (usuario.getNombre() == null || usuario.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        if (usuario.getApellido() == null || usuario.getApellido().isBlank()) {
            throw new IllegalArgumentException("El apellido es obligatorio");
        }
        if (usuario.getEmail() == null || usuario.getEmail().isBlank()) {
            throw new IllegalArgumentException("El email es obligatorio");
        }
        if (usuario.getPassword() == null || usuario.getPassword().isBlank()) {
            throw new IllegalArgumentException("La contraseña es obligatoria");
        }
        if (usuario.getDni() == null || usuario.getDni().isBlank()) {
            throw new IllegalArgumentException("El DNI es obligatorio");
        }
    }

    private void validarUsuarioParaActualizacion(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("Los datos del usuario son obligatorios");
        }
        if (usuario.getNombre() == null || usuario.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        if (usuario.getApellido() == null || usuario.getApellido().isBlank()) {
            throw new IllegalArgumentException("El apellido es obligatorio");
        }
    }
}

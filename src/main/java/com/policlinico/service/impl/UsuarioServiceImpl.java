package com.policlinico.service.impl;

import com.policlinico.model.Usuario;
import com.policlinico.repository.UsuarioRepository;
import com.policlinico.service.UsuarioService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Usuario> obtenerTodos() {
        return usuarioRepository.findAll();
    }

    public Usuario obtenerPorId(int id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    public boolean registrarUsuario(Usuario usuario) {
        if (usuario == null) {
            return false;
        }
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            return false;
        }
        usuarioRepository.save(usuario);
        return true;
    }

    public boolean existeEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    public void actualizarUsuario(Usuario usuario) {
        usuarioRepository.save(usuario);
    }

    public void eliminarUsuario(int id) {
        usuarioRepository.deleteById(id);
    }

    public Usuario login(String email, String password) {
        Usuario usuario = usuarioRepository.findByEmail(email).orElse(null);
        if (usuario != null && usuario.getPassword() != null && usuario.getPassword().equals(password)) {
            return usuario;
        }
        return null;
    }
}

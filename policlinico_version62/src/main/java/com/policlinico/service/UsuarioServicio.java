package com.policlinico.service;

import com.policlinico.adapter.UsuarioAdapter;
import com.policlinico.entity.Usuario;
import com.policlinico.model.UsuarioModelo;
import com.policlinico.repository.UsuarioRepositorio;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioServicio {
    private static final String GMAIL_DOMINIO = "@gmail.com";
    private final UsuarioRepositorio usuarioRepositorio;

    public UsuarioServicio(UsuarioRepositorio usuarioRepositorio) {
        this.usuarioRepositorio = usuarioRepositorio;
    }

    public UsuarioModelo iniciarSesion(String email, String password) {
        if (!esGmail(email) || password == null || password.isBlank()) return null;
        return usuarioRepositorio.findByEmailIgnoreCaseAndPasswordAndActivoTrue(email.trim(), password)
                .map(this::modeloConRol)
                .orElse(null);
    }

    public List<UsuarioModelo> listar() {
        return usuarioRepositorio.findAll().stream().map(this::modeloConRol).collect(Collectors.toList());
    }

    public UsuarioModelo buscar(Long id) {
        return usuarioRepositorio.findById(id).map(this::modeloConRol).orElse(null);
    }

    public UsuarioModelo guardar(UsuarioModelo modelo) {
        normalizarUsuario(modelo);
        Usuario usuarioGuardado = usuarioRepositorio.save(UsuarioAdapter.aEntidad(modelo));
        return modeloConRol(usuarioRepositorio.findById(usuarioGuardado.getId()).orElse(usuarioGuardado));
    }

    public UsuarioModelo actualizarPerfil(UsuarioModelo modelo) {
        Usuario usuario = usuarioRepositorio.findById(modelo.getId()).orElse(null);
        if (usuario == null) return null;
        // DNI y email son identificadores inmutables: no se actualizan desde el perfil.
        usuario.setNombre(modelo.getNombre());
        usuario.setApellido(modelo.getApellido());
        usuario.setTelefono(modelo.getTelefono());
        if (modelo.getPassword() != null && !modelo.getPassword().isBlank()) usuario.setPassword(modelo.getPassword());
        if (modelo.getRol() != null && !modelo.getRol().isBlank()) usuario.setRol(modelo.getRol());
        return modeloConRol(usuarioRepositorio.save(usuario));
    }

    public void eliminar(Long id) {
        usuarioRepositorio.deleteById(id);
    }

    public long contar() {
        return usuarioRepositorio.count();
    }

    private UsuarioModelo modeloConRol(Usuario usuario) {
        UsuarioModelo modelo = UsuarioAdapter.aModelo(usuario);
        modelo.setRol(usuario.getRol());
        return modelo;
    }

    private void normalizarUsuario(UsuarioModelo modelo) {
        if (modelo == null) return;
        if (!esGmail(modelo.getEmail())) {
            modelo.setEmail(null);
        } else {
            modelo.setEmail(modelo.getEmail().trim().toLowerCase());
        }
        if (modelo.getActivo() == null) modelo.setActivo(true);
        if (modelo.getRol() == null || modelo.getRol().isBlank()) modelo.setRol("PACIENTE");
    }

    public boolean esGmail(String email) {
        return email != null && email.trim().toLowerCase().endsWith(GMAIL_DOMINIO);
    }

    // Verifica si ya existe un usuario con el mismo DNI
    public boolean existePorDni(String dni) {
        if (dni == null) return false;
        return usuarioRepositorio.findAll().stream()
                .anyMatch(u -> dni.equals(u.getDni()));
    }

    // Verifica si ya existe un usuario con el mismo Email
    public boolean existePorEmail(String email) {
        if (email == null) return false;
        return usuarioRepositorio.findAll().stream()
                .anyMatch(u -> email.trim().equalsIgnoreCase(u.getEmail()));
    }
}

package com.policlinico.service;

import com.policlinico.model.Usuario;
import java.util.List;

public interface UsuarioService {

    List<Usuario> obtenerTodos();

    Usuario obtenerPorId(int id);

    Usuario obtenerPorEmail(String email);

    boolean registrarUsuario(Usuario usuario);

    boolean existeEmail(String email);

    void actualizarUsuario(Usuario usuario);

    void eliminarUsuario(int id);

    Usuario login(String email, String password);
}

package com.policlinico.service;

import com.policlinico.model.Usuario;
public interface UsuarioService {

    Usuario obtenerPorId(int id);

    boolean registrarUsuario(Usuario usuario);

    boolean existeEmail(String email);

    void actualizarUsuario(Usuario usuario);

    Usuario login(String email, String password);
}

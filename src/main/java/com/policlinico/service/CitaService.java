package com.policlinico.service;

import com.policlinico.model.Cita;
import java.util.List;

public interface CitaService {

    Cita obtenerPorId(int id);

    Cita obtenerPorIdDeUsuario(int id, int usuarioId);

    void registrarCita(Cita cita);

    void actualizarCita(Cita cita);

    void cancelarCita(int id, int usuarioId);

    List<Cita> obtenerPorUsuario(int usuarioId);

    List<Cita> obtenerPorEstado(String estado);

    List<String> obtenerHorasDisponibles();
}

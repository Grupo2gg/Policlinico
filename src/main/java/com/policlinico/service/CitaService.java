package com.policlinico.service;

import com.policlinico.model.Cita;
import java.util.List;

public interface CitaService {

    List<Cita> obtenerTodas();

    Cita obtenerPorId(int id);

    void registrarCita(Cita cita);

    void actualizarCita(Cita cita);

    void cancelarCita(int id);

    void confirmarCita(int id);

    void eliminarCita(int id);

    List<Cita> obtenerPorUsuario(int usuarioId);

    List<Cita> obtenerPorEstado(String estado);
}

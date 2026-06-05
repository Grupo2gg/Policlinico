package com.policlinico.service;

import com.policlinico.model.Cita;
import java.util.List;

// Contrato de negocio: el controlador depende de esta interfaz para no hablar
// directamente con el repositorio. Aqui se expresan los casos de uso de citas.
public interface CitaService {

    List<Cita> obtenerTodas();

    Cita obtenerPorId(int id);

    // Devuelve la cita solo si pertenece al usuario autenticado.
    Cita obtenerPorIdDeUsuario(int id, int usuarioId);

    // Caso de uso para registrar una nueva cita.
    void registrarCita(Cita cita);

    // Caso de uso para modificar una cita.
    void actualizarCita(Cita cita);

    void eliminarCita(int id);

    // Caso de uso para cambiar el estado a cancelada.
    void cancelarCita(int id, int usuarioId);

    // Listado que el controlador envía luego a la JSP.
    List<Cita> obtenerPorUsuario(int usuarioId);

    List<Cita> obtenerPorEstado(String estado);

    // Horarios que el controlador carga en el formulario.
    List<String> obtenerHorasDisponibles();
}

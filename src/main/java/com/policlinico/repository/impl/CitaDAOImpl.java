package com.policlinico.repository.impl;

import com.policlinico.model.Cita;
import com.policlinico.model.EstadoCita;
import com.policlinico.model.LogCita;
import com.policlinico.repository.CitaDAO;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class CitaDAOImpl implements CitaDAO {

    private static final List<EstadoCita> estados = new ArrayList<>();
    private static final List<Cita> citas = new ArrayList<>();
    private static final List<LogCita> logs = new ArrayList<>();

    static {
        estados.add(new EstadoCita(1, "PENDIENTE"));
        estados.add(new EstadoCita(2, "CONFIRMADA"));
        estados.add(new EstadoCita(3, "CANCELADA"));

        citas.add(new Cita(1, 2, 1, 1, "Lucia Paredes", "Dermatologia", "Dra. Valeria Soto", "2026-05-03", "08:00", "PENDIENTE", "Consulta por acne", "2026-05-02"));
        citas.add(new Cita(2, 3, 2, 2, "Andrea Salas", "Cirugia Plastica", "Dr. Hector Rivas", "2026-05-03", "09:00", "CONFIRMADA", "Evaluacion postoperatoria", "2026-05-02"));

        logs.add(new LogCita(1, 1, "2026-05-02", "CITA_CREADA"));
        logs.add(new LogCita(2, 2, "2026-05-02", "CITA_CONFIRMADA"));
    }

    @Override
    public List<Cita> findAll() {
        return new ArrayList<>(citas);
    }

    @Override
    public Cita findById(int id) {
        return citas.stream()
                .filter(cita -> cita.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public void save(Cita cita) {
        int nuevoId = citas.stream().mapToInt(Cita::getId).max().orElse(0) + 1;
        cita.setId(nuevoId);
        citas.add(cita);
        logs.add(new LogCita(siguienteLogId(), nuevoId, LocalDate.now().toString(), "CITA_CREADA"));
    }

    @Override
    public void update(Cita cita) {
        for (int i = 0; i < citas.size(); i++) {
            if (citas.get(i).getId() == cita.getId()) {
                citas.set(i, cita);
                logs.add(new LogCita(siguienteLogId(), cita.getId(), LocalDate.now().toString(), "CITA_ACTUALIZADA"));
                return;
            }
        }
    }

    @Override
    public void delete(int id) {
        citas.removeIf(cita -> cita.getId() == id);
    }

    @Override
    public List<Cita> findByUsuarioId(int usuarioId) {
        return citas.stream()
                .filter(cita -> cita.getUsuarioId() == usuarioId)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public List<Cita> findByHorarioId(int horarioId) {
        return citas.stream()
                .filter(cita -> cita.getHorarioId() == horarioId)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public List<Cita> findByEstado(String estado) {
        Integer estadoId = buscarEstadoId(estado);
        if (estadoId == null) {
            return new ArrayList<>();
        }
        return citas.stream()
                .filter(cita -> Objects.equals(cita.getEstadoId(), estadoId))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private Integer buscarEstadoId(String estado) {
        if (estado == null) {
            return null;
        }
        return estados.stream()
                .filter(item -> item.getNombre() != null && item.getNombre().equalsIgnoreCase(estado.trim()))
                .map(EstadoCita::getId)
                .findFirst()
                .orElse(null);
    }

    private int siguienteLogId() {
        return logs.stream().mapToInt(LogCita::getId).max().orElse(0) + 1;
    }
}

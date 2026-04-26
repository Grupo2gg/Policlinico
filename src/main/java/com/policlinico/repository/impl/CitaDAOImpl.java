package com.policlinico.repository.impl;

import com.policlinico.model.Cita;
import com.policlinico.repository.CitaDAO;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class CitaDAOImpl implements CitaDAO {

    private static final List<Cita> citas = new ArrayList<>();
    private static final List<String> horasDisponibles = Arrays.asList(
            "08:00", "09:00", "10:00", "11:00",
            "12:00", "14:00", "15:00", "16:00", "17:00");

    static {
        citas.add(new Cita(1, 2, "Lucia Paredes", "Dermatologia", "Dra. Valeria Soto", "2026-04-26", "08:00", "PENDIENTE", "Consulta por acné", "2026-04-20"));
        citas.add(new Cita(2, 3, "Andrea Salas", "Cirugia Plastica", "Dr. Hector Rivas", "2026-04-26", "09:00", "CONFIRMADA", "Evaluacion postoperatoria", "2026-04-21"));
        citas.add(new Cita(3, 5, "Paula Rios", "Laser y Estetica", "Dr. Alonso Perez", "2026-04-27", "10:30", "CANCELADA", "Tratamiento de manchas", "2026-04-22"));
        citas.add(new Cita(4, 2, "Lucia Paredes", "Dermatologia Pediatrica", "Dra. Elena Ruiz", "2026-04-27", "11:00", "CONFIRMADA", "Control dermatologico infantil", "2026-04-22"));
        citas.add(new Cita(5, 3, "Andrea Salas", "Dermatologia Oncologica", "Dr. Jose Medina", "2026-04-28", "15:00", "PENDIENTE", "Revision de lesion sospechosa", "2026-04-23"));
        citas.add(new Cita(6, 4, "Miguel Torres", "Cirugia Plastica", "Dr. Hector Rivas", "2026-04-29", "16:30", "CANCELADA", "Consulta preoperatoria", "2026-04-24"));
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
    }

    @Override
    public void update(Cita cita) {
        for (int i = 0; i < citas.size(); i++) {
            if (citas.get(i).getId() == cita.getId()) {
                citas.set(i, cita);
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
                .collect(Collectors.toList());
    }

    @Override
    public List<Cita> findByEstado(String estado) {
        return citas.stream()
                .filter(cita -> Objects.equals(cita.getEstado(), estado))
                .collect(Collectors.toList());
    }

    @Override
    public List<String> findHorasDisponibles() {
        return new ArrayList<>(horasDisponibles);
    }
}

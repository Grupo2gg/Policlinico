package com.policlinico.repository.impl;

import com.policlinico.model.Disponibilidad;
import com.policlinico.model.Horario;
import com.policlinico.repository.HorarioDAO;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class HorarioDAOImpl implements HorarioDAO {

    private static final List<Disponibilidad> disponibilidades = new ArrayList<>();
    private static final List<Horario> horarios = new ArrayList<>();

    static {
        disponibilidades.add(new Disponibilidad(1, 1, "2026-05-02", "08:00", "12:00"));
        disponibilidades.add(new Disponibilidad(2, 2, "2026-05-02", "14:00", "17:00"));

        horarios.add(new Horario(1, 1, "08:00", true));
        horarios.add(new Horario(2, 1, "09:00", true));
        horarios.add(new Horario(3, 1, "10:00", true));
        horarios.add(new Horario(4, 1, "10:30", true));
        horarios.add(new Horario(5, 1, "11:00", true));
        horarios.add(new Horario(6, 1, "12:00", true));
        horarios.add(new Horario(7, 2, "14:00", true));
        horarios.add(new Horario(8, 2, "15:00", true));
        horarios.add(new Horario(9, 2, "16:00", true));
        horarios.add(new Horario(10, 2, "16:30", true));
        horarios.add(new Horario(11, 2, "17:00", true));
    }

    @Override
    public List<Horario> findAll() {
        return new ArrayList<>(horarios);
    }

    @Override
    public List<Horario> findDisponibles() {
        return horarios.stream()
                .filter(Horario::isDisponible)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public Horario findById(int id) {
        return horarios.stream()
                .filter(horario -> horario.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public Horario findByHora(String hora) {
        return horarios.stream()
                .filter(horario -> Objects.equals(horario.getHora(), hora))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void save(Horario horario) {
        int nuevoId = horarios.stream().mapToInt(Horario::getId).max().orElse(0) + 1;
        horario.setId(nuevoId);
        horarios.add(horario);
    }

    @Override
    public void update(Horario horario) {
        for (int i = 0; i < horarios.size(); i++) {
            if (horarios.get(i).getId() == horario.getId()) {
                horarios.set(i, horario);
                return;
            }
        }
    }

    @Override
    public void delete(int id) {
        horarios.removeIf(horario -> horario.getId() == id);
    }
}

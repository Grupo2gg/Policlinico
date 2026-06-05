package com.policlinico.repository.impl;

import com.policlinico.model.Medico;
import com.policlinico.repository.MedicoDAO;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class MedicoDAOImpl implements MedicoDAO {

    private static final List<Medico> medicos = new ArrayList<>();

    static {
        medicos.add(new Medico(1, "Dra. Valeria Soto", 1, "Dermatologia", Medico.ESTADO_ACTIVO));
        medicos.add(new Medico(2, "Dr. Hector Rivas", 2, "Cirugia Plastica", Medico.ESTADO_ACTIVO));
        medicos.add(new Medico(3, "Dra. Elena Ruiz", 3, "Dermatologia Pediatrica", Medico.ESTADO_ACTIVO));
        medicos.add(new Medico(4, "Dr. Alonso Perez", 4, "Laser y Estetica", Medico.ESTADO_ACTIVO));
        medicos.add(new Medico(5, "Dr. Jose Medina", 5, "Dermatologia Oncologica", Medico.ESTADO_INACTIVO));
    }

    @Override
    public List<Medico> findAll() {
        return new ArrayList<>(medicos);
    }

    @Override
    public List<Medico> findActivos() {
        return new ArrayList<>(medicos);
    }

    @Override
    public Medico findById(int id) {
        return medicos.stream()
                .filter(medico -> medico.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public void save(Medico medico) {
        int nuevoId = medicos.stream().mapToInt(Medico::getId).max().orElse(0) + 1;
        medico.setId(nuevoId);
        medicos.add(medico);
    }

    @Override
    public void update(Medico medico) {
        for (int i = 0; i < medicos.size(); i++) {
            if (medicos.get(i).getId() == medico.getId()) {
                medicos.set(i, medico);
                return;
            }
        }
    }

    @Override
    public void delete(int id) {
        medicos.removeIf(medico -> medico.getId() == id);
    }
}

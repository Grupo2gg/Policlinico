package com.policlinico.repository.impl;

import com.policlinico.model.Especialidad;
import com.policlinico.repository.EspecialidadDAO;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class EspecialidadDAOImpl implements EspecialidadDAO {

    private static final List<Especialidad> especialidades = new ArrayList<>();

    static {
        especialidades.add(new Especialidad(1, "Dermatologia", "Diagnostico y tratamiento integral de la piel", "Dra. Valeria Soto", "Lunes a Viernes 08:00 - 14:00", true));
        especialidades.add(new Especialidad(2, "Cirugia Plastica", "Procedimientos reconstructivos y esteticos", "Dr. Hector Rivas", "Lunes, Miercoles y Viernes 09:00 - 16:00", true));
        especialidades.add(new Especialidad(3, "Dermatologia Pediatrica", "Atencion dermatologica para ninos y adolescentes", "Dra. Elena Ruiz", "Martes y Jueves 08:00 - 13:00", true));
        especialidades.add(new Especialidad(4, "Laser y Estetica", "Tratamientos laser y procedimientos esteticos no invasivos", "Dr. Alonso Perez", "Lunes a Sabado 10:00 - 18:00", true));
        especialidades.add(new Especialidad(5, "Dermatologia Oncologica", "Deteccion y seguimiento de lesiones cutaneas oncologicas", "Dr. Jose Medina", "Miercoles y Viernes 09:00 - 15:00", false));
    }

    @Override
    public List<Especialidad> findAll() {
        return new ArrayList<>(especialidades);
    }

    @Override
    public Especialidad findById(int id) {
        return especialidades.stream()
                .filter(especialidad -> especialidad.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Especialidad> findActivas() {
        return especialidades.stream()
                .filter(Especialidad::isActiva)
                .collect(Collectors.toList());
    }
}

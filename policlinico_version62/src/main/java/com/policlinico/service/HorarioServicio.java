package com.policlinico.service;

import com.policlinico.adapter.HorarioAdapter;
import com.policlinico.entity.Horario;
import com.policlinico.model.HorarioModelo;
import com.policlinico.repository.HorarioRepositorio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HorarioServicio {
    private final HorarioRepositorio horarioRepositorio;

    public HorarioServicio(HorarioRepositorio horarioRepositorio) {
        this.horarioRepositorio = horarioRepositorio;
    }

    public List<HorarioModelo> listar() {
        return horarioRepositorio.findAll().stream().map(HorarioAdapter::aModelo).collect(Collectors.toList());
    }

    public List<HorarioModelo> listarPorMedico(Long medicoId) {
        return horarioRepositorio.findByMedicoId(medicoId).stream().map(HorarioAdapter::aModelo).collect(Collectors.toList());
    }

    public List<HorarioModelo> listarDisponibles() {
        return horarioRepositorio.findByEstado("DISPONIBLE").stream().map(HorarioAdapter::aModelo).collect(Collectors.toList());
    }

    public HorarioModelo buscar(Long id) {
        return horarioRepositorio.findById(id).map(HorarioAdapter::aModelo).orElse(null);
    }

    @Transactional
    public HorarioModelo guardar(HorarioModelo modelo) {
        if (modelo.getId() != null) {
            Horario actual = horarioRepositorio.findById(modelo.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Horario no encontrado"));

            if ("RESERVADO".equals(actual.getEstado()) && "DISPONIBLE".equals(modelo.getEstado())) {
                throw new IllegalArgumentException("Un horario RESERVADO solo vuelve a DISPONIBLE al cancelar la cita asociada");
            }
        }
        return HorarioAdapter.aModelo(horarioRepositorio.save(HorarioAdapter.aEntidad(modelo)));
    }

    public void eliminar(Long id) {
        horarioRepositorio.deleteById(id);
    }

    public long contar() {
        return horarioRepositorio.count();
    }
}

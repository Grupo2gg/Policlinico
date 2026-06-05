package com.policlinico.service;

import com.policlinico.adapter.DisponibilidadAdapter;
import com.policlinico.model.DisponibilidadModelo;
import com.policlinico.repository.DisponibilidadRepositorio;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DisponibilidadServicio {
    private final DisponibilidadRepositorio disponibilidadRepositorio;

    public DisponibilidadServicio(DisponibilidadRepositorio disponibilidadRepositorio) {
        this.disponibilidadRepositorio = disponibilidadRepositorio;
    }

    public List<DisponibilidadModelo> listarPorMedico(Long medicoId) {
        return disponibilidadRepositorio.findByMedicoId(medicoId).stream().map(DisponibilidadAdapter::aModelo).collect(Collectors.toList());
    }

    public List<DisponibilidadModelo> listar() {
        return disponibilidadRepositorio.findAll().stream().map(DisponibilidadAdapter::aModelo).collect(Collectors.toList());
    }

    public DisponibilidadModelo buscar(Long id) {
        return disponibilidadRepositorio.findById(id).map(DisponibilidadAdapter::aModelo).orElse(null);
    }

    public DisponibilidadModelo guardar(DisponibilidadModelo modelo) {
        return DisponibilidadAdapter.aModelo(disponibilidadRepositorio.save(DisponibilidadAdapter.aEntidad(modelo)));
    }

    public void eliminar(Long id) {
        disponibilidadRepositorio.deleteById(id);
    }
}

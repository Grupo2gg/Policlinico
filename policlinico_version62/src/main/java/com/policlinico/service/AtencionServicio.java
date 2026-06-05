package com.policlinico.service;

import com.policlinico.adapter.AtencionAdapter;
import com.policlinico.entity.Atencion;
import com.policlinico.entity.Cita;
import com.policlinico.model.AtencionModelo;
import com.policlinico.repository.AtencionRepositorio;
import com.policlinico.repository.CitaRepositorio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AtencionServicio {
    private final AtencionRepositorio atencionRepositorio;
    private final CitaRepositorio citaRepositorio;

    public AtencionServicio(AtencionRepositorio atencionRepositorio, CitaRepositorio citaRepositorio) {
        this.atencionRepositorio = atencionRepositorio;
        this.citaRepositorio = citaRepositorio;
    }

    public List<AtencionModelo> listarPorMedico(Long medicoId) {
        return atencionRepositorio.findByMedicoId(medicoId).stream().map(AtencionAdapter::aModelo).collect(Collectors.toList());
    }

    public List<AtencionModelo> listar() {
        return atencionRepositorio.findAll().stream().map(AtencionAdapter::aModelo).collect(Collectors.toList());
    }

    public AtencionModelo buscar(Long id) {
        return atencionRepositorio.findById(id).map(AtencionAdapter::aModelo).orElse(null);
    }

    @Transactional
    public AtencionModelo guardar(AtencionModelo modelo) {
        Cita cita = citaRepositorio.findById(modelo.getCitaId())
                .orElseThrow(() -> new IllegalArgumentException("Cita no encontrada"));

        if (atencionRepositorio.existsByCitaId(cita.getId())) {
            throw new IllegalArgumentException("La cita ya tiene una atencion registrada");
        }

        if (!estadoEs(cita, "CONFIRMADA")) {
            throw new IllegalArgumentException("Solo se puede registrar atencion en citas CONFIRMADAS");
        }

        if (cita.getMedico() == null || !cita.getMedico().getId().equals(modelo.getMedicoId())) {
            throw new IllegalArgumentException("El medico solo puede registrar atenciones de sus propias citas");
        }

        Atencion entidad = AtencionAdapter.aEntidad(modelo);
        entidad.setCita(cita);
        entidad.setEstado("EN_PROCESO");
        entidad.setFechaAtencion(LocalDate.now());

        return AtencionAdapter.aModelo(atencionRepositorio.save(entidad));
    }

    @Transactional
    public void finalizar(Long atencionId, Long medicoId) {
        Atencion atencion = atencionRepositorio.findById(atencionId)
                .orElseThrow(() -> new IllegalArgumentException("Atencion no encontrada"));
        if (atencion.getMedico() == null || !atencion.getMedico().getId().equals(medicoId)) {
            throw new IllegalArgumentException("El medico solo puede finalizar sus propias atenciones");
        }
        if ("FINALIZADA".equals(atencion.getEstado())) {
            throw new IllegalArgumentException("La atencion ya esta FINALIZADA");
        }
        atencion.setEstado("FINALIZADA");
        atencionRepositorio.save(atencion);

        Cita cita = atencion.getCita();
        if (cita != null) {
            cita.setEstado("ATENDIDA");
            citaRepositorio.save(cita);
        }
    }

    @Transactional
    public AtencionModelo guardarAdmin(AtencionModelo modelo) {
        if (modelo.getId() == null) return guardar(modelo);

        Atencion entidad = atencionRepositorio.findById(modelo.getId())
                .orElseThrow(() -> new IllegalArgumentException("Atencion no encontrada"));
        entidad.setDiagnostico(modelo.getDiagnostico());
        entidad.setObservaciones(modelo.getObservaciones());
        if (modelo.getEstado() != null && !modelo.getEstado().isBlank()) {
            if (!estadoAtencionValido(modelo.getEstado())) {
                throw new IllegalArgumentException("Estado de atencion no valido");
            }
            entidad.setEstado(modelo.getEstado());
            if ("FINALIZADA".equals(modelo.getEstado()) && entidad.getCita() != null) {
                entidad.getCita().setEstado("ATENDIDA");
                citaRepositorio.save(entidad.getCita());
            }
        }
        if (modelo.getFechaAtencion() != null) entidad.setFechaAtencion(modelo.getFechaAtencion());
        return AtencionAdapter.aModelo(atencionRepositorio.save(entidad));
    }

    public void eliminar(Long id) {
        atencionRepositorio.deleteById(id);
    }

    public long contar() {
        return atencionRepositorio.count();
    }

    public boolean tieneAtencionParaCita(Long citaId) {
        return citaId != null && atencionRepositorio.existsByCitaId(citaId);
    }

    private boolean estadoEs(Cita cita, String nombre) {
        return cita.getEstado() != null && nombre.equalsIgnoreCase(cita.getEstado());
    }

    private boolean estadoAtencionValido(String estado) {
        return "EN_PROCESO".equals(estado) || "FINALIZADA".equals(estado);
    }
}

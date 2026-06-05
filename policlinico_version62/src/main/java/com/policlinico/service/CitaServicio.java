package com.policlinico.service;

import com.policlinico.adapter.CitaAdapter;
import com.policlinico.entity.Cita;
import com.policlinico.entity.Horario;
import com.policlinico.model.CitaModelo;
import com.policlinico.repository.AtencionRepositorio;
import com.policlinico.repository.CitaRepositorio;
import com.policlinico.repository.HorarioRepositorio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CitaServicio {
    private final CitaRepositorio citaRepositorio;
    private final HorarioRepositorio horarioRepositorio;
    private final AtencionRepositorio atencionRepositorio;

    public CitaServicio(CitaRepositorio citaRepositorio, HorarioRepositorio horarioRepositorio,
                        AtencionRepositorio atencionRepositorio) {
        this.citaRepositorio = citaRepositorio;
        this.horarioRepositorio = horarioRepositorio;
        this.atencionRepositorio = atencionRepositorio;
    }

    public List<CitaModelo> listar() {
        return citaRepositorio.findAll().stream().map(CitaAdapter::aModelo).collect(Collectors.toList());
    }

    public List<CitaModelo> listarPorPaciente(Long pacienteId) {
        return citaRepositorio.findByPacienteId(pacienteId).stream().map(CitaAdapter::aModelo).collect(Collectors.toList());
    }

    public List<CitaModelo> listarPorMedico(Long medicoId) {
        return citaRepositorio.findByMedicoId(medicoId).stream().map(CitaAdapter::aModelo).collect(Collectors.toList());
    }

    public CitaModelo buscar(Long id) {
        return citaRepositorio.findById(id).map(CitaAdapter::aModelo).orElse(null);
    }

    @Transactional
    public CitaModelo guardar(CitaModelo modelo) {
        if (modelo.getHorarioId() == null) {
            throw new IllegalArgumentException("Debe seleccionar un horario disponible");
        }
        if (modelo.getPacienteId() == null) {
            throw new IllegalArgumentException("No se identifico al paciente de la cita");
        }
        // RN-C01 / RN-C02: Bloqueo pesimista del Horario para garantizar atomicidad de la reserva
        Horario horario = horarioRepositorio.findByIdForUpdate(modelo.getHorarioId())
                .orElseThrow(() -> new IllegalArgumentException("Horario no encontrado"));

        if (!"DISPONIBLE".equals(horario.getEstado())) {
            throw new IllegalArgumentException("El horario seleccionado ya no esta DISPONIBLE");
        }

        if (horario.getDisponibilidad() == null || !Boolean.TRUE.equals(horario.getDisponibilidad().getActivo())) {
            throw new IllegalArgumentException("La disponibilidad del medico no esta ACTIVA para ese dia");
        }

        if (horario.getDisponibilidad().getMedico() == null || !Boolean.TRUE.equals(horario.getDisponibilidad().getMedico().getActivo())) {
            throw new IllegalArgumentException("No se puede crear una cita para un medico inactivo");
        }

        if (horario.getDisponibilidad().getMedico().getEspecialidad() == null
                || !"ACTIVA".equals(horario.getDisponibilidad().getMedico().getEspecialidad().getEstado())) {
            throw new IllegalArgumentException("La especialidad seleccionada no acepta nuevas citas");
        }

        horario.setEstado("RESERVADO");
        horarioRepositorio.save(horario);

        if (modelo.getFecha() == null && horario.getDisponibilidad() != null) {
            modelo.setFecha(horario.getDisponibilidad().getFecha());
        }
        if (modelo.getMedicoId() == null && horario.getDisponibilidad() != null && horario.getDisponibilidad().getMedico() != null) {
            modelo.setMedicoId(horario.getDisponibilidad().getMedico().getId());
        }

        Cita cita = CitaAdapter.aEntidad(modelo);
        cita.setMedico(horario.getDisponibilidad().getMedico());
        if (modelo.getId() == null) {
            cita.setEstado("PENDIENTE");
        } else {
            Cita citaExistente = citaRepositorio.findById(modelo.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Cita no encontrada"));
            if (estadoBloqueado(citaExistente)) {
                throw new IllegalArgumentException("No se puede modificar el estado de una cita atendida o con atencion finalizada");
            }
            String estadoSolicitado = modelo.getEstado() == null || modelo.getEstado().isBlank()
                    ? citaExistente.getEstado()
                    : modelo.getEstado();
            if (!estadoCitaValido(estadoSolicitado)) {
                throw new IllegalArgumentException("Estado de cita no valido");
            }
            cita.setEstado(estadoSolicitado);
        }
        cita.setHorario(horario);
        if (cita.getFecha() == null && horario.getDisponibilidad() != null) {
            cita.setFecha(horario.getDisponibilidad().getFecha());
        }

        return CitaAdapter.aModelo(citaRepositorio.save(cita));
    }

    @Transactional
    public void cambiarEstadoPorMedico(Long citaId, Long medicoId, String estado) {
        Cita cita = citaRepositorio.findById(citaId)
                .orElseThrow(() -> new IllegalArgumentException("Cita no encontrada"));
        if (cita.getMedico() == null || !cita.getMedico().getId().equals(medicoId)) {
            throw new IllegalArgumentException("El medico solo puede modificar sus propias citas");
        }
        if (!estadoCitaValido(estado)) {
            throw new IllegalArgumentException("Estado de cita no valido");
        }
        if (estadoBloqueado(cita)) {
            throw new IllegalArgumentException("No se puede cambiar el estado de una cita atendida o con atencion finalizada");
        }
        if ("ATENDIDA".equals(estado)) {
            throw new IllegalArgumentException("La cita solo pasa a ATENDIDA al finalizar una atencion");
        }
        if ("CANCELADA".equals(estado) && !puedeCancelar(cita)) {
            throw new IllegalArgumentException("Solo se puede cancelar una cita PENDIENTE o CONFIRMADA");
        }
        cita.setEstado(estado);
        if ("CANCELADA".equals(estado) && cita.getHorario() != null) {
            cita.getHorario().setEstado("DISPONIBLE");
            horarioRepositorio.save(cita.getHorario());
        }
        citaRepositorio.save(cita);
    }

    @Transactional
    public void cancelar(Long id) {
        citaRepositorio.findById(id).ifPresent(cita -> {
            if (!puedeCancelar(cita)) {
                throw new IllegalArgumentException("Solo se puede cancelar una cita PENDIENTE o CONFIRMADA");
            }
            cita.setEstado("CANCELADA");
            if (cita.getHorario() != null) {
                cita.getHorario().setEstado("DISPONIBLE");
                horarioRepositorio.save(cita.getHorario());
            }
            citaRepositorio.save(cita);
        });
    }

    @Transactional
    public void confirmar(Long id) {
        citaRepositorio.findById(id).ifPresent(cita -> {
            if (estadoBloqueado(cita)) {
                throw new IllegalArgumentException("No se puede confirmar una cita atendida o con atencion finalizada");
            }
            cita.setEstado("CONFIRMADA");
            citaRepositorio.save(cita);
        });
    }

    @Transactional
    public void noConfirmar(Long id) {
        cancelar(id);
    }

    @Transactional
    public void eliminar(Long id) {
        citaRepositorio.findById(id).ifPresent(cita -> {
            if (cita.getHorario() != null) {
                cita.getHorario().setEstado("DISPONIBLE");
                horarioRepositorio.save(cita.getHorario());
            }
            citaRepositorio.deleteById(id);
        });
    }

    public long contar() {
        return citaRepositorio.count();
    }

    private boolean estadoEs(Cita cita, String nombre) {
        return cita.getEstado() != null && nombre.equalsIgnoreCase(cita.getEstado());
    }

    private boolean puedeCancelar(Cita cita) {
        return cita != null
                && (estadoEs(cita, "PENDIENTE") || estadoEs(cita, "CONFIRMADA"))
                && !atencionRepositorio.existsByCitaIdAndEstado(cita.getId(), "FINALIZADA");
    }

    private boolean estadoBloqueado(Cita cita) {
        return cita != null
                && (estadoEs(cita, "ATENDIDA")
                || atencionRepositorio.existsByCitaIdAndEstado(cita.getId(), "FINALIZADA"));
    }

    private boolean estadoCitaValido(String estado) {
        return "PENDIENTE".equals(estado)
                || "CONFIRMADA".equals(estado)
                || "CANCELADA".equals(estado)
                || "ATENDIDA".equals(estado);
    }
}

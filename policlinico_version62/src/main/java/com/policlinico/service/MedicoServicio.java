package com.policlinico.service;

import com.policlinico.adapter.MedicoAdapter;
import com.policlinico.entity.Medico;
import com.policlinico.model.MedicoModelo;
import com.policlinico.repository.CitaRepositorio;
import com.policlinico.repository.HorarioRepositorio;
import com.policlinico.repository.MedicoRepositorio;
import com.policlinico.repository.UsuarioRepositorio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MedicoServicio {
    private final MedicoRepositorio medicoRepositorio;
    private final CitaRepositorio citaRepositorio;
    private final HorarioRepositorio horarioRepositorio;
    private final UsuarioRepositorio usuarioRepositorio;
    private final com.policlinico.repository.EspecialidadRepositorio especialidadRepositorio;

    public MedicoServicio(MedicoRepositorio medicoRepositorio,
                          CitaRepositorio citaRepositorio,
                          HorarioRepositorio horarioRepositorio,
                          UsuarioRepositorio usuarioRepositorio,
                          com.policlinico.repository.EspecialidadRepositorio especialidadRepositorio) {
        this.medicoRepositorio = medicoRepositorio;
        this.citaRepositorio = citaRepositorio;
        this.horarioRepositorio = horarioRepositorio;
        this.usuarioRepositorio = usuarioRepositorio;
        this.especialidadRepositorio = especialidadRepositorio;
    }

    public List<MedicoModelo> listar() {
        return medicoRepositorio.findAll().stream().map(MedicoAdapter::aModelo).collect(Collectors.toList());
    }

    public List<MedicoModelo> listarActivos() {
        return medicoRepositorio.findByActivoTrue().stream()
                .filter(m -> m.getEspecialidad() != null && "ACTIVA".equals(m.getEspecialidad().getEstado()))
                .map(MedicoAdapter::aModelo)
                .collect(Collectors.toList());
    }

    public MedicoModelo buscar(Long id) {
        return medicoRepositorio.findById(id).map(MedicoAdapter::aModelo).orElse(null);
    }

    public MedicoModelo buscarPorUsuario(Long usuarioId) {
        return medicoRepositorio.findByUsuarioId(usuarioId).map(MedicoAdapter::aModelo).orElse(null);
    }

    @Transactional
    public MedicoModelo guardar(MedicoModelo modelo) {
        // Validar: usuario no puede estar vinculado a más de un médico
        if (modelo.getUsuarioId() != null) {
            if (modelo.getId() == null) {
                if (medicoRepositorio.existsByUsuarioId(modelo.getUsuarioId())) {
                    throw new IllegalArgumentException("Este usuario ya está registrado como médico.");
                }
            } else {
                if (medicoRepositorio.existsByUsuarioIdAndIdNot(modelo.getUsuarioId(), modelo.getId())) {
                    throw new IllegalArgumentException("Este usuario ya está registrado como médico.");
                }
            }
        }

        if (modelo.getId() != null) {
            Medico medicoExistente = medicoRepositorio.findById(modelo.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Medico no encontrado"));

            boolean desactivar = Boolean.FALSE.equals(modelo.getActivo());
            if (Boolean.TRUE.equals(medicoExistente.getActivo()) && desactivar) {
                if (tieneCitasFuturas(modelo.getId())) {
                    throw new IllegalArgumentException("No se puede desactivar al medico porque tiene citas futuras activas.");
                }
                try {
                    horarioRepositorio.bloquearHorariosFuturosPorMedico(
                            modelo.getId(), LocalDate.now(), "BLOQUEADO", "DISPONIBLE");
                } catch (Exception ignored) {}
            }

            if (modelo.getEspecialidadId() != null) {
                com.policlinico.entity.Especialidad esp = especialidadRepositorio.findById(modelo.getEspecialidadId())
                        .orElseThrow(() -> new IllegalArgumentException("Especialidad no encontrada"));
                medicoExistente.setEspecialidad(esp);
            }
            medicoExistente.setActivo(Boolean.TRUE.equals(modelo.getActivo()));
            Medico guardado = medicoRepositorio.save(medicoExistente);
            sincronizarActivoUsuario(guardado);
            return MedicoAdapter.aModelo(guardado);
        }

        Medico nuevo = MedicoAdapter.aEntidad(modelo);
        if (modelo.getEspecialidadId() != null) {
            com.policlinico.entity.Especialidad esp = especialidadRepositorio.findById(modelo.getEspecialidadId())
                    .orElseThrow(() -> new IllegalArgumentException("Especialidad no encontrada"));
            nuevo.setEspecialidad(esp);
        }
        Medico guardado = medicoRepositorio.save(nuevo);
        sincronizarActivoUsuario(guardado);
        return MedicoAdapter.aModelo(guardado);
    }

    @Transactional
    public void eliminar(Long id) {
        medicoRepositorio.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Medico no encontrado"));

        if (tieneCitasFuturas(id)) {
            throw new IllegalArgumentException("No se puede eliminar al medico porque tiene citas futuras activas");
        }

        medicoRepositorio.deleteById(id);
    }

    public long contar() {
        return medicoRepositorio.count();
    }

    /**
     * Verifica si el médico tiene citas futuras activas (PENDIENTE o CONFIRMADA).
     * Si los estados no están configurados en BD, devuelve false (no bloquea),
     * en lugar de lanzar una excepción que impida el cambio de estado.
     */
    private boolean tieneCitasFuturas(Long medicoId) {
        try {
            return citaRepositorio.existeCitasFuturasPorMedico(
                    medicoId, LocalDate.now(), "PENDIENTE", "CONFIRMADA");
        } catch (Exception e) {
            return false;
        }
    }

    private void sincronizarActivoUsuario(Medico medico) {
        if (medico == null || medico.getUsuario() == null || medico.getUsuario().getId() == null) return;
        usuarioRepositorio.findById(medico.getUsuario().getId()).ifPresent(usuario -> {
            usuario.setActivo(Boolean.TRUE.equals(medico.getActivo()));
            usuarioRepositorio.save(usuario);
        });
    }
}

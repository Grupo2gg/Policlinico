package com.policlinico.service;

import com.policlinico.adapter.EspecialidadAdapter;
import com.policlinico.entity.Especialidad;
import com.policlinico.model.EspecialidadModelo;
import com.policlinico.repository.CitaRepositorio;
import com.policlinico.repository.EspecialidadRepositorio;
import com.policlinico.repository.HorarioRepositorio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EspecialidadServicio {
    private final EspecialidadRepositorio especialidadRepositorio;
    private final CitaRepositorio citaRepositorio;
    private final HorarioRepositorio horarioRepositorio;

    public EspecialidadServicio(EspecialidadRepositorio especialidadRepositorio,
                               CitaRepositorio citaRepositorio,
                               HorarioRepositorio horarioRepositorio) {
        this.especialidadRepositorio = especialidadRepositorio;
        this.citaRepositorio = citaRepositorio;
        this.horarioRepositorio = horarioRepositorio;
    }

    public List<EspecialidadModelo> listar() {
        return especialidadRepositorio.findAllByOrderByNombreAsc().stream()
                .filter(e -> !"ELIMINADA_LOGICA".equals(e.getEstado()))
                .map(EspecialidadAdapter::aModelo)
                .collect(Collectors.toList());
    }

    public List<EspecialidadModelo> listarActivas() {
        return especialidadRepositorio.findAllByOrderByNombreAsc().stream()
                .filter(e -> "ACTIVA".equals(e.getEstado()))
                .map(EspecialidadAdapter::aModelo)
                .collect(Collectors.toList());
    }

    public EspecialidadModelo buscar(Long id) {
        return especialidadRepositorio.findById(id)
                .filter(e -> !"ELIMINADA_LOGICA".equals(e.getEstado()))
                .map(EspecialidadAdapter::aModelo)
                .orElse(null);
    }

    @Transactional
    public EspecialidadModelo guardar(EspecialidadModelo modelo) {
        // Validar nombre único
        if (modelo.getNombre() != null) {
            especialidadRepositorio.findByNombreIgnoreCase(modelo.getNombre().trim()).ifPresent(existente -> {
                if (!existente.getId().equals(modelo.getId())) {
                    throw new IllegalArgumentException(
                        "Ya existe una especialidad con el nombre \"" + modelo.getNombre().trim() + "\". No se permiten nombres duplicados.");
                }
            });
        }

        if (modelo.getId() != null) {
            Especialidad especialidadExistente = especialidadRepositorio.findById(modelo.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Especialidad no encontrada"));

            String estadoSolicitado = modelo.getEstado() != null ? modelo.getEstado() : especialidadExistente.getEstado();

            if ("ELIMINADA_LOGICA".equals(estadoSolicitado)) {
                throw new IllegalArgumentException("Use la opcion Eliminar para aplicar eliminacion logica de especialidad");
            }

            if ("INACTIVA".equals(estadoSolicitado) && "ACTIVA".equals(especialidadExistente.getEstado())) {
                try {
                    horarioRepositorio.bloquearHorariosFuturosPorEspecialidad(
                            modelo.getId(), LocalDate.now(), "BLOQUEADO", "DISPONIBLE");
                } catch (Exception ignored) { /* sin horarios que bloquear */ }
            }

            // Actualizar directamente sobre la entidad existente para evitar pérdida de estado
            especialidadExistente.setNombre(modelo.getNombre() != null ? modelo.getNombre().trim() : especialidadExistente.getNombre());
            especialidadExistente.setDescripcion(modelo.getDescripcion());
            especialidadExistente.setEstado(estadoSolicitado);
            return EspecialidadAdapter.aModelo(especialidadRepositorio.save(especialidadExistente));
        }

        // Crear nueva
        Especialidad entidad = EspecialidadAdapter.aEntidad(modelo);
        if (modelo.getEstado() != null && !modelo.getEstado().isBlank()) {
            entidad.setEstado(modelo.getEstado());
        }
        return EspecialidadAdapter.aModelo(especialidadRepositorio.save(entidad));
    }

    @Transactional
    public void eliminar(Long id) {
        Especialidad especialidad = especialidadRepositorio.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Especialidad no encontrada"));

        if (tieneCitasFuturas(id)) {
            throw new IllegalArgumentException("No se puede eliminar la especialidad porque tiene citas futuras activas");
        }

        especialidad.setEstado("ELIMINADA_LOGICA");
        especialidadRepositorio.save(especialidad);

        try {
            horarioRepositorio.bloquearHorariosFuturosPorEspecialidad(
                    id, LocalDate.now(), "BLOQUEADO", "DISPONIBLE");
        } catch (Exception ignored) {}
    }

    /**
     * Verifica de forma segura si la especialidad tiene citas futuras activas.
     * Si los estados no están configurados en BD, devuelve false (no bloquea).
     */
    private boolean tieneCitasFuturas(Long especialidadId) {
        try {
            return citaRepositorio.existeCitasFuturasPorEspecialidad(
                    especialidadId, LocalDate.now(), "PENDIENTE", "CONFIRMADA");
        } catch (Exception e) {
            return false;
        }
    }

    public long contar() {
        return especialidadRepositorio.findAll().stream()
                .filter(e -> !"ELIMINADA_LOGICA".equals(e.getEstado()))
                .count();
    }

}

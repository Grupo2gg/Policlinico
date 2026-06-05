package com.policlinico.repository;

import com.policlinico.entity.Horario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

public interface HorarioRepositorio extends JpaRepository<Horario, Long> {

    @Query("SELECT h FROM Horario h JOIN FETCH h.disponibilidad d JOIN FETCH d.medico m JOIN FETCH m.especialidad WHERE h.estado = :estado")
    List<Horario> findByEstado(@Param("estado") String estado);

    @Query("SELECT h FROM Horario h JOIN FETCH h.disponibilidad d JOIN FETCH d.medico m JOIN FETCH m.especialidad WHERE d.medico.id = :medicoId ORDER BY d.fecha, h.horaInicio")
    List<Horario> findByMedicoId(@Param("medicoId") Long medicoId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT h FROM Horario h JOIN FETCH h.disponibilidad d JOIN FETCH d.medico m JOIN FETCH m.especialidad WHERE h.id = :id")
    Optional<Horario> findByIdForUpdate(@Param("id") Long id);

    @Modifying
    @Query("UPDATE Horario h SET h.estado = :bloqueado WHERE h.disponibilidad.medico.especialidad.id = :especialidadId AND h.disponibilidad.fecha >= :fecha AND h.estado = :disponible")
    void bloquearHorariosFuturosPorEspecialidad(
        @Param("especialidadId") Long especialidadId, 
        @Param("fecha") java.time.LocalDate fecha,
        @Param("bloqueado") String bloqueado,
        @Param("disponible") String disponible
    );

    @Modifying
    @Query("UPDATE Horario h SET h.estado = :bloqueado WHERE h.disponibilidad.medico.id = :medicoId AND h.disponibilidad.fecha >= :fecha AND h.estado = :disponible")
    void bloquearHorariosFuturosPorMedico(
        @Param("medicoId") Long medicoId, 
        @Param("fecha") java.time.LocalDate fecha,
        @Param("bloqueado") String bloqueado,
        @Param("disponible") String disponible
    );
}

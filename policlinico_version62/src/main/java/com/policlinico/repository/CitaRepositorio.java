package com.policlinico.repository;

import com.policlinico.entity.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CitaRepositorio extends JpaRepository<Cita, Long> {

    @Query("SELECT c FROM Cita c JOIN FETCH c.paciente JOIN FETCH c.medico m JOIN FETCH m.usuario JOIN FETCH m.especialidad JOIN FETCH c.horario WHERE c.paciente.id = :pacienteId ORDER BY c.fecha DESC, c.id DESC")
    List<Cita> findByPacienteId(@Param("pacienteId") Long pacienteId);

    @Query("SELECT c FROM Cita c JOIN FETCH c.paciente JOIN FETCH c.medico m JOIN FETCH m.usuario JOIN FETCH m.especialidad JOIN FETCH c.horario WHERE c.medico.id = :medicoId ORDER BY c.fecha DESC, c.id DESC")
    List<Cita> findByMedicoId(@Param("medicoId") Long medicoId);

    @Query("SELECT COUNT(c) > 0 FROM Cita c WHERE c.medico.especialidad.id = :especialidadId AND c.fecha >= :fecha AND (c.estado = :pendiente OR c.estado = :confirmada)")
    boolean existeCitasFuturasPorEspecialidad(
        @Param("especialidadId") Long especialidadId, 
        @Param("fecha") java.time.LocalDate fecha,
        @Param("pendiente") String pendiente,
        @Param("confirmada") String confirmada
    );

    @Query("SELECT COUNT(c) > 0 FROM Cita c WHERE c.medico.id = :medicoId AND c.fecha >= :fecha AND (c.estado = :pendiente OR c.estado = :confirmada)")
    boolean existeCitasFuturasPorMedico(
        @Param("medicoId") Long medicoId, 
        @Param("fecha") java.time.LocalDate fecha,
        @Param("pendiente") String pendiente,
        @Param("confirmada") String confirmada
    );
}

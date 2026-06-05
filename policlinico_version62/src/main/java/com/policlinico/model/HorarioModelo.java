package com.policlinico.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HorarioModelo {
    private Long id;
    private Long disponibilidadId;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private Boolean disponible;
    private String estado;
    private Long medicoId;
    private String nombreMedico;
    private LocalDate fecha;
    private String diaSemana;
    /** Texto para vistas JSP (EL no formatea bien java.time). */
    @Getter(AccessLevel.NONE)
    private String fechaTexto;
    @Getter(AccessLevel.NONE)
    private String horaInicioTexto;
    @Getter(AccessLevel.NONE)
    private String horaFinTexto;

    /** Para JSP: siempre devuelve texto aunque EL no use los campos LocalDate/LocalTime. */
    public String getFechaTexto() {
        if (fechaTexto != null && !fechaTexto.isBlank()) {
            return fechaTexto;
        }
        return fecha == null ? "" : fecha.toString();
    }

    public String getHoraInicioTexto() {
        if (horaInicioTexto != null && !horaInicioTexto.isBlank()) {
            return horaInicioTexto;
        }
        return horaInicio == null ? "" : String.format("%02d:%02d", horaInicio.getHour(), horaInicio.getMinute());
    }

    public String getHoraFinTexto() {
        if (horaFinTexto != null && !horaFinTexto.isBlank()) {
            return horaFinTexto;
        }
        return horaFin == null ? "" : String.format("%02d:%02d", horaFin.getHour(), horaFin.getMinute());
    }
}

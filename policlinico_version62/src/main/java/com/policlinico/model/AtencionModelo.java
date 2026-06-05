package com.policlinico.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AtencionModelo {
    private Long id;
    private Long citaId;
    private Long medicoId;
    private String diagnostico;
    private String observaciones;
    private LocalDate fechaAtencion;
    private String estado;
    private String paciente;
    private String nombreMedico;
}

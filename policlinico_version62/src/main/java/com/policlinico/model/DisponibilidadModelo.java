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
public class DisponibilidadModelo {
    private Long id;
    private Long medicoId;
    private LocalDate fecha;
    private String diaSemana;
    private Boolean activo;
    private String nombreMedico;
}

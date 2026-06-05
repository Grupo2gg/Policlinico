package com.policlinico.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MedicoModelo {
    private Long id;
    private Long usuarioId;
    private Long especialidadId;
    private String cmp;
    private Boolean activo;
    private String nombre;
    private String apellido;
    private String email;
    private String especialidad;
}

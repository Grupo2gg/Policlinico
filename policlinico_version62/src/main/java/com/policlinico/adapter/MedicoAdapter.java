package com.policlinico.adapter;

import com.policlinico.entity.Especialidad;
import com.policlinico.entity.Medico;
import com.policlinico.entity.Usuario;
import com.policlinico.model.MedicoModelo;

public class MedicoAdapter {
    public static MedicoModelo aModelo(Medico entidad) {
        if (entidad == null) return null;
        MedicoModelo modelo = new MedicoModelo();
        modelo.setId(entidad.getId());
        modelo.setUsuarioId(entidad.getUsuario().getId());
        modelo.setEspecialidadId(entidad.getEspecialidad().getId());
        modelo.setCmp(entidad.getCmp());
        modelo.setActivo(entidad.getActivo());
        modelo.setNombre(entidad.getUsuario().getNombre());
        modelo.setApellido(entidad.getUsuario().getApellido());
        modelo.setEmail(entidad.getUsuario().getEmail());
        modelo.setEspecialidad(entidad.getEspecialidad().getNombre());
        return modelo;
    }

    public static Medico aEntidad(MedicoModelo modelo) {
        if (modelo == null) return null;
        Usuario usuario = new Usuario();
        Especialidad especialidad = new Especialidad();
        usuario.setId(modelo.getUsuarioId());
        especialidad.setId(modelo.getEspecialidadId());
        Medico entidad = new Medico();
        entidad.setId(modelo.getId());
        entidad.setUsuario(usuario);
        entidad.setEspecialidad(especialidad);
        if (modelo.getCmp() != null && !modelo.getCmp().isBlank()) {
            entidad.setCmp(modelo.getCmp());
        }
        entidad.setActivo(Boolean.TRUE.equals(modelo.getActivo()));
        return entidad;
    }
}

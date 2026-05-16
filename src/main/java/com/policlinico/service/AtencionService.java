package com.policlinico.service;

import com.policlinico.model.Atencion;
import java.util.List;

public interface AtencionService {
    Atencion registrar(Atencion atencion);
    Atencion obtenerPorCita(int citaId);
}

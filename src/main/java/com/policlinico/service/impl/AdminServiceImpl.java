package com.policlinico.service.impl;

import com.policlinico.model.Cita;
import com.policlinico.model.Usuario;
import com.policlinico.repository.AdminDAO;
import com.policlinico.service.AdminService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminDAO adminDAO;

    @Override
    public int totalUsuarios() {
        return adminDAO.countUsuarios();
    }

    @Override
    public int totalCitas() {
        return adminDAO.countCitas();
    }

    @Override
    public int totalEspecialidadesActivas() {
        return adminDAO.countEspecialidadesActivas();
    }

    @Override
    public int totalMedicos() {
        return adminDAO.countMedicos();
    }

    @Override
    public int totalHorarios() {
        return adminDAO.countHorarios();
    }

    @Override
    public List<Usuario> obtenerUsuarios() {
        return adminDAO.findUsuarios();
    }

    @Override
    public List<Cita> obtenerCitas() {
        return adminDAO.findCitas();
    }
}

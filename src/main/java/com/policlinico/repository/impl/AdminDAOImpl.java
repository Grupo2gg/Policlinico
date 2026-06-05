package com.policlinico.repository.impl;

import com.policlinico.model.Cita;
import com.policlinico.model.Usuario;
import com.policlinico.repository.AdminDAO;
import com.policlinico.repository.CitaDAO;
import com.policlinico.repository.EspecialidadDAO;
import com.policlinico.repository.HorarioDAO;
import com.policlinico.repository.MedicoDAO;
import com.policlinico.repository.UsuarioDAO;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class AdminDAOImpl implements AdminDAO {

    @Autowired
    private UsuarioDAO usuarioDAO;

    @Autowired
    private CitaDAO citaDAO;

    @Autowired
    private EspecialidadDAO especialidadDAO;

    @Autowired
    private MedicoDAO medicoDAO;

    @Autowired
    private HorarioDAO horarioDAO;

    @Override
    public int countUsuarios() {
        return usuarioDAO.findAll().size();
    }

    @Override
    public int countCitas() {
        return citaDAO.findAll().size();
    }

    @Override
    public int countEspecialidadesActivas() {
        return especialidadDAO.findActivas().size();
    }

    @Override
    public int countMedicos() {
        return medicoDAO.findAll().size();
    }

    @Override
    public int countHorarios() {
        return horarioDAO.findAll().size();
    }

    @Override
    public List<Usuario> findUsuarios() {
        return usuarioDAO.findAll();
    }

    @Override
    public List<Cita> findCitas() {
        return citaDAO.findAll();
    }
}

package com.policlinico.service.impl;

import com.policlinico.model.Cita;
import com.policlinico.model.Usuario;
import com.policlinico.repository.CitaRepository;
import com.policlinico.repository.EspecialidadRepository;
import com.policlinico.repository.HorarioRepository;
import com.policlinico.repository.MedicoRepository;
import com.policlinico.repository.UsuarioRepository;
import com.policlinico.service.AdminService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private CitaRepository citaRepository;
    @Autowired
    private EspecialidadRepository especialidadRepository;
    @Autowired
    private MedicoRepository medicoRepository;
    @Autowired
    private HorarioRepository horarioRepository;

    @Override
    public int totalUsuarios() {
        return (int) usuarioRepository.count();
    }

    @Override
    public int totalCitas() {
        return (int) citaRepository.count();
    }

    @Override
    public int totalEspecialidadesActivas() {
        return especialidadRepository.findByActivaTrue().size();
    }

    @Override
    public int totalMedicos() {
        return (int) medicoRepository.count();
    }

    @Override
    public int totalHorarios() {
        return (int) horarioRepository.count();
    }

    @Override
    public List<Usuario> obtenerUsuarios() {
        return usuarioRepository.findAll();
    }

    @Override
    public List<Cita> obtenerCitas() {
        return citaRepository.findAll();
    }
}

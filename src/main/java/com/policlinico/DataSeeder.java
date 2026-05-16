package com.policlinico;

import com.policlinico.model.Especialidad;
import com.policlinico.model.Medico;
import com.policlinico.model.Horario;
import com.policlinico.model.Usuario;
import com.policlinico.repository.EspecialidadRepository;
import com.policlinico.repository.MedicoRepository;
import com.policlinico.repository.HorarioRepository;
import com.policlinico.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EspecialidadRepository especialidadRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private HorarioRepository horarioRepository;

    @Override
    public void run(String... args) throws Exception {
        // Verifica si el administrador ya existe para no duplicarlo
        if (!usuarioRepository.existsByEmail("admin@policlinico.com")) {
            Usuario admin = new Usuario();
            admin.setNombre("Administrador");
            admin.setApellido("Principal");
            admin.setEmail("admin@policlinico.com");
            admin.setPassword("admin123");
            admin.setTelefono("999999999");
            admin.setDni("12345678");
            admin.setFechaNacimiento("1990-01-01");
            admin.setRol(Usuario.ROL_ADMIN);
            
            usuarioRepository.save(admin);
            System.out.println("✅ Administrador creado exitosamente.");
        }

        // Sembrar Especialidades
        if (especialidadRepository.count() == 0) {
            especialidadRepository.save(new Especialidad(0, "Dermatologia", "Diagnostico y tratamiento integral de la piel", "Dra. Valeria Soto", "Lunes a Viernes 08:00-14:00", true));
            especialidadRepository.save(new Especialidad(0, "Cirugia Plastica", "Procedimientos reconstructivos y esteticos", "Dr. Hector Rivas", "Lunes, Miercoles 09:00-16:00", true));
            especialidadRepository.save(new Especialidad(0, "Dermatologia Pediatrica", "Atencion dermatologica para ninos", "Dra. Elena Ruiz", "Martes y Jueves 08:00-13:00", true));
            System.out.println("✅ Especialidades iniciales creadas.");
        }

        // Sembrar Medicos
        if (medicoRepository.count() == 0) {
            medicoRepository.save(new Medico(0, "Valeria Soto", 1, "Dermatologia", Medico.ESTADO_ACTIVO));
            medicoRepository.save(new Medico(0, "Hector Rivas", 2, "Cirugia Plastica", Medico.ESTADO_ACTIVO));
            System.out.println("✅ Medicos iniciales creados.");
        }

        // Sembrar Horarios
        if (horarioRepository.count() == 0) {
            horarioRepository.save(new Horario(0, 1, "08:00", true));
            horarioRepository.save(new Horario(0, 1, "09:00", true));
            horarioRepository.save(new Horario(0, 1, "10:00", true));
            horarioRepository.save(new Horario(0, 1, "11:00", true));
            System.out.println("✅ Horarios iniciales creados.");
        }
    }
}

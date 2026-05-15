package com.policlinico;

import com.policlinico.model.Usuario;
import com.policlinico.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

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
            System.out.println("✅ Administrador creado exitosamente con el email: admin@policlinico.com");
        }
    }
}

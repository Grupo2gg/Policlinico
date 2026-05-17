package com.policlinico.controller;

import com.policlinico.model.Usuario;
import com.policlinico.service.EspecialidadService;
import com.policlinico.service.UsuarioService;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping
public class UsuarioController {

    // Servicio de negocio para autenticacion y gestion de usuarios.
    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private EspecialidadService especialidadService;

    @GetMapping("/")
    public String inicio(HttpSession session) {
        Usuario usuario = obtenerUsuarioSesion(session);
        if (usuario == null) {
            return "redirect:/login";
        }
        return redirigirSegunRol(usuario);
    }

    @GetMapping("/login")
    public String mostrarLogin(HttpSession session) {
        if (session.getAttribute("usuario") != null) {
            return redirigirSegunRol((Usuario) session.getAttribute("usuario"));
        }
        return "login";
    }

    @PostMapping("/login")
    public String login(String email, String password, Model model, HttpSession session) {
        // La vista manda credenciales; el controlador las delega al servicio.
        Usuario usuario = usuarioService.login(email, password);
        if (usuario != null) {
            // La sesion crea el enlace entre autenticacion y el resto de controladores.
            session.setAttribute("usuario", usuario);
            return redirigirSegunRol(usuario);
        }
        model.addAttribute("email", email);
        model.addAttribute("error", "Credenciales incorrectas");
        return "login";
    }

    @GetMapping("/registro")
    public String mostrarRegistro(HttpSession session) {
        if (session.getAttribute("usuario") != null) {
            return "redirect:/main";
        }
        return "registro";
    }

    @PostMapping("/registro")
    public String procesarRegistro(
            @ModelAttribute Usuario usuario,
            @RequestParam("password") String password,
            @RequestParam("confirmar") String confirmar,
            Model model) {

        // Estas validaciones son de entrada del formulario antes de registrar.
        if (!password.trim().equals(confirmar.trim())) {
            model.addAttribute("error", "Las contraseñas no coinciden");
            model.addAttribute("usuario", usuario);
            return "registro";
        }

        if (password.trim().length() < 6) {
            model.addAttribute("error",
                    "La contraseña debe tener mínimo 6 caracteres");
            model.addAttribute("usuario", usuario);
            return "registro";
        }

        if (usuario.getDni() == null || !usuario.getDni().matches("[0-9]{8}")) {
            model.addAttribute("error",
                    "El DNI debe tener exactamente 8 dígitos numéricos");
            model.addAttribute("usuario", usuario);
            return "registro";
        }

        if (usuarioService.existeEmail(usuario.getEmail())) {
            model.addAttribute("error",
                    "Ya existe una cuenta con ese email");
            model.addAttribute("usuario", usuario);
            return "registro";
        }

        usuario.setPassword(password.trim());
        try {
            // El registro real se delega al servicio para centralizar reglas.
            if (!usuarioService.registrarUsuario(usuario)) {
                model.addAttribute("error", "Ya existe una cuenta con ese email");
                model.addAttribute("usuario", usuario);
                return "registro";
            }
        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("usuario", usuario);
            return "registro";
        }

        return "redirect:/login";
    }

    @GetMapping("/main")
    public String main(HttpSession session, Model model) {
        Usuario usuario = obtenerUsuarioSesion(session);
        if (usuario == null) {
            return "redirect:/login";
        }
        model.addAttribute("usuario", usuario);
        model.addAttribute("especialidades", especialidadService.obtenerActivas());
        return "main";
    }

    @GetMapping("/contacto")
    public String contacto() {
        return "contacto";
    }

    @GetMapping("/publicidad")
    public String publicidad(Model model) {
        List<Map<String, String>> servicios = List.of(
                Map.of("nombre", "Consulta Dermatológica",
                        "descripcion", "Evaluación completa de tu piel",
                        "precio", "Desde S/ 80"),
                Map.of("nombre", "Tratamiento con Láser",
                        "descripcion", "Eliminación de manchas y rejuvenecimiento",
                        "precio", "Desde S/ 150"),
                Map.of("nombre", "Cirugía Estética",
                        "descripcion", "Procedimientos con tecnología moderna",
                        "precio", "Desde S/ 500"),
                Map.of("nombre", "Peeling Facial",
                        "descripcion", "Renovación celular y limpieza profunda",
                        "precio", "Desde S/ 120")
        );
        model.addAttribute("servicios", servicios);
        model.addAttribute("especialidades", especialidadService.obtenerActivas());
        return "publicidad";
    }

    @GetMapping("/citas")
    public String citas(HttpSession session) {
        Usuario usuario = obtenerUsuarioSesion(session);
        if (usuario == null) {
            return "redirect:/login";
        }
        if (!"PACIENTE".equals(usuario.getRol())) {
            return "redirect:/login";
        }
        return "redirect:/cita/list";
    }

    @GetMapping("/perfil")
    public String perfil(HttpSession session, Model model, String exito) {
        // Se toma el usuario de sesion porque es la fuente confiable en la capa web.
        Usuario usuario = obtenerUsuarioSesion(session);
        if (usuario == null) {
            return "redirect:/login";
        }
        if (!"PACIENTE".equals(usuario.getRol())) {
            return "redirect:/login";
        }
        model.addAttribute("usuario", usuario);
        if (exito != null) {
            model.addAttribute("exito", "Datos actualizados correctamente");
        }
        return "perfil";
    }

    @PostMapping("/perfil/actualizar")
    public String actualizarPerfil(@ModelAttribute Usuario usuario, HttpSession session, Model model) {
        Usuario usuarioSesion = obtenerUsuarioSesion(session);
        if (usuarioSesion == null) {
            return "redirect:/login";
        }
        if (!"PACIENTE".equals(usuarioSesion.getRol())) {
            return "redirect:/login";
        }
        usuario.setId(usuarioSesion.getId());
        try {
            usuarioService.actualizarUsuario(usuario);
        } catch (IllegalArgumentException ex) {
            Usuario actual = usuarioService.obtenerPorId(usuarioSesion.getId());
            model.addAttribute("usuario", actual);
            model.addAttribute("error", ex.getMessage());
            return "perfil";
        }
        session.setAttribute("usuario", usuarioService.obtenerPorId(usuario.getId()));
        return "redirect:/perfil?exito=1";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @PostMapping("/logout")
    public String logoutPost(HttpSession session) {
        Usuario usuario = obtenerUsuarioSesion(session);
        if (usuario == null) {
            return "redirect:/login";
        }
        return cerrarSesion(session);
    }

    private String cerrarSesion(HttpSession session) {
        // Rompe el enlace de autenticacion entre la capa web y los datos del usuario.
        session.invalidate();
        return "redirect:/login";
    }

    private Usuario obtenerUsuarioSesion(HttpSession session) {
        return (Usuario) session.getAttribute("usuario");
    }

    private String redirigirSegunRol(Usuario usuario) {
        if ("ADMIN".equals(usuario.getRol())) {
            return "redirect:/admin/dashboard";
        }
        if ("MEDICO".equals(usuario.getRol())) {
            return "redirect:/medico/atenciones";
        }
        return "redirect:/citas";
    }
}

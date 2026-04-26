package com.policlinico.controller;

import com.policlinico.model.Cita;
import com.policlinico.model.Servicio;
import com.policlinico.model.Usuario;
import com.policlinico.service.CitaService;
import com.policlinico.service.EspecialidadService;
import com.policlinico.service.UsuarioService;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private EspecialidadService especialidadService;

    @Autowired
    private CitaService citaService;

    @GetMapping("/login")
    public String mostrarLogin(HttpSession session) {
        if (session.getAttribute("usuario") != null) {
            return "redirect:/main";
        }
        return "login";
    }

    @PostMapping("/login")
    public String login(String email, String password, Model model, HttpSession session) {
        Usuario usuario = usuarioService.login(email, password);
        if (usuario != null) {
            session.setAttribute("usuario", usuario);
            return "redirect:/main";
        }
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
        usuarioService.registrarUsuario(usuario);

        return "redirect:/login";
    }

    @GetMapping("/main")
    public String main(HttpSession session, Model model) {
        if (haySesionActiva(session)) {
            model.addAttribute("usuario", obtenerUsuarioSesion(session));
        }
        model.addAttribute("especialidades", especialidadService.obtenerActivas());
        return "main";
    }

    @GetMapping("/contacto")
    public String contacto() {
        return "contacto";
    }

    @GetMapping("/publicidad")
    public String publicidad(Model model) {
        List<Servicio> servicios = new ArrayList<>();
        servicios.add(new Servicio("Consulta Dermatológica",
                "Evaluación completa de tu piel", "Desde S/ 80"));
        servicios.add(new Servicio("Tratamiento con Láser",
                "Eliminación de manchas y rejuvenecimiento", "Desde S/ 150"));
        servicios.add(new Servicio("Cirugía Estética",
                "Procedimientos con tecnología moderna", "Desde S/ 500"));
        servicios.add(new Servicio("Peeling Facial",
                "Renovación celular y limpieza profunda", "Desde S/ 120"));
        model.addAttribute("servicios", servicios);
        return "publicidad";
    }

    @GetMapping("/gestion")
    public String gestion(HttpSession session, Model model) {
        if (!haySesionActiva(session)) {
            return "redirect:/login";
        }
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        List<Cita> ultimas = citaService.obtenerPorUsuario(usuario.getId());
        model.addAttribute("ultimasCitas", ultimas);
        model.addAttribute("totalCitas", ultimas.size());
        return "gestion";
    }

    @GetMapping("/perfil")
    public String perfil(HttpSession session, Model model, String exito) {
        Usuario usuario = obtenerUsuarioSesion(session);
        if (usuario == null) {
            return "redirect:/login";
        }
        model.addAttribute("usuario", usuario);
        if (exito != null) {
            model.addAttribute("exito", true);
        }
        return "perfil";
    }

    @PostMapping("/perfil/actualizar")
    public String actualizarPerfil(@ModelAttribute Usuario usuario, HttpSession session) {
        Usuario usuarioSesion = obtenerUsuarioSesion(session);
        if (usuarioSesion == null) {
            return "redirect:/login";
        }
        usuario.setId(usuarioSesion.getId());
        usuarioService.actualizarUsuario(usuario);
        session.setAttribute("usuario", usuarioService.obtenerPorId(usuario.getId()));
        return "redirect:/perfil?exito=1";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    private boolean haySesionActiva(HttpSession session) {
        return session.getAttribute("usuario") != null;
    }

    private Usuario obtenerUsuarioSesion(HttpSession session) {
        return (Usuario) session.getAttribute("usuario");
    }
}

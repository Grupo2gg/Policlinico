package com.policlinico.controller;

import com.policlinico.model.Cita;
import com.policlinico.model.Usuario;
import com.policlinico.service.AdminService;
import com.policlinico.service.CitaService;
import com.policlinico.service.EspecialidadService;
import com.policlinico.service.MedicoService;
import com.policlinico.service.UsuarioService;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private CitaService citaService;

    @Autowired
    private EspecialidadService especialidadService;

    @Autowired
    private MedicoService medicoService;

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Usuario usuario = obtenerUsuarioSesion(session);
        if (usuario == null) {
            return "redirect:/login";
        }
        if (!"ADMIN".equals(usuario.getRol())) {
            return "redirect:/login";
        }

        model.addAttribute("usuario", usuario);
        model.addAttribute("totalUsuarios", adminService.totalUsuarios());
        model.addAttribute("totalCitas", adminService.totalCitas());
        model.addAttribute("totalEspecialidades", adminService.totalEspecialidadesActivas());
        model.addAttribute("totalMedicos", adminService.totalMedicos());
        model.addAttribute("totalHorarios", adminService.totalHorarios());
        return "admin/dashboard";
    }

    @GetMapping("/usuarios")
    public String usuarios(HttpSession session, Model model) {
        Usuario usuario = obtenerUsuarioSesion(session);
        if (usuario == null) {
            return "redirect:/login";
        }
        if (!"ADMIN".equals(usuario.getRol())) {
            return "redirect:/login";
        }

        cargarUsuarios(model, usuario, new Usuario(), false);
        return "admin/usuarios";
    }

    @GetMapping("/usuarios/editar/{id}")
    public String editarUsuario(@PathVariable int id, HttpSession session, Model model,
                                RedirectAttributes redirectAttributes) {
        Usuario usuario = obtenerUsuarioSesion(session);
        if (usuario == null) {
            return "redirect:/login";
        }
        if (!"ADMIN".equals(usuario.getRol())) {
            return "redirect:/login";
        }

        Usuario usuarioForm = usuarioService.obtenerPorId(id);
        if (usuarioForm == null) {
            redirectAttributes.addFlashAttribute("mensaje", "El usuario solicitado no fue encontrado.");
            return "redirect:/admin/usuarios";
        }
        cargarUsuarios(model, usuario, usuarioForm, true);
        return "admin/usuarios";
    }

    @PostMapping("/usuarios/guardar")
    public String guardarUsuario(@ModelAttribute Usuario usuarioForm, HttpSession session) {
        Usuario usuario = obtenerUsuarioSesion(session);
        if (usuario == null) {
            return "redirect:/login";
        }
        if (!"ADMIN".equals(usuario.getRol())) {
            return "redirect:/login";
        }

        usuarioService.registrarUsuario(usuarioForm);
        return "redirect:/admin/usuarios";
    }

    @PostMapping("/usuarios/actualizar")
    public String actualizarUsuario(@ModelAttribute Usuario usuarioForm, HttpSession session) {
        Usuario usuario = obtenerUsuarioSesion(session);
        if (usuario == null) {
            return "redirect:/login";
        }
        if (!"ADMIN".equals(usuario.getRol())) {
            return "redirect:/login";
        }

        usuarioService.actualizarUsuario(usuarioForm);
        return "redirect:/admin/usuarios";
    }

    @GetMapping("/usuarios/eliminar/{id}")
    public String eliminarUsuario(@PathVariable int id, HttpSession session) {
        Usuario usuario = obtenerUsuarioSesion(session);
        if (usuario == null) {
            return "redirect:/login";
        }
        if (!"ADMIN".equals(usuario.getRol())) {
            return "redirect:/login";
        }

        usuarioService.eliminarUsuario(id);
        return "redirect:/admin/usuarios";
    }

    @GetMapping("/citas")
    public String citas(HttpSession session, Model model) {
        Usuario usuario = obtenerUsuarioSesion(session);
        if (usuario == null) {
            return "redirect:/login";
        }
        if (!"ADMIN".equals(usuario.getRol())) {
            return "redirect:/login";
        }

        cargarCitas(model, usuario, new Cita(), false);
        return "admin/citas";
    }

    @GetMapping("/citas/editar/{id}")
    public String editarCita(@PathVariable int id, HttpSession session, Model model,
                             RedirectAttributes redirectAttributes) {
        Usuario usuario = obtenerUsuarioSesion(session);
        if (usuario == null) {
            return "redirect:/login";
        }
        if (!"ADMIN".equals(usuario.getRol())) {
            return "redirect:/login";
        }

        Cita cita = citaService.obtenerPorId(id);
        if (cita == null) {
            redirectAttributes.addFlashAttribute("mensaje", "La cita solicitada no fue encontrada.");
            return "redirect:/admin/citas";
        }
        cargarCitas(model, usuario, cita, true);
        return "admin/citas";
    }

    @PostMapping("/citas/guardar")
    public String guardarCita(@ModelAttribute Cita cita, BindingResult bindingResult,
                              HttpSession session, Model model) {
        Usuario usuario = obtenerUsuarioSesion(session);
        if (usuario == null) {
            return "redirect:/login";
        }
        if (!"ADMIN".equals(usuario.getRol())) {
            return "redirect:/login";
        }

        if (bindingResult.hasErrors() || !pacienteRegistradoValido(cita.getUsuarioId())) {
            model.addAttribute("mensaje", "Debes seleccionar un paciente registrado para crear la cita.");
            cargarCitas(model, usuario, cita, false);
            return "admin/citas";
        }

        completarPaciente(cita);
        citaService.registrarCita(cita);
        return "redirect:/admin/citas";
    }

    @PostMapping("/citas/actualizar")
    public String actualizarCita(@ModelAttribute Cita cita, BindingResult bindingResult,
                                 HttpSession session, Model model) {
        Usuario usuario = obtenerUsuarioSesion(session);
        if (usuario == null) {
            return "redirect:/login";
        }
        if (!"ADMIN".equals(usuario.getRol())) {
            return "redirect:/login";
        }

        if (bindingResult.hasErrors() || !pacienteRegistradoValido(cita.getUsuarioId())) {
            model.addAttribute("mensaje", "Debes seleccionar un paciente registrado para actualizar la cita.");
            cargarCitas(model, usuario, cita, true);
            return "admin/citas";
        }

        completarPaciente(cita);
        citaService.actualizarCita(cita);
        return "redirect:/admin/citas";
    }

    @GetMapping("/citas/eliminar/{id}")
    public String eliminarCita(@PathVariable int id, HttpSession session) {
        Usuario usuario = obtenerUsuarioSesion(session);
        if (usuario == null) {
            return "redirect:/login";
        }
        if (!"ADMIN".equals(usuario.getRol())) {
            return "redirect:/login";
        }

        citaService.eliminarCita(id);
        return "redirect:/admin/citas";
    }

    private void cargarUsuarios(Model model, Usuario usuario, Usuario usuarioForm, boolean modoEdicion) {
        model.addAttribute("usuario", usuario);
        model.addAttribute("usuarios", usuarioService.obtenerTodos());
        model.addAttribute("usuarioForm", usuarioForm);
        model.addAttribute("modoEdicion", modoEdicion);
    }

    private void cargarCitas(Model model, Usuario usuario, Cita cita, boolean modoEdicion) {
        model.addAttribute("usuario", usuario);
        model.addAttribute("citas", citaService.obtenerTodas());
        model.addAttribute("cita", cita);
        model.addAttribute("modoEdicion", modoEdicion);
        model.addAttribute("usuarios", obtenerPacientesRegistrados());
        model.addAttribute("especialidades", especialidadService.obtenerActivas());
        model.addAttribute("medicos", medicoService.obtenerActivos());
        model.addAttribute("horas", citaService.obtenerHorasDisponibles());
    }

    private void completarPaciente(Cita cita) {
        Usuario paciente = usuarioService.obtenerPorId(cita.getUsuarioId());
        if (paciente != null) {
            cita.setNombrePaciente((paciente.getNombre() + " " + paciente.getApellido()).trim());
        }
    }

    private boolean pacienteRegistradoValido(int usuarioId) {
        Usuario paciente = usuarioService.obtenerPorId(usuarioId);
        return paciente != null && Usuario.ROL_PACIENTE.equals(paciente.getRol());
    }

    private List<Usuario> obtenerPacientesRegistrados() {
        return usuarioService.obtenerTodos().stream()
                .filter(usuario -> Usuario.ROL_PACIENTE.equals(usuario.getRol()))
                .collect(Collectors.toList());
    }

    private Usuario obtenerUsuarioSesion(HttpSession session) {
        return (Usuario) session.getAttribute("usuario");
    }
}

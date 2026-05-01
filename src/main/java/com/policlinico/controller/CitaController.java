package com.policlinico.controller;

import com.policlinico.model.Cita;
import com.policlinico.model.Usuario;
import com.policlinico.service.CitaService;
import com.policlinico.service.EspecialidadService;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/cita")
public class CitaController {

    @Autowired
    private CitaService citaService;

    @Autowired
    private EspecialidadService especialidadService;

    @GetMapping("/list")
    public String listar(@RequestParam(required = false) String estado, HttpSession session, Model model) {
        Usuario usuario = obtenerUsuarioSesion(session);
        if (usuario == null) {
            return "redirect:/login";
        }
        List<Cita> citasUsuario = citaService.obtenerPorUsuario(usuario.getId());
        List<Cita> citasFiltradas = citasUsuario;
        if (estado != null && !estado.isBlank() && !"TODAS".equalsIgnoreCase(estado)) {
            citasFiltradas = citasUsuario.stream()
                    .filter(cita -> estado.equalsIgnoreCase(cita.getEstado()))
                    .collect(Collectors.toList());
        }
        model.addAttribute("citas", citasFiltradas);
        cargarResumen(model, citasUsuario);
        model.addAttribute("estadoSeleccionado", estado == null || estado.isBlank() ? "TODAS" : estado);
        return "cita/list";
    }

    @GetMapping("/nueva")
    public String nueva(@RequestParam(required = false) String especialidad, HttpSession session, Model model) {
        if (obtenerUsuarioSesion(session) == null) {
            return "redirect:/login";
        }
        Cita cita = new Cita();
        if (especialidad != null && !especialidad.isBlank()) {
            cita.setEspecialidad(especialidad);
        }
        cargarFormulario(model, cita);
        return "cita/form";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Cita cita, HttpSession session, Model model) {
        Usuario usuario = obtenerUsuarioSesion(session);
        if (usuario == null) {
            return "redirect:/login";
        }
        cita.setUsuarioId(usuario.getId());
        cita.setNombrePaciente(usuario.getNombre() + " " + usuario.getApellido());
        try {
            citaService.registrarCita(cita);
        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());
            cargarFormulario(model, cita);
            return "cita/form";
        }
        return "redirect:/cita/list";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable int id, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        Usuario usuario = obtenerUsuarioSesion(session);
        if (usuario == null) {
            return "redirect:/login";
        }
        Cita cita = citaService.obtenerPorIdDeUsuario(id, usuario.getId());
        if (cita == null) {
            return mostrarNoEncontrado(redirectAttributes, "La cita solicitada no fue encontrada.");
        }
        cargarFormulario(model, cita);
        return "cita/form";
    }

    @PostMapping("/actualizar")
    public String actualizar(@ModelAttribute Cita cita, HttpSession session, Model model) {
        Usuario usuario = obtenerUsuarioSesion(session);
        if (usuario == null) {
            return "redirect:/login";
        }
        cita.setUsuarioId(usuario.getId());
        cita.setNombrePaciente(usuario.getNombre() + " " + usuario.getApellido());
        try {
            citaService.actualizarCita(cita);
        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());
            cargarFormulario(model, cita);
            return "cita/form";
        }
        return "redirect:/cita/list";
    }

    @GetMapping("/cancelar/{id}")
    public String cancelar(@PathVariable int id, HttpSession session) {
        Usuario usuario = obtenerUsuarioSesion(session);
        if (usuario == null) {
            return "redirect:/login";
        }
        citaService.cancelarCita(id, usuario.getId());
        return "redirect:/cita/list";
    }

    @GetMapping("/ver/{id}")
    public String ver(@PathVariable int id, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        Usuario usuario = obtenerUsuarioSesion(session);
        if (usuario == null) {
            return "redirect:/login";
        }
        Cita cita = citaService.obtenerPorIdDeUsuario(id, usuario.getId());
        if (cita == null) {
            return mostrarNoEncontrado(redirectAttributes, "La cita solicitada no fue encontrada.");
        }
        model.addAttribute("cita", cita);
        return "cita/detalle";
    }

    private Usuario obtenerUsuarioSesion(HttpSession session) {
        return (Usuario) session.getAttribute("usuario");
    }

    private void cargarFormulario(Model model, Cita cita) {
        model.addAttribute("especialidades", especialidadService.obtenerActivas());
        model.addAttribute("medicos", especialidadService.obtenerMedicos());
        model.addAttribute("horas", citaService.obtenerHorasDisponibles());
        model.addAttribute("cita", cita);
        model.addAttribute("hoy", LocalDate.now().toString());
    }

    private void cargarResumen(Model model, List<Cita> citas) {
        model.addAttribute("totalCitas", citas.size());
        model.addAttribute("totalPendientes", contarPorEstado(citas, "PENDIENTE"));
        model.addAttribute("totalConfirmadas", contarPorEstado(citas, "CONFIRMADA"));
        model.addAttribute("totalCanceladas", contarPorEstado(citas, "CANCELADA"));
    }

    private long contarPorEstado(List<Cita> citas, String estado) {
        return citas.stream()
                .filter(cita -> estado.equalsIgnoreCase(cita.getEstado()))
                .count();
    }

    private String mostrarNoEncontrado(RedirectAttributes redirectAttributes, String mensaje) {
        redirectAttributes.addFlashAttribute("mensaje", mensaje);
        return "redirect:/cita/list";
    }
}

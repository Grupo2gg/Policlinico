package com.policlinico.controller;

import com.policlinico.model.Horario;
import com.policlinico.model.Usuario;
import com.policlinico.service.HorarioService;
import com.policlinico.service.MedicoService;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/horarios")
public class HorarioController {

    @Autowired
    private HorarioService horarioService;

    @Autowired
    private MedicoService medicoService;

    @GetMapping
    public String listar(HttpSession session, Model model) {
        Usuario usuario = obtenerUsuarioSesion(session);
        if (usuario == null) {
            return "redirect:/login";
        }
        if (!"ADMIN".equals(usuario.getRol())) {
            return "redirect:/login";
        }

        cargarFormulario(model, usuario, new Horario(), false);
        return "admin/horarios";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable int id, HttpSession session, Model model,
                         RedirectAttributes redirectAttributes) {
        Usuario usuario = obtenerUsuarioSesion(session);
        if (usuario == null) {
            return "redirect:/login";
        }
        if (!"ADMIN".equals(usuario.getRol())) {
            return "redirect:/login";
        }

        Horario horario = horarioService.obtenerPorId(id);
        if (horario == null) {
            redirectAttributes.addFlashAttribute("mensaje", "El horario solicitado no fue encontrado.");
            return "redirect:/admin/horarios";
        }
        cargarFormulario(model, usuario, horario, true);
        return "admin/horarios";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Horario horario, HttpSession session, Model model) {
        Usuario usuario = obtenerUsuarioSesion(session);
        if (usuario == null) {
            return "redirect:/login";
        }
        if (!"ADMIN".equals(usuario.getRol())) {
            return "redirect:/login";
        }

        try {
            horarioService.registrar(horario);
        } catch (IllegalArgumentException ex) {
            cargarFormulario(model, usuario, horario, false);
            model.addAttribute("error", ex.getMessage());
            return "admin/horarios";
        }
        return "redirect:/admin/horarios";
    }

    @PostMapping("/actualizar")
    public String actualizar(@ModelAttribute Horario horario, HttpSession session, Model model) {
        Usuario usuario = obtenerUsuarioSesion(session);
        if (usuario == null) {
            return "redirect:/login";
        }
        if (!"ADMIN".equals(usuario.getRol())) {
            return "redirect:/login";
        }

        try {
            horarioService.actualizar(horario);
        } catch (IllegalArgumentException ex) {
            cargarFormulario(model, usuario, horario, true);
            model.addAttribute("error", ex.getMessage());
            return "admin/horarios";
        }
        return "redirect:/admin/horarios";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable int id, HttpSession session,
                           RedirectAttributes redirectAttributes) {
        Usuario usuario = obtenerUsuarioSesion(session);
        if (usuario == null) {
            return "redirect:/login";
        }
        if (!"ADMIN".equals(usuario.getRol())) {
            return "redirect:/login";
        }

        try {
            horarioService.eliminar(id);
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("mensaje", ex.getMessage());
        }
        return "redirect:/admin/horarios";
    }

    private void cargarFormulario(Model model, Usuario usuario, Horario horario, boolean modoEdicion) {
        model.addAttribute("usuario", usuario);
        model.addAttribute("horarios", horarioService.obtenerTodos());
        model.addAttribute("horario", horario);
        model.addAttribute("modoEdicion", modoEdicion);
        model.addAttribute("medicos", medicoService.obtenerActivos());
    }

    private Usuario obtenerUsuarioSesion(HttpSession session) {
        return (Usuario) session.getAttribute("usuario");
    }
}

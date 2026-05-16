package com.policlinico.controller;

import com.policlinico.model.Especialidad;
import com.policlinico.model.Usuario;
import com.policlinico.service.EspecialidadService;
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
@RequestMapping("/especialidad")
public class EspecialidadController {

    @Autowired
    private EspecialidadService especialidadService;

    @GetMapping("/list")
    public String listar(HttpSession session, Model model) {
        Usuario usuario = obtenerUsuarioSesion(session);
        if (usuario == null) {
            return "redirect:/login";
        }
        if (!"ADMIN".equals(usuario.getRol())) {
            return "redirect:/login";
        }
        cargarFormulario(model, new Especialidad(), false);
        return "admin/especialidades";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable int id, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        Usuario usuario = obtenerUsuarioSesion(session);
        if (usuario == null) {
            return "redirect:/login";
        }
        if (!"ADMIN".equals(usuario.getRol())) {
            return "redirect:/login";
        }
        Especialidad especialidad = especialidadService.obtenerPorId(id);
        if (especialidad == null) {
            redirectAttributes.addFlashAttribute("mensaje", "La especialidad solicitada no fue encontrada.");
            return "redirect:/especialidad/list";
        }
        cargarFormulario(model, especialidad, true);
        return "admin/especialidades";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Especialidad especialidad,
                          @RequestParam(value = "dias", required = false) String[] dias,
                          @RequestParam String horaInicio,
                          @RequestParam String horaFin,
                          HttpSession session) {
        Usuario usuario = obtenerUsuarioSesion(session);
        if (usuario == null) {
            return "redirect:/login";
        }
        if (!"ADMIN".equals(usuario.getRol())) {
            return "redirect:/login";
        }
        completarHorarioConDias(especialidad, dias, horaInicio, horaFin);
        especialidadService.registrar(especialidad);
        return "redirect:/especialidad/list";
    }

    @PostMapping("/actualizar")
    public String actualizar(@ModelAttribute Especialidad especialidad,
                             @RequestParam(value = "dias", required = false) String[] dias,
                             @RequestParam String horaInicio,
                             @RequestParam String horaFin,
                             HttpSession session) {
        Usuario usuario = obtenerUsuarioSesion(session);
        if (usuario == null) {
            return "redirect:/login";
        }
        if (!"ADMIN".equals(usuario.getRol())) {
            return "redirect:/login";
        }
        completarHorarioConDias(especialidad, dias, horaInicio, horaFin);
        especialidadService.actualizar(especialidad);
        return "redirect:/especialidad/list";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable int id, HttpSession session) {
        Usuario usuario = obtenerUsuarioSesion(session);
        if (usuario == null) {
            return "redirect:/login";
        }
        if (!"ADMIN".equals(usuario.getRol())) {
            return "redirect:/login";
        }
        especialidadService.eliminar(id);
        return "redirect:/especialidad/list";
    }

    @GetMapping("/ver/{id}")
    public String ver(@PathVariable int id, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        Usuario usuario = obtenerUsuarioSesion(session);
        if (usuario == null) {
            return "redirect:/login";
        }
        if (!"ADMIN".equals(usuario.getRol())) {
            return "redirect:/login";
        }
        model.addAttribute("especialidad", especialidadService.obtenerPorId(id));
        if (model.getAttribute("especialidad") == null) {
            redirectAttributes.addFlashAttribute("mensaje", "La especialidad solicitada no fue encontrada.");
            return "redirect:/especialidad/list";
        }
        return "especialidad/detalle";
    }

    private Usuario obtenerUsuarioSesion(HttpSession session) {
        return (Usuario) session.getAttribute("usuario");
    }

    private void cargarFormulario(Model model, Especialidad especialidad, boolean modoEdicion) {
        model.addAttribute("especialidades", especialidadService.obtenerTodas());
        model.addAttribute("especialidad", especialidad);
        model.addAttribute("modoEdicion", modoEdicion);
    }

    private void completarHorarioConDias(Especialidad especialidad, String[] dias, 
                                          String horaInicio, String horaFin) {
        // Construir el String: "Lunes,Martes 08:00-12:00"
        if (dias != null && dias.length > 0) {
            String diasFormato = String.join(",", dias);
            especialidad.setHorarioDisponible(diasFormato + " " + horaInicio + "-" + horaFin);
        } else {
            especialidad.setHorarioDisponible("");
        }
    }
}

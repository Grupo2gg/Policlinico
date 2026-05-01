package com.policlinico.controller;

import com.policlinico.service.EspecialidadService;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/especialidad")
public class EspecialidadController {

    @Autowired
    private EspecialidadService especialidadService;

    @GetMapping("/list")
    public String listar(HttpSession session, Model model) {
        if (session.getAttribute("usuario") == null) {
            return "redirect:/login";
        }
        model.addAttribute("especialidades", especialidadService.obtenerActivas());
        return "especialidad/list";
    }

    @GetMapping("/ver/{id}")
    public String ver(@PathVariable int id, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        if (session.getAttribute("usuario") == null) {
            return "redirect:/login";
        }
        model.addAttribute("especialidad", especialidadService.obtenerPorId(id));
        if (model.getAttribute("especialidad") == null) {
            redirectAttributes.addFlashAttribute("mensaje", "La especialidad solicitada no fue encontrada.");
            return "redirect:/especialidad/list";
        }
        return "especialidad/detalle";
    }
}

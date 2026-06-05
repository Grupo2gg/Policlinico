package com.policlinico.controller;

import com.policlinico.model.Medico;
import com.policlinico.model.Usuario;
import com.policlinico.service.EspecialidadService;
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
@RequestMapping("/admin/medicos")
public class MedicoController {

    @Autowired
    private MedicoService medicoService;

    @Autowired
    private EspecialidadService especialidadService;

    @GetMapping
    public String listar(HttpSession session, Model model) {
        Usuario usuario = obtenerUsuarioSesion(session);
        if (usuario == null) {
            return "redirect:/login";
        }
        if (!"ADMIN".equals(usuario.getRol())) {
            return "redirect:/login";
        }

        cargarFormulario(model, usuario, new Medico(), false);
        return "admin/medicos";
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

        Medico medico = medicoService.obtenerPorId(id);
        if (medico == null) {
            redirectAttributes.addFlashAttribute("mensaje", "El medico solicitado no fue encontrado.");
            return "redirect:/admin/medicos";
        }
        cargarFormulario(model, usuario, medico, true);
        return "admin/medicos";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Medico medico, HttpSession session, Model model) {
        Usuario usuario = obtenerUsuarioSesion(session);
        if (usuario == null) {
            return "redirect:/login";
        }
        if (!"ADMIN".equals(usuario.getRol())) {
            return "redirect:/login";
        }

        try {
            // Generar nombre con título basado en género
            generarNombreConTitulo(medico);
            medicoService.registrar(medico);
        } catch (IllegalArgumentException ex) {
            cargarFormulario(model, usuario, medico, false);
            model.addAttribute("error", ex.getMessage());
            return "admin/medicos";
        }
        return "redirect:/admin/medicos";
    }

    @PostMapping("/actualizar")
    public String actualizar(@ModelAttribute Medico medico, HttpSession session, Model model) {
        Usuario usuario = obtenerUsuarioSesion(session);
        if (usuario == null) {
            return "redirect:/login";
        }
        if (!"ADMIN".equals(usuario.getRol())) {
            return "redirect:/login";
        }

        try {
            // Generar nombre con título basado en género
            generarNombreConTitulo(medico);
            medicoService.actualizar(medico);
        } catch (IllegalArgumentException ex) {
            cargarFormulario(model, usuario, medico, true);
            model.addAttribute("error", ex.getMessage());
            return "admin/medicos";
        }
        return "redirect:/admin/medicos";
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

        medicoService.eliminar(id);
        return "redirect:/admin/medicos";
    }

    private void cargarFormulario(Model model, Usuario usuario, Medico medico, boolean modoEdicion) {
        model.addAttribute("usuario", usuario);
        model.addAttribute("medicos", medicoService.obtenerTodos());
        model.addAttribute("medico", medico);
        model.addAttribute("modoEdicion", modoEdicion);
        model.addAttribute("especialidades", especialidadService.obtenerActivas());
    }

    private Usuario obtenerUsuarioSesion(HttpSession session) {
        return (Usuario) session.getAttribute("usuario");
    }

    /**
     * Genera el nombre con título (Dr. o Dra.) según el género
     */
    private void generarNombreConTitulo(Medico medico) {
        if (medico.getNombre() != null && !medico.getNombre().trim().isEmpty()) {
            String nombreOriginal = medico.getNombre();
            
            // Evitar duplicar el título si ya existe
            if (!nombreOriginal.startsWith("Dr.") && !nombreOriginal.startsWith("Dra.")) {
                String titulo = Medico.GENERO_FEMENINO.equalsIgnoreCase(medico.getGenero()) 
                    ? "Dra. " 
                    : "Dr. ";
                medico.setNombre(titulo + nombreOriginal);
            }
        }
    }
}

package com.policlinico.controller;

import com.policlinico.model.EspecialidadModelo;
import com.policlinico.service.EspecialidadServicio;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/especialidades")
public class EspecialidadController {
    private final EspecialidadServicio especialidadServicio;

    public EspecialidadController(EspecialidadServicio especialidadServicio) {
        this.especialidadServicio = especialidadServicio;
    }

    @GetMapping
    public ModelAndView listar(HttpSession session, Model model) {
        if (!rolValido(session)) return new ModelAndView("redirect:/login");
        cargarEspecialidades(model, new EspecialidadModelo(), false);
        return new ModelAndView("admin/especialidades");
    }

    @GetMapping("/ver/{id}")
    public ModelAndView ver(@PathVariable Long id, HttpSession session, Model model) {
        if (!rolValido(session)) return new ModelAndView("redirect:/login");
        model.addAttribute("especialidadDetalle", especialidadServicio.buscar(id));
        cargarEspecialidades(model, new EspecialidadModelo(), false);
        return new ModelAndView("admin/especialidades");
    }

    @GetMapping("/editar/{id}")
    public ModelAndView editar(@PathVariable Long id, HttpSession session, Model model) {
        if (!rolValido(session)) return new ModelAndView("redirect:/login");
        cargarEspecialidades(model, especialidadServicio.buscar(id), true);
        return new ModelAndView("admin/especialidades");
    }

    @PostMapping("/guardar")
    public ModelAndView guardar(@ModelAttribute EspecialidadModelo especialidad,
                                HttpSession session, org.springframework.ui.Model model) {
        if (!rolValido(session)) return new ModelAndView("redirect:/login");
        try {
            especialidadServicio.guardar(especialidad);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            cargarEspecialidades(model, especialidad, especialidad.getId() != null);
            return new ModelAndView("admin/especialidades");
        }
        return new ModelAndView("redirect:/especialidades");
    }

    @GetMapping("/eliminar/{id}")
    public ModelAndView eliminar(@PathVariable Long id, HttpSession session) {
        if (!rolValido(session)) return new ModelAndView("redirect:/login");
        especialidadServicio.eliminar(id);
        return new ModelAndView("redirect:/especialidades");
    }

    private boolean rolValido(HttpSession session) {
        Long usuarioId = (Long) session.getAttribute("usuarioId");
        return usuarioId != null && "ADMIN".equals(session.getAttribute("rol"));
    }

    private void cargarEspecialidades(Model model, EspecialidadModelo especialidad, boolean editando) {
        model.addAttribute("especialidades", especialidadServicio.listar());
        model.addAttribute("especialidadFormulario", especialidad);
        model.addAttribute("editando", editando);
        model.addAttribute("estadosEspecialidad", new String[] {"ACTIVA", "INACTIVA"});
    }
    // Maneja los errores de este controlador y vuelve con un mensaje
    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public ModelAndView manejarError(Exception ex,
                                     org.springframework.web.servlet.mvc.support.RedirectAttributes ra) {
        String msg = (ex instanceof IllegalArgumentException && ex.getMessage() != null)
                ? ex.getMessage()
                : "No se pudo procesar la especialidad. Verifique los datos e intente de nuevo.";
        ra.addFlashAttribute("error", msg);
        return new ModelAndView("redirect:/especialidades");
    }
}

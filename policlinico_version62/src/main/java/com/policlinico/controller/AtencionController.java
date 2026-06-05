package com.policlinico.controller;

import com.policlinico.model.AtencionModelo;
import com.policlinico.model.MedicoModelo;
import com.policlinico.service.AtencionServicio;
import com.policlinico.service.CitaServicio;
import com.policlinico.service.MedicoServicio;
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
@RequestMapping("/atenciones")
public class AtencionController {
    private final AtencionServicio atencionServicio;
    private final CitaServicio citaServicio;
    private final MedicoServicio medicoServicio;

    public AtencionController(AtencionServicio atencionServicio, CitaServicio citaServicio, MedicoServicio medicoServicio) {
        this.atencionServicio = atencionServicio;
        this.citaServicio = citaServicio;
        this.medicoServicio = medicoServicio;
    }

    @GetMapping("/registrar/{citaId}")
    public ModelAndView registrar(@PathVariable Long citaId, HttpSession session, Model model) {
        if (!rolValido(session)) return new ModelAndView("redirect:/login");
        MedicoModelo medico = medicoSesion(session);
        if (medico == null) return new ModelAndView("redirect:/login");
        // Redirige a mis-atenciones con la cita preseleccionada
        return new ModelAndView("redirect:/atenciones/mis-atenciones?citaId=" + citaId);
    }

    @PostMapping("/guardar")
    public ModelAndView guardar(
            @org.springframework.web.bind.annotation.RequestParam Long citaId,
            @org.springframework.web.bind.annotation.RequestParam(required = false) String diagnostico,
            @org.springframework.web.bind.annotation.RequestParam(required = false) String observaciones,
            @org.springframework.web.bind.annotation.RequestParam(required = false) String fechaAtencion,
            @org.springframework.web.bind.annotation.RequestParam(required = false) String estado,
            HttpSession session, Model model) {
        if (!rolValido(session)) return new ModelAndView("redirect:/login");
        MedicoModelo medico = medicoSesion(session);
        if (medico == null) return new ModelAndView("redirect:/login");

        AtencionModelo atencion = new AtencionModelo();
        atencion.setCitaId(citaId);
        atencion.setMedicoId(medico.getId());
        atencion.setDiagnostico(diagnostico != null ? diagnostico.trim() : "");
        atencion.setObservaciones(observaciones);
        atencion.setEstado(estado);
        if (fechaAtencion != null && !fechaAtencion.isBlank()) {
            try { atencion.setFechaAtencion(java.time.LocalDate.parse(fechaAtencion)); } catch (Exception ignored) {}
        }
        if (atencion.getFechaAtencion() == null) atencion.setFechaAtencion(java.time.LocalDate.now());

        try {
            atencionServicio.guardar(atencion);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            MedicoModelo m = medico;
            model.addAttribute("atenciones", atencionServicio.listarPorMedico(m.getId()));
            model.addAttribute("citasConfirmadas", citaServicio.listarPorMedico(m.getId())
                    .stream().filter(c -> "CONFIRMADA".equalsIgnoreCase(c.getEstado()))
                    .collect(java.util.stream.Collectors.toList()));
            model.addAttribute("citaPreseleccionada", citaId);
            return new ModelAndView("medico/mis-atenciones");
        }
        return new ModelAndView("redirect:/atenciones/mis-atenciones");
    }

    @GetMapping("/finalizar/{id}")
    public ModelAndView finalizar(@PathVariable Long id, HttpSession session, Model model) {
        if (!rolValido(session)) return new ModelAndView("redirect:/login");
        MedicoModelo medico = medicoSesion(session);
        if (medico == null) return new ModelAndView("redirect:/login");
        try {
            atencionServicio.finalizar(id, medico.getId());
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            cargarMisAtenciones(model, medico, null);
            return new ModelAndView("medico/mis-atenciones");
        }
        return new ModelAndView("redirect:/atenciones/mis-atenciones");
    }

    // El médico edita su atención mientras está EN_PROCESO (carga datos en el formulario)
    @GetMapping("/editar/{id}")
    public ModelAndView editarMia(@PathVariable Long id, HttpSession session, Model model) {
        if (!rolValido(session)) return new ModelAndView("redirect:/login");
        MedicoModelo medico = medicoSesion(session);
        if (medico == null) return new ModelAndView("redirect:/login");
        com.policlinico.model.AtencionModelo atencion = atencionServicio.buscar(id);
        if (atencion == null || !medico.getId().equals(atencion.getMedicoId())) {
            return new ModelAndView("redirect:/atenciones/mis-atenciones");
        }
        if (!"EN_PROCESO".equals(atencion.getEstado())) {
            model.addAttribute("error", "Solo se puede editar una atención EN_PROCESO.");
            cargarMisAtenciones(model, medico, null);
            return new ModelAndView("medico/mis-atenciones");
        }
        model.addAttribute("atencionEditar", atencion);
        cargarMisAtenciones(model, medico, null);
        return new ModelAndView("medico/mis-atenciones");
    }

    // El médico guarda los cambios de su atención EN_PROCESO (sin finalizar)
    @PostMapping("/actualizar")
    public ModelAndView actualizarMia(@ModelAttribute AtencionModelo atencion, HttpSession session, Model model) {
        if (!rolValido(session)) return new ModelAndView("redirect:/login");
        MedicoModelo medico = medicoSesion(session);
        if (medico == null) return new ModelAndView("redirect:/login");
        try {
            com.policlinico.model.AtencionModelo actual = atencionServicio.buscar(atencion.getId());
            if (actual == null || !medico.getId().equals(actual.getMedicoId())) {
                throw new IllegalArgumentException("No puede editar esta atención.");
            }
            // Mantener EN_PROCESO (no finaliza desde aquí)
            atencion.setEstado("EN_PROCESO");
            atencionServicio.guardarAdmin(atencion);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            cargarMisAtenciones(model, medico, null);
            return new ModelAndView("medico/mis-atenciones");
        }
        return new ModelAndView("redirect:/atenciones/mis-atenciones");
    }

    @GetMapping("/mis-atenciones")
    public ModelAndView misAtenciones(HttpSession session, Model model,
            @org.springframework.web.bind.annotation.RequestParam(required = false) Long citaId) {
        if (!rolValido(session)) return new ModelAndView("redirect:/login");
        MedicoModelo medico = medicoSesion(session);
        java.util.List<com.policlinico.model.CitaModelo> confirmadas = cargarMisAtenciones(model, medico, citaId);
        // Si viene con citaId preseleccionado (desde botón Atender), pasarlo a la vista
        if (citaId != null) {
            model.addAttribute("citaPreseleccionada", citaId);
            confirmadas.stream().filter(c -> c.getId().equals(citaId)).findFirst().ifPresent(c -> {
                model.addAttribute("fechaAtenciónPreseleccionada", c.getFechaTexto());
            });
        }
        return new ModelAndView("medico/mis-atenciones");
    }

    @GetMapping("/admin/lista")
    public ModelAndView listaAdmin(HttpSession session, Model model) {
        if (!rolAdmin(session)) return new ModelAndView("redirect:/login");
        cargarAdmin(model, new AtencionModelo(), false);
        return new ModelAndView("admin/atenciones");
    }

    @GetMapping("/admin/ver/{id}")
    public ModelAndView verAdmin(@PathVariable Long id, HttpSession session, Model model) {
        if (!rolAdmin(session)) return new ModelAndView("redirect:/login");
        model.addAttribute("atencionDetalle", atencionServicio.buscar(id));
        cargarAdmin(model, new AtencionModelo(), false);
        return new ModelAndView("admin/atenciones");
    }

    @GetMapping("/admin/editar/{id}")
    public ModelAndView editarAdmin(@PathVariable Long id, HttpSession session, Model model) {
        if (!rolAdmin(session)) return new ModelAndView("redirect:/login");
        cargarAdmin(model, atencionServicio.buscar(id), true);
        return new ModelAndView("admin/atenciones");
    }

    @PostMapping("/admin/guardar")
    public ModelAndView guardarAdmin(@ModelAttribute AtencionModelo atencion, HttpSession session) {
        if (!rolAdmin(session)) return new ModelAndView("redirect:/login");
        atencionServicio.guardarAdmin(atencion);
        return new ModelAndView("redirect:/atenciones/admin/lista");
    }

    @GetMapping("/admin/eliminar/{id}")
    public ModelAndView eliminarAdmin(@PathVariable Long id, HttpSession session) {
        if (!rolAdmin(session)) return new ModelAndView("redirect:/login");
        atencionServicio.eliminar(id);
        return new ModelAndView("redirect:/atenciones/admin/lista");
    }

    private MedicoModelo medicoSesion(HttpSession session) {
        return medicoServicio.buscarPorUsuario((Long) session.getAttribute("usuarioId"));
    }

    private boolean rolValido(HttpSession session) {
        Long usuarioId = (Long) session.getAttribute("usuarioId");
        return usuarioId != null && "MEDICO".equals(session.getAttribute("rol"));
    }

    private boolean rolAdmin(HttpSession session) {
        Long usuarioId = (Long) session.getAttribute("usuarioId");
        return usuarioId != null && "ADMIN".equals(session.getAttribute("rol"));
    }

    private void cargarAdmin(Model model, AtencionModelo atencion, boolean editando) {
        model.addAttribute("atenciones", atencionServicio.listar());
        model.addAttribute("citas", citaServicio.listar());
        model.addAttribute("medicos", medicoServicio.listar());
        model.addAttribute("atencionFormulario", atencion);
        model.addAttribute("editando", editando);
    }

    private java.util.List<com.policlinico.model.CitaModelo> cargarMisAtenciones(Model model, MedicoModelo medico, Long citaId) {
        model.addAttribute("atenciones", atencionServicio.listarPorMedico(medico.getId()));
        java.util.List<com.policlinico.model.CitaModelo> confirmadas = citaServicio.listarPorMedico(medico.getId())
                .stream()
                .filter(c -> "CONFIRMADA".equalsIgnoreCase(c.getEstado()))
                .filter(c -> !atencionServicio.tieneAtencionParaCita(c.getId()))
                .collect(java.util.stream.Collectors.toList());
        model.addAttribute("citasConfirmadas", confirmadas);
        if (citaId != null) model.addAttribute("citaPreseleccionada", citaId);
        return confirmadas;
    }

    // Maneja los errores de este controlador y vuelve con un mensaje
    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public ModelAndView manejarError(Exception ex,
                                     org.springframework.web.servlet.mvc.support.RedirectAttributes ra) {
        String msg = (ex instanceof IllegalArgumentException && ex.getMessage() != null)
                ? ex.getMessage()
                : "No se pudo procesar la atencion. Verifique los datos e intente de nuevo.";
        ra.addFlashAttribute("error", msg);
        return new ModelAndView("redirect:/atenciones/mis-atenciones");
    }
}

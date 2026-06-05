package com.policlinico.controller;

import com.policlinico.model.CitaModelo;
import com.policlinico.service.CitaServicio;
import com.policlinico.service.EspecialidadServicio;
import com.policlinico.service.HorarioServicio;
import com.policlinico.service.MedicoServicio;
import com.policlinico.service.UsuarioServicio;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/citas")
public class CitaController {
    private final CitaServicio citaServicio;
    private final MedicoServicio medicoServicio;
    private final HorarioServicio horarioServicio;
    private final EspecialidadServicio especialidadServicio;
    private final UsuarioServicio usuarioServicio;

    public CitaController(CitaServicio citaServicio, MedicoServicio medicoServicio,
                          HorarioServicio horarioServicio, EspecialidadServicio especialidadServicio,
                          UsuarioServicio usuarioServicio) {
        this.citaServicio = citaServicio;
        this.medicoServicio = medicoServicio;
        this.horarioServicio = horarioServicio;
        this.especialidadServicio = especialidadServicio;
        this.usuarioServicio = usuarioServicio;
    }

    @GetMapping("/nueva")
    public ModelAndView nueva(HttpSession session, Model model) {
        if (!rolPaciente(session)) return new ModelAndView("redirect:/login");
        cargarFormulario(model, new CitaModelo());
        return new ModelAndView("paciente/reservar-cita");
    }

    @PostMapping("/guardar")
    public ModelAndView guardar(HttpServletRequest request, HttpSession session, Model model) {
        if (!rolPaciente(session)) return new ModelAndView("redirect:/login");
        return guardarCita(leerCitaDesdeRequest(request, session, false), model, false);
    }

    @GetMapping("/mis-citas")
    public ModelAndView misCitas(HttpSession session, Model model) {
        if (!rolPaciente(session)) return new ModelAndView("redirect:/login");
        model.addAttribute("citas", citaServicio.listarPorPaciente((Long) session.getAttribute("usuarioId")));
        return new ModelAndView("paciente/mis-citas");
    }

    @GetMapping("/cancelar/{id}")
    public ModelAndView cancelar(@PathVariable Long id, HttpSession session) {
        if (!rolPaciente(session)) return new ModelAndView("redirect:/login");
        citaServicio.cancelar(id);
        return new ModelAndView("redirect:/citas/mis-citas");
    }

    @GetMapping("/admin/lista")
    public ModelAndView listaAdmin(HttpSession session, Model model) {
        if (!rolAdmin(session)) return new ModelAndView("redirect:/login");
        cargarAdmin(model, new CitaModelo(), false);
        return new ModelAndView("admin/citas");
    }

    @GetMapping("/admin/ver/{id}")
    public ModelAndView verAdmin(@PathVariable Long id, HttpSession session, Model model) {
        if (!rolAdmin(session)) return new ModelAndView("redirect:/login");
        model.addAttribute("citaDetalle", citaServicio.buscar(id));
        cargarAdmin(model, new CitaModelo(), false);
        return new ModelAndView("admin/citas");
    }

    @GetMapping("/admin/editar/{id}")
    public ModelAndView editarAdmin(@PathVariable Long id, HttpSession session, Model model) {
        if (!rolAdmin(session)) return new ModelAndView("redirect:/login");
        cargarAdmin(model, citaServicio.buscar(id), true);
        return new ModelAndView("admin/citas");
    }

    @PostMapping("/admin/guardar")
    public ModelAndView guardarAdmin(HttpServletRequest request, HttpSession session, Model model) {
        if (!rolAdmin(session)) return new ModelAndView("redirect:/login");
        CitaModelo cita = leerCitaDesdeRequest(request, session, true);
        if (cita.getPacienteId() == null) {
            model.addAttribute("error", "Debe seleccionar un paciente.");
            cargarAdmin(model, cita, cita.getId() != null);
            return new ModelAndView("admin/citas");
        }
        return guardarCita(cita, model, true);
    }

    @GetMapping("/admin/eliminar/{id}")
    public ModelAndView eliminarAdmin(@PathVariable Long id, HttpSession session) {
        if (!rolAdmin(session)) return new ModelAndView("redirect:/login");
        citaServicio.eliminar(id);
        return new ModelAndView("redirect:/citas/admin/lista");
    }

    @GetMapping("/ver/{id}")
    public ModelAndView verPaciente(@PathVariable Long id, HttpSession session, Model model) {
        if (!rolPaciente(session)) return new ModelAndView("redirect:/login");
        model.addAttribute("cita", citaServicio.buscar(id));
        return new ModelAndView("paciente/detalle-cita");
    }

    @GetMapping("/editar/{id}")
    public ModelAndView editarPaciente(@PathVariable Long id, HttpSession session, Model model) {
        if (!rolPaciente(session)) return new ModelAndView("redirect:/login");
        cargarFormulario(model, citaServicio.buscar(id));
        return new ModelAndView("paciente/reservar-cita");
    }

    @PostMapping("/actualizar")
    public ModelAndView actualizarPaciente(HttpServletRequest request, HttpSession session, Model model) {
        if (!rolPaciente(session)) return new ModelAndView("redirect:/login");
        CitaModelo cita = leerCitaDesdeRequest(request, session, false);
        if (cita.getId() == null) {
            model.addAttribute("error", "Cita no identificada para actualizar.");
            cargarFormulario(model, cita);
            return new ModelAndView("paciente/reservar-cita");
        }
        return guardarCita(cita, model, false);
    }

    private CitaModelo leerCitaDesdeRequest(HttpServletRequest request, HttpSession session, boolean admin) {
        CitaModelo cita = new CitaModelo();
        cita.setId(longParam(request, "id"));
        cita.setHorarioId(longParam(request, "horarioId"));
        cita.setMotivo(stringParam(request, "motivo"));
        cita.setObservaciones(stringParam(request, "observaciones"));
        cita.setEstado(stringParam(request, "estado"));
        if (admin) {
            cita.setPacienteId(longParam(request, "pacienteId"));
        } else {
            cita.setPacienteId((Long) session.getAttribute("usuarioId"));
        }
        return cita;
    }

    // Lee un parámetro numérico del formulario (null si viene vacío)
    private Long longParam(HttpServletRequest request, String name) {
        String raw = request.getParameter(name);
        if (raw == null || raw.trim().isEmpty()) return null;
        return Long.valueOf(raw.trim());
    }

    // Lee un parámetro de texto del formulario (cadena vacía si viene null)
    private String stringParam(HttpServletRequest request, String name) {
        String value = request.getParameter(name);
        return value == null ? "" : value.trim();
    }

    private ModelAndView guardarCita(CitaModelo cita, Model model, boolean admin) {
        if (cita.getHorarioId() == null) {
            model.addAttribute("error", "Debe seleccionar un horario disponible.");
            if (admin) {
                cargarAdmin(model, cita, cita.getId() != null);
                return new ModelAndView("admin/citas");
            }
            cargarFormulario(model, cita);
            return new ModelAndView("paciente/reservar-cita");
        }
        try {
            citaServicio.guardar(cita);
            return new ModelAndView(admin ? "redirect:/citas/admin/lista" : "redirect:/citas/mis-citas");
        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());
            if (admin) {
                cargarAdmin(model, cita, cita.getId() != null);
                return new ModelAndView("admin/citas");
            }
            cargarFormulario(model, cita);
            return new ModelAndView("paciente/reservar-cita");
        }
    }

    private void cargarAdmin(Model model, CitaModelo cita, boolean editando) {
        model.addAttribute("citas", citaServicio.listar());
        model.addAttribute("citaFormulario", cita);
        model.addAttribute("medicos", medicoServicio.listarActivos());
        model.addAttribute("especialidades", especialidadServicio.listarActivas());
        agregarHorariosJson(model);
        model.addAttribute("pacientes", usuarioServicio.listar().stream()
                .filter(u -> "PACIENTE".equals(u.getRol()))
                .collect(Collectors.toList()));
        model.addAttribute("editando", editando);
    }

    private void cargarFormulario(Model model, CitaModelo cita) {
        model.addAttribute("cita", cita);
        model.addAttribute("medicos", medicoServicio.listarActivos());
        model.addAttribute("especialidades", especialidadServicio.listarActivas());
        agregarHorariosJson(model);
    }

    private void agregarHorariosJson(Model model) {
        model.addAttribute("horariosJson", serializarHorarios(horarioServicio.listarDisponibles()));
    }

    // Convierte la lista de horarios disponibles a JSON para el calendario de citas
    private String serializarHorarios(java.util.List<com.policlinico.model.HorarioModelo> horarios) {
        java.util.List<java.util.Map<String, Object>> opciones = new java.util.ArrayList<>();
        if (horarios != null) {
            for (com.policlinico.model.HorarioModelo h : horarios) {
                if (h == null || h.getId() == null || h.getMedicoId() == null) continue;
                String fecha = h.getFechaTexto();
                if (fecha == null || fecha.isEmpty()) continue;
                java.util.Map<String, Object> item = new java.util.LinkedHashMap<>();
                item.put("id", h.getId());
                item.put("medicoId", h.getMedicoId());
                item.put("fecha", fecha);
                item.put("diaSemana", h.getDiaSemana() != null ? h.getDiaSemana() : "");
                item.put("horaInicio", h.getHoraInicioTexto());
                item.put("horaFin", h.getHoraFinTexto());
                opciones.add(item);
            }
        }
        try {
            return new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(opciones);
        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            return "[]";
        }
    }

    private boolean rolPaciente(HttpSession session) {
        Long usuarioId = (Long) session.getAttribute("usuarioId");
        return usuarioId != null && "PACIENTE".equals(session.getAttribute("rol"));
    }

    private boolean rolAdmin(HttpSession session) {
        Long usuarioId = (Long) session.getAttribute("usuarioId");
        return usuarioId != null && "ADMIN".equals(session.getAttribute("rol"));
    }

    // Maneja los errores de este controlador y vuelve a la pantalla con un mensaje
    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public ModelAndView manejarError(Exception ex, HttpServletRequest request,
                                     org.springframework.web.servlet.mvc.support.RedirectAttributes ra) {
        String msg = (ex instanceof IllegalArgumentException && ex.getMessage() != null)
                ? ex.getMessage()
                : "Ocurrio un error al procesar la cita. Verifique los datos e intente de nuevo.";
        ra.addFlashAttribute("error", msg);
        String uri = request.getRequestURI() != null ? request.getRequestURI() : "";
        if (uri.contains("/citas/admin")) {
            return new ModelAndView("redirect:/citas/admin/lista");
        }
        if (uri.contains("/citas")) {
            return new ModelAndView("redirect:/citas/mis-citas");
        }
        return new ModelAndView("redirect:/login");
    }
}

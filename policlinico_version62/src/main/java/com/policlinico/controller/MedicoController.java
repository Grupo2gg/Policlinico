package com.policlinico.controller;

import com.policlinico.model.MedicoModelo;
import com.policlinico.service.CitaServicio;
import com.policlinico.service.DisponibilidadServicio;
import com.policlinico.service.EspecialidadServicio;
import com.policlinico.service.HorarioServicio;
import com.policlinico.service.MedicoServicio;
import com.policlinico.service.UsuarioServicio;
import com.policlinico.repository.DisponibilidadRepositorio;
import com.policlinico.repository.HorarioRepositorio;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/medico")
public class MedicoController {
    private final MedicoServicio medicoServicio;
    private final CitaServicio citaServicio;
    private final DisponibilidadServicio disponibilidadServicio;
    private final UsuarioServicio usuarioServicio;
    private final EspecialidadServicio especialidadServicio;
    private final HorarioServicio horarioServicio;
    private final DisponibilidadRepositorio disponibilidadRepositorio;
    private final HorarioRepositorio horarioRepositorio;

    public MedicoController(MedicoServicio medicoServicio, CitaServicio citaServicio,
                            DisponibilidadServicio disponibilidadServicio, UsuarioServicio usuarioServicio,
                            EspecialidadServicio especialidadServicio, DisponibilidadRepositorio disponibilidadRepositorio,
                            HorarioRepositorio horarioRepositorio,
                            HorarioServicio horarioServicio) {
        this.medicoServicio = medicoServicio;
        this.citaServicio = citaServicio;
        this.disponibilidadServicio = disponibilidadServicio;
        this.usuarioServicio = usuarioServicio;
        this.especialidadServicio = especialidadServicio;
        this.horarioServicio = horarioServicio;
        this.disponibilidadRepositorio = disponibilidadRepositorio;
        this.horarioRepositorio = horarioRepositorio;
    }

    @GetMapping("/inicio")
    public ModelAndView inicio(HttpSession session, Model model) {
        if (!rolValido(session)) return new ModelAndView("redirect:/login");
        MedicoModelo medico = medicoSesion(session);
        model.addAttribute("citas", medico == null ? 0 : citaServicio.listarPorMedico(medico.getId()).size());
        return new ModelAndView("medico/inicio");
    }

    @GetMapping("/mis-citas")
    public ModelAndView misCitas(HttpSession session, Model model) {
        if (!rolValido(session)) return new ModelAndView("redirect:/login");
        MedicoModelo medico = medicoSesion(session);
        model.addAttribute("citas", medico == null ? null : citaServicio.listarPorMedico(medico.getId()));
        return new ModelAndView("medico/mis-citas");
    }

    @GetMapping("/detalle-cita/{id}")
    public ModelAndView detalleCita(@PathVariable Long id, HttpSession session, Model model) {
        if (!rolValido(session)) return new ModelAndView("redirect:/login");
        MedicoModelo medico = medicoSesion(session);
        if (medico == null || citaServicio.listarPorMedico(medico.getId()).stream().noneMatch(c -> c.getId().equals(id))) {
            return new ModelAndView("redirect:/medico/mis-citas");
        }
        model.addAttribute("cita", citaServicio.buscar(id));
        return new ModelAndView("medico/detalle-cita");
    }

    @GetMapping("/confirmar-cita/{id}")
    public ModelAndView confirmarCita(@PathVariable Long id, HttpSession session) {
        if (!rolValido(session)) return new ModelAndView("redirect:/login");
        MedicoModelo medico = medicoSesion(session);
        citaServicio.cambiarEstadoPorMedico(id, medico.getId(), "CONFIRMADA");
        return new ModelAndView("redirect:/medico/mis-citas");
    }

    @PostMapping("/cita/estado")
    public ModelAndView cambiarEstado(@RequestParam Long citaId, @RequestParam String estado, HttpSession session) {
        if (!rolValido(session)) return new ModelAndView("redirect:/login");
        MedicoModelo medico = medicoSesion(session);
        if (medico == null) return new ModelAndView("redirect:/login");
        citaServicio.cambiarEstadoPorMedico(citaId, medico.getId(), estado);
        return new ModelAndView("redirect:/medico/mis-citas");
    }

    @GetMapping("/no-confirmar-cita/{id}")
    public ModelAndView noConfirmarCita(@PathVariable Long id, HttpSession session) {
        if (!rolValido(session)) return new ModelAndView("redirect:/login");
        MedicoModelo medico = medicoSesion(session);
        citaServicio.cambiarEstadoPorMedico(id, medico.getId(), "CANCELADA");
        return new ModelAndView("redirect:/medico/mis-citas");
    }

    @GetMapping("/mis-horarios")
    public ModelAndView misHorarios(HttpSession session, Model model) {
        if (!rolValido(session)) return new ModelAndView("redirect:/login");
        MedicoModelo medico = medicoSesion(session);
        model.addAttribute("horarios", medico == null ? null : horarioServicio.listarPorMedico(medico.getId()));
        return new ModelAndView("medico/mis-horarios");
    }

    @GetMapping("/disponibilidad")
    public ModelAndView disponibilidad(HttpSession session, Model model) {
        if (!rolValido(session)) return new ModelAndView("redirect:/medico/inicio");
        return new ModelAndView("redirect:/medico/mis-horarios");
    }

    @PostMapping("/disponibilidad/guardar")
    public ModelAndView guardarDisponibilidad(
            @RequestParam String mes,
            @RequestParam(required = false) List<String> dias,
            @RequestParam String horaInicioRango,
            @RequestParam String horaFinRango,
            @RequestParam(defaultValue = "30") int duracion,
            HttpSession session) {
        if (!rolValido(session)) return new ModelAndView("redirect:/login");
        MedicoModelo medico = medicoSesion(session);
        if (medico == null) return new ModelAndView("redirect:/login");

        if (dias != null && !dias.isEmpty() && mes != null && !mes.isEmpty()) {
            String[] parts = mes.split("-");
            int anio   = Integer.parseInt(parts[0]);
            int mesNum = Integer.parseInt(parts[1]);
            java.time.LocalDate startDate = java.time.LocalDate.of(anio, mesNum, 1);
            java.time.LocalDate endDate   = startDate.with(java.time.temporal.TemporalAdjusters.lastDayOfMonth());

            // Eliminar disponibilidades+horarios anteriores del médico en ese mes
            List<com.policlinico.entity.Disponibilidad> dispList = disponibilidadRepositorio.findByMedicoId(medico.getId());
            for (com.policlinico.entity.Disponibilidad d : dispList) {
                if (d.getFecha().getYear() == anio && d.getFecha().getMonthValue() == mesNum) {
                    List<com.policlinico.entity.Horario> hList = horarioRepositorio.findAll();
                    for (com.policlinico.entity.Horario h : hList) {
                        if (h.getDisponibilidad().getId().equals(d.getId())) {
                            horarioRepositorio.delete(h);
                        }
                    }
                    disponibilidadRepositorio.delete(d);
                }
            }

            java.time.LocalTime hiInicio = java.time.LocalTime.parse(horaInicioRango);
            java.time.LocalTime hiFin    = java.time.LocalTime.parse(horaFinRango);

            // Generar nuevas disponibilidades y horarios en bloques
            for (java.time.LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
                String diaEsp = traducirDia(date.getDayOfWeek().name());
                if (!dias.contains(diaEsp)) continue;

                com.policlinico.entity.Medico medEntity = new com.policlinico.entity.Medico();
                medEntity.setId(medico.getId());

                com.policlinico.entity.Disponibilidad d = new com.policlinico.entity.Disponibilidad();
                d.setMedico(medEntity);
                d.setFecha(date);
                d.setDiaSemana(diaEsp);
                d.setActivo(true);
                d = disponibilidadRepositorio.save(d);

                java.time.LocalTime cursor = hiInicio;
                while (!cursor.plusMinutes(duracion).isAfter(hiFin)) {
                    java.time.LocalTime bloqueInicio = cursor;
                    java.time.LocalTime bloqueFin    = cursor.plusMinutes(duracion);
                    com.policlinico.entity.Horario h = new com.policlinico.entity.Horario();
                    h.setDisponibilidad(d);
                    h.setHoraInicio(bloqueInicio);
                    h.setHoraFin(bloqueFin);
                    h.setDisponible(true);
                    horarioRepositorio.save(h);
                    cursor = bloqueFin;
                }
            }
        }
        return new ModelAndView("redirect:/medico/disponibilidad");
    }

    private String traducirDia(String englishDay) {
        switch (englishDay) {
            case "MONDAY": return "LUNES";
            case "TUESDAY": return "MARTES";
            case "WEDNESDAY": return "MIERCOLES";
            case "THURSDAY": return "JUEVES";
            case "FRIDAY": return "VIERNES";
            case "SATURDAY": return "SABADO";
            case "SUNDAY": return "DOMINGO";
            default: return "";
        }
    }

    @GetMapping("/admin/lista")
    public ModelAndView listaAdmin(HttpSession session, Model model) {
        if (!rolAdmin(session)) return new ModelAndView("redirect:/login");
        cargarMedicos(model, new MedicoModelo(), false);
        return new ModelAndView("admin/medicos");
    }

    @GetMapping("/admin/ver/{id}")
    public ModelAndView verAdmin(@PathVariable Long id, HttpSession session, Model model) {
        if (!rolAdmin(session)) return new ModelAndView("redirect:/login");
        model.addAttribute("medicoDetalle", medicoServicio.buscar(id));
        cargarMedicos(model, new MedicoModelo(), false);
        return new ModelAndView("admin/medicos");
    }

    @GetMapping("/admin/editar/{id}")
    public ModelAndView editarAdmin(@PathVariable Long id, HttpSession session, Model model) {
        if (!rolAdmin(session)) return new ModelAndView("redirect:/login");
        cargarMedicos(model, medicoServicio.buscar(id), true);
        return new ModelAndView("admin/medicos");
    }

    @PostMapping("/admin/guardar")
    public ModelAndView guardar(
            @ModelAttribute MedicoModelo medico,
            @RequestParam(value = "activo", defaultValue = "true") String activo,
            HttpSession session,
            org.springframework.ui.Model model) {
        if (!rolAdmin(session)) return new ModelAndView("redirect:/login");
        medico.setActivo(Boolean.parseBoolean(activo));
        try {
            medicoServicio.guardar(medico);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            cargarMedicos(model, medico, medico.getId() != null);
            return new ModelAndView("admin/medicos");
        }
        return new ModelAndView("redirect:/medico/admin/lista");
    }

    @GetMapping("/admin/eliminar/{id}")
    public ModelAndView eliminarAdmin(@PathVariable Long id, HttpSession session) {
        if (!rolAdmin(session)) return new ModelAndView("redirect:/login");
        medicoServicio.eliminar(id);
        return new ModelAndView("redirect:/medico/admin/lista");
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

    private void cargarMedicos(Model model, MedicoModelo medico, boolean editando) {
        java.util.List<MedicoModelo> medicosReg = medicoServicio.listar();
        java.util.Set<Long> yaEsMedico = medicosReg.stream()
                .map(MedicoModelo::getUsuarioId)
                .collect(java.util.stream.Collectors.toSet());
        if (medico != null && medico.getId() != null && medico.getUsuarioId() != null) {
            yaEsMedico.remove(medico.getUsuarioId());
        }
        model.addAttribute("medicos", medicosReg);
        model.addAttribute("yaEsMedico", yaEsMedico);
        model.addAttribute("medicoFormulario", medico != null ? medico : new MedicoModelo());
        // Solo usuarios con rol MEDICO que aún no estén vinculados
        model.addAttribute("usuarios", usuarioServicio.listar().stream()
                .filter(u -> "MEDICO".equals(u.getRol()))
                .filter(u -> !yaEsMedico.contains(u.getId()))
                .collect(java.util.stream.Collectors.toList()));
        model.addAttribute("especialidades", especialidadServicio.listarActivas());
        model.addAttribute("editando", editando);
    }
    // Maneja los errores de este controlador y vuelve con un mensaje
    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public ModelAndView manejarError(Exception ex,
                                     org.springframework.web.servlet.mvc.support.RedirectAttributes ra) {
        String msg = (ex instanceof IllegalArgumentException && ex.getMessage() != null)
                ? ex.getMessage()
                : "No se pudo procesar la operacion del medico. Verifique los datos e intente de nuevo.";
        ra.addFlashAttribute("error", msg);
        return new ModelAndView("redirect:/medico/admin/lista");
    }
}

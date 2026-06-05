package com.policlinico.controller;

import com.policlinico.entity.Disponibilidad;
import com.policlinico.entity.Horario;
import com.policlinico.entity.Medico;
import com.policlinico.model.DisponibilidadModelo;
import com.policlinico.repository.DisponibilidadRepositorio;
import com.policlinico.repository.HorarioRepositorio;
import com.policlinico.service.DisponibilidadServicio;
import com.policlinico.service.MedicoServicio;
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
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@RestController
@RequestMapping("/disponibilidades")
public class DisponibilidadController {

    private final DisponibilidadServicio disponibilidadServicio;
    private final MedicoServicio medicoServicio;
    private final DisponibilidadRepositorio disponibilidadRepositorio;
    private final HorarioRepositorio horarioRepositorio;

    public DisponibilidadController(DisponibilidadServicio disponibilidadServicio,
                                    MedicoServicio medicoServicio,
                                    DisponibilidadRepositorio disponibilidadRepositorio,
                                    HorarioRepositorio horarioRepositorio) {
        this.disponibilidadServicio = disponibilidadServicio;
        this.medicoServicio = medicoServicio;
        this.disponibilidadRepositorio = disponibilidadRepositorio;
        this.horarioRepositorio = horarioRepositorio;
    }

    @GetMapping
    public ModelAndView listar(HttpSession session, Model model) {
        if (!rolAdmin(session)) return new ModelAndView("redirect:/login");
        cargar(model, new DisponibilidadModelo(), false);
        return new ModelAndView("admin/disponibilidades");
    }

    @GetMapping("/ver/{id}")
    public ModelAndView ver(@PathVariable Long id, HttpSession session, Model model) {
        if (!rolAdmin(session)) return new ModelAndView("redirect:/login");
        model.addAttribute("disponibilidadDetalle", disponibilidadServicio.buscar(id));
        cargar(model, new DisponibilidadModelo(), false);
        return new ModelAndView("admin/disponibilidades");
    }

    @GetMapping("/editar/{id}")
    public ModelAndView editar(@PathVariable Long id, HttpSession session, Model model) {
        if (!rolAdmin(session)) return new ModelAndView("redirect:/login");
        cargar(model, disponibilidadServicio.buscar(id), true);
        return new ModelAndView("admin/disponibilidades");
    }

    @PostMapping("/guardar")
    public ModelAndView guardar(@ModelAttribute DisponibilidadModelo disponibilidad, HttpSession session) {
        if (!rolAdmin(session)) return new ModelAndView("redirect:/login");
        disponibilidadServicio.guardar(disponibilidad);
        return new ModelAndView("redirect:/disponibilidades");
    }

    @GetMapping("/eliminar/{id}")
    public ModelAndView eliminar(@PathVariable Long id, HttpSession session) {
        if (!rolAdmin(session)) return new ModelAndView("redirect:/login");
        disponibilidadServicio.eliminar(id);
        return new ModelAndView("redirect:/disponibilidades");
    }

    /**
     * Genera disponibilidades + horarios en bloques para un médico/mes/rango.
     * Llamado desde admin/disponibilidades.jsp (paso 2).
     */
    @PostMapping("/guardar-mensual")
    public ModelAndView guardarMensual(
            @RequestParam Long medicoId,
            @RequestParam String mes,
            @RequestParam(required = false) List<String> dias,
            @RequestParam String horaInicio,
            @RequestParam String horaFin,
            @RequestParam(defaultValue = "30") int duracion,
            HttpSession session) {

        if (!rolAdmin(session)) return new ModelAndView("redirect:/login");

        if (dias == null || dias.isEmpty() || mes == null || mes.isBlank()) {
            return new ModelAndView("redirect:/disponibilidades");
        }

        String[] parts = mes.split("-");
        int anio   = Integer.parseInt(parts[0]);
        int mesNum = Integer.parseInt(parts[1]);

        // Solo se permite configurar el MES ACTUAL (ni pasados ni futuros)
        java.time.YearMonth mesSolicitado = java.time.YearMonth.of(anio, mesNum);
        java.time.YearMonth mesActual = java.time.YearMonth.now();
        if (!mesSolicitado.equals(mesActual)) {
            throw new IllegalArgumentException("Solo se puede configurar el mes actual (" + mesActual + ").");
        }

        generarDisponibilidadYHorarios(medicoId, anio, mesNum, dias, horaInicio, horaFin, duracion);

        return new ModelAndView("redirect:/disponibilidades");
    }

    // ── helpers ───────────────────────────────────────────────────────────────

    private void generarDisponibilidadYHorarios(Long medicoId, int anio, int mesNum,
                                                List<String> dias,
                                                String horaInicioStr, String horaFinStr,
                                                int duracionMin) {
        LocalDate startDate = LocalDate.of(anio, mesNum, 1);
        LocalDate endDate   = startDate.with(TemporalAdjusters.lastDayOfMonth());

        // Eliminar disponibilidades+horarios anteriores de ese médico en ese mes
        List<Disponibilidad> dispList = disponibilidadRepositorio.findByMedicoId(medicoId);
        for (Disponibilidad d : dispList) {
            if (d.getFecha().getYear() == anio && d.getFecha().getMonthValue() == mesNum) {
                List<Horario> hList = horarioRepositorio.findAll();
                for (Horario h : hList) {
                    if (h.getDisponibilidad().getId().equals(d.getId())) {
                        horarioRepositorio.delete(h);
                    }
                }
                disponibilidadRepositorio.delete(d);
            }
        }

        LocalTime hiInicio = LocalTime.parse(horaInicioStr);
        LocalTime hiFin    = LocalTime.parse(horaFinStr);

        // Generar nuevas (solo fechas posteriores a hoy)
        LocalDate hoyGen = LocalDate.now();
        for (LocalDate fecha = startDate; !fecha.isAfter(endDate); fecha = fecha.plusDays(1)) {
            if (!fecha.isAfter(hoyGen)) continue; // saltar hoy y días pasados
            String diaEsp = traducirDia(fecha.getDayOfWeek().name());
            if (!dias.contains(diaEsp)) continue;

            Medico medEntity = new Medico();
            medEntity.setId(medicoId);

            Disponibilidad d = new Disponibilidad();
            d.setMedico(medEntity);
            d.setFecha(fecha);
            d.setDiaSemana(diaEsp);
            d.setActivo(true);
            d = disponibilidadRepositorio.save(d);

            // Generar bloques
            LocalTime cursor = hiInicio;
            while (!cursor.plusMinutes(duracionMin).isAfter(hiFin)) {
                LocalTime bloqueInicio = cursor;
                LocalTime bloqueFin    = cursor.plusMinutes(duracionMin);

                Horario h = new Horario();
                h.setDisponibilidad(d);
                h.setHoraInicio(bloqueInicio);
                h.setHoraFin(bloqueFin);
                h.setDisponible(true);
                horarioRepositorio.save(h);

                cursor = bloqueFin;
            }
        }
    }

    private String traducirDia(String eng) {
        switch (eng) {
            case "MONDAY":    return "LUNES";
            case "TUESDAY":   return "MARTES";
            case "WEDNESDAY": return "MIERCOLES";
            case "THURSDAY":  return "JUEVES";
            case "FRIDAY":    return "VIERNES";
            case "SATURDAY":  return "SABADO";
            case "SUNDAY":    return "DOMINGO";
            default:          return "";
        }
    }

    private void cargar(Model model, DisponibilidadModelo disponibilidad, boolean editando) {
        model.addAttribute("disponibilidades", disponibilidadServicio.listar());
        model.addAttribute("medicos", medicoServicio.listar());
        model.addAttribute("disponibilidadFormulario", disponibilidad);
        model.addAttribute("editando", editando);
    }

    private boolean rolAdmin(HttpSession session) {
        Long usuarioId = (Long) session.getAttribute("usuarioId");
        return usuarioId != null && "ADMIN".equals(session.getAttribute("rol"));
    }

    // Maneja los errores de este controlador y vuelve a la pantalla con un mensaje
    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public ModelAndView manejarError(Exception ex,
                                     org.springframework.web.servlet.mvc.support.RedirectAttributes ra) {
        String msg = (ex instanceof IllegalArgumentException && ex.getMessage() != null)
                ? ex.getMessage()
                : "No se pudo procesar la disponibilidad. Verifique los datos e intente de nuevo.";
        ra.addFlashAttribute("error", msg);
        return new ModelAndView("redirect:/disponibilidades");
    }
}

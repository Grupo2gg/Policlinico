package com.policlinico.controller;

import com.policlinico.exception.CitaNotFoundException;
import com.policlinico.model.Cita;
import com.policlinico.model.Usuario;
import java.util.Arrays;
import java.util.List;
import com.policlinico.service.CitaService;
import com.policlinico.service.EspecialidadService;
import java.time.LocalDate;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
        if (estado != null && !estado.isBlank() && !"TODAS".equalsIgnoreCase(estado)) {
            model.addAttribute(
                    "citas",
                    citaService.obtenerPorEstado(estado).stream()
                            .filter(cita -> cita.getUsuarioId() == usuario.getId())
                            .toList());
        } else {
            model.addAttribute("citas", citaService.obtenerPorUsuario(usuario.getId()));
        }
        model.addAttribute("totalPendientes", citaService.obtenerPorEstado("PENDIENTE").stream()
                .filter(cita -> cita.getUsuarioId() == usuario.getId()).count());
        model.addAttribute("totalConfirmadas", citaService.obtenerPorEstado("CONFIRMADA").stream()
                .filter(cita -> cita.getUsuarioId() == usuario.getId()).count());
        model.addAttribute("totalCanceladas", citaService.obtenerPorEstado("CANCELADA").stream()
                .filter(cita -> cita.getUsuarioId() == usuario.getId()).count());
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
        model.addAttribute("especialidades", especialidadService.obtenerActivas());
        model.addAttribute("medicos", especialidadService.obtenerMedicos());
        model.addAttribute("horas", obtenerHorasDisponibles());
        model.addAttribute("cita", cita);
        model.addAttribute("hoy", LocalDate.now().toString());
        return "cita/form";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Cita cita, HttpSession session, Model model) {
        Usuario usuario = obtenerUsuarioSesion(session);
        if (usuario == null) {
            return "redirect:/login";
        }
        if (LocalDate.parse(cita.getFecha()).isBefore(LocalDate.now())) {
            model.addAttribute("error", "La fecha no puede ser en el pasado");
            model.addAttribute("especialidades", especialidadService.obtenerActivas());
            model.addAttribute("medicos", especialidadService.obtenerMedicos());
            model.addAttribute("horas", obtenerHorasDisponibles());
            model.addAttribute("cita", cita);
            model.addAttribute("hoy", LocalDate.now().toString());
            return "cita/form";
        }
        boolean existeDuplicada = citaService.obtenerPorUsuario(usuario.getId()).stream()
                .anyMatch(item -> item.getFecha().equals(cita.getFecha()) && item.getHora().equals(cita.getHora()));
        if (existeDuplicada) {
            model.addAttribute("error", "Ya tienes una cita registrada para la misma fecha y hora");
            model.addAttribute("especialidades", especialidadService.obtenerActivas());
            model.addAttribute("medicos", especialidadService.obtenerMedicos());
            model.addAttribute("horas", obtenerHorasDisponibles());
            model.addAttribute("cita", cita);
            model.addAttribute("hoy", LocalDate.now().toString());
            return "cita/form";
        }
        cita.setUsuarioId(usuario.getId());
        cita.setNombrePaciente(usuario.getNombre() + " " + usuario.getApellido());
        citaService.registrarCita(cita);
        return "redirect:/cita/list";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable int id, HttpSession session, Model model) {
        if (obtenerUsuarioSesion(session) == null) {
            return "redirect:/login";
        }
        model.addAttribute("especialidades", especialidadService.obtenerActivas());
        Cita cita = citaService.obtenerPorId(id);
        if (cita == null) {
            throw new CitaNotFoundException("La cita solicitada no fue encontrada.");
        }
        model.addAttribute("cita", cita);
        model.addAttribute("medicos", especialidadService.obtenerMedicos());
        model.addAttribute("horas", obtenerHorasDisponibles());
        model.addAttribute("hoy", LocalDate.now().toString());
        return "cita/form";
    }

    @PostMapping("/actualizar")
    public String actualizar(@ModelAttribute Cita cita, HttpSession session) {
        Usuario usuario = obtenerUsuarioSesion(session);
        if (usuario == null) {
            return "redirect:/login";
        }
        cita.setUsuarioId(usuario.getId());
        if (cita.getNombrePaciente() == null || cita.getNombrePaciente().isBlank()) {
            cita.setNombrePaciente(usuario.getNombre() + " " + usuario.getApellido());
        }
        citaService.actualizarCita(cita);
        return "redirect:/cita/list";
    }

    @GetMapping("/cancelar/{id}")
    public String cancelar(@PathVariable int id, HttpSession session) {
        if (obtenerUsuarioSesion(session) == null) {
            return "redirect:/login";
        }
        if (citaService.obtenerPorId(id) == null) {
            throw new CitaNotFoundException("La cita solicitada no fue encontrada.");
        }
        citaService.cancelarCita(id);
        return "redirect:/cita/list";
    }

    @GetMapping("/ver/{id}")
    public String ver(@PathVariable int id, HttpSession session, Model model) {
        if (obtenerUsuarioSesion(session) == null) {
            return "redirect:/login";
        }
        Cita cita = citaService.obtenerPorId(id);
        if (cita == null) {
            throw new CitaNotFoundException("La cita solicitada no fue encontrada.");
        }
        model.addAttribute("cita", cita);
        return "cita/detalle";
    }

    @GetMapping("/reporte")
    public String reporte(HttpSession session, Model model) {
        if (obtenerUsuarioSesion(session) == null) {
            return "redirect:/login";
        }
        model.addAttribute("citas", citaService.obtenerTodas());
        model.addAttribute("totalPendientes", citaService.obtenerPorEstado("PENDIENTE").size());
        model.addAttribute("totalConfirmadas", citaService.obtenerPorEstado("CONFIRMADA").size());
        model.addAttribute("totalCanceladas", citaService.obtenerPorEstado("CANCELADA").size());
        return "cita/reporte";
    }

    private Usuario obtenerUsuarioSesion(HttpSession session) {
        return (Usuario) session.getAttribute("usuario");
    }

    private List<String> obtenerHorasDisponibles() {
        return Arrays.asList("08:00", "09:00", "10:00", "11:00",
                "12:00", "14:00", "15:00", "16:00", "17:00");
    }
}

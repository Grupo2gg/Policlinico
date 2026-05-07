package com.policlinico.controller;

import com.policlinico.model.Cita;
import com.policlinico.model.Usuario;
import com.policlinico.service.CitaService;
import com.policlinico.service.EspecialidadService;
import com.policlinico.service.MedicoService;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
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
@RequestMapping("/cita")
public class CitaController {

    // Enlace del controlador con la capa de negocio de citas.
    @Autowired
    private CitaService citaService;

    // Se usa para alimentar listas de especialidades y medicos en la vista.
    @Autowired
    private EspecialidadService especialidadService;

    @Autowired
    private MedicoService medicoService;

    @GetMapping("/list")
    public String listar(@RequestParam(required = false) String estado, HttpSession session, Model model) {
        Usuario usuario = obtenerUsuarioSesion(session);
        if (usuario == null) {
            return "redirect:/login";
        }
        if (!"PACIENTE".equals(usuario.getRol())) {
            return "redirect:/login";
        }
        // Aqui ocurre el enlace con servicio: el controlador no consulta repositorios directos.
        List<Cita> citasUsuario = citaService.obtenerPorUsuario(usuario.getId());
        List<Cita> citasFiltradas = citasUsuario;
        if (estado != null && !estado.isBlank() && !"TODAS".equalsIgnoreCase(estado)) {
            citasFiltradas = citasUsuario.stream()
                    .filter(cita -> estado.equalsIgnoreCase(cita.getEstado()))
                    .collect(Collectors.toList());
        }
        // El modelo web se llena con datos que la JSP mostrara al usuario.
        model.addAttribute("citas", citasFiltradas);
        cargarResumen(model, citasUsuario);
        model.addAttribute("estadoSeleccionado", estado == null || estado.isBlank() ? "TODAS" : estado);
        // Devuelve el nombre logico de la JSP.
        return "cita/list";
    }

    @GetMapping("/nueva")
    public String nueva(@RequestParam(required = false) String especialidad, HttpSession session, Model model) {
        Usuario usuario = obtenerUsuarioSesion(session);
        if (usuario == null) {
            return "redirect:/login";
        }
        if (!"PACIENTE".equals(usuario.getRol())) {
            return "redirect:/login";
        }
        // El controlador prepara un modelo vacio para que la vista enlace los campos del formulario.
        Cita cita = new Cita();
        if (especialidad != null && !especialidad.isBlank()) {
            cita.setEspecialidad(especialidad);
        }
        cargarFormulario(model, cita);
        return "cita/form";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Cita cita, HttpSession session, Model model) {
        // Spring llena el modelo Cita con los datos enviados desde la JSP.
        Usuario usuario = obtenerUsuarioSesion(session);
        if (usuario == null) {
            return "redirect:/login";
        }
        if (!"PACIENTE".equals(usuario.getRol())) {
            return "redirect:/login";
        }
        // El controlador completa datos confiables desde sesion, no desde el formulario.
        cita.setUsuarioId(usuario.getId());
        cita.setNombrePaciente(usuario.getNombre() + " " + usuario.getApellido());
        try {
            // Enlace con negocio: el servicio valida, aplica reglas y persiste.
            citaService.registrarCita(cita);
        } catch (IllegalArgumentException ex) {
            // Si negocio rechaza la operacion, el controlador vuelve a cargar la vista con el error.
            model.addAttribute("error", ex.getMessage());
            cargarFormulario(model, cita);
            return "cita/form";
        }
        return "redirect:/cita/list";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable int id, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        Usuario usuario = obtenerUsuarioSesion(session);
        if (usuario == null) {
            return "redirect:/login";
        }
        if (!"PACIENTE".equals(usuario.getRol())) {
            return "redirect:/login";
        }
        // Pide al servicio una cita valida para ese usuario y la manda a la JSP.
        Cita cita = citaService.obtenerPorIdDeUsuario(id, usuario.getId());
        if (cita == null) {
            return mostrarNoEncontrado(redirectAttributes, "La cita solicitada no fue encontrada.");
        }
        cargarFormulario(model, cita);
        return "cita/form";
    }

    @PostMapping("/actualizar")
    public String actualizar(@ModelAttribute Cita cita, HttpSession session, Model model) {
        Usuario usuario = obtenerUsuarioSesion(session);
        if (usuario == null) {
            return "redirect:/login";
        }
        if (!"PACIENTE".equals(usuario.getRol())) {
            return "redirect:/login";
        }
        // Se vuelve a completar la identidad del propietario para evitar manipulacion desde la vista.
        cita.setUsuarioId(usuario.getId());
        cita.setNombrePaciente(usuario.getNombre() + " " + usuario.getApellido());
        try {
            citaService.actualizarCita(cita);
        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());
            cargarFormulario(model, cita);
            return "cita/form";
        }
        return "redirect:/cita/list";
    }

    @GetMapping("/cancelar/{id}")
    public String cancelar(@PathVariable int id, HttpSession session) {
        Usuario usuario = obtenerUsuarioSesion(session);
        if (usuario == null) {
            return "redirect:/login";
        }
        if (!"PACIENTE".equals(usuario.getRol())) {
            return "redirect:/login";
        }
        // El controlador solo dispara el caso de uso; la regla de cancelacion vive en servicio.
        citaService.cancelarCita(id, usuario.getId());
        return "redirect:/cita/list";
    }

    @GetMapping("/ver/{id}")
    public String ver(@PathVariable int id, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        Usuario usuario = obtenerUsuarioSesion(session);
        if (usuario == null) {
            return "redirect:/login";
        }
        if (!"PACIENTE".equals(usuario.getRol())) {
            return "redirect:/login";
        }
        Cita cita = citaService.obtenerPorIdDeUsuario(id, usuario.getId());
        if (cita == null) {
            return mostrarNoEncontrado(redirectAttributes, "La cita solicitada no fue encontrada.");
        }
        model.addAttribute("cita", cita);
        return "cita/detalle";
    }

    private Usuario obtenerUsuarioSesion(HttpSession session) {
        return (Usuario) session.getAttribute("usuario");
    }

    private void cargarFormulario(Model model, Cita cita) {
        // Este metodo arma todos los datos que la JSP necesita para renderizarse.
        model.addAttribute("especialidades", especialidadService.obtenerActivas());
        model.addAttribute("medicos", medicoService.obtenerActivos());
        model.addAttribute("horas", citaService.obtenerHorasDisponibles());
        model.addAttribute("cita", cita);
        model.addAttribute("hoy", LocalDate.now().toString());
    }

    private void cargarResumen(Model model, List<Cita> citas) {
        // Calcula datos de apoyo para la vista de listado.
        model.addAttribute("totalCitas", citas.size());
        model.addAttribute("totalPendientes", contarPorEstado(citas, "PENDIENTE"));
        model.addAttribute("totalConfirmadas", contarPorEstado(citas, "CONFIRMADA"));
        model.addAttribute("totalCanceladas", contarPorEstado(citas, "CANCELADA"));
    }

    private long contarPorEstado(List<Cita> citas, String estado) {
        return citas.stream()
                .filter(cita -> estado.equalsIgnoreCase(cita.getEstado()))
                .count();
    }

    private String mostrarNoEncontrado(RedirectAttributes redirectAttributes, String mensaje) {
        redirectAttributes.addFlashAttribute("mensaje", mensaje);
        return "redirect:/cita/list";
    }
}

package com.policlinico.controller;

import com.policlinico.service.CitaServicio;
import com.policlinico.service.AtencionServicio;
import com.policlinico.service.EspecialidadServicio;
import com.policlinico.service.HorarioServicio;
import com.policlinico.service.MedicoServicio;
import com.policlinico.service.UsuarioServicio;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final UsuarioServicio usuarioServicio;
    private final MedicoServicio medicoServicio;
    private final EspecialidadServicio especialidadServicio;
    private final HorarioServicio horarioServicio;
    private final CitaServicio citaServicio;
    private final AtencionServicio atencionServicio;

    public AdminController(UsuarioServicio usuarioServicio, MedicoServicio medicoServicio, EspecialidadServicio especialidadServicio, HorarioServicio horarioServicio, CitaServicio citaServicio, AtencionServicio atencionServicio) {
        this.usuarioServicio = usuarioServicio;
        this.medicoServicio = medicoServicio;
        this.especialidadServicio = especialidadServicio;
        this.horarioServicio = horarioServicio;
        this.citaServicio = citaServicio;
        this.atencionServicio = atencionServicio;
    }

    @GetMapping("/inicio")
    public ModelAndView inicio(HttpSession session, Model model) {
        if (!rolValido(session)) return new ModelAndView("redirect:/login");
        model.addAttribute("totalUsuarios", usuarioServicio.contar());
        model.addAttribute("totalMedicos", medicoServicio.contar());
        model.addAttribute("totalEspecialidades", especialidadServicio.contar());
        model.addAttribute("totalHorarios", horarioServicio.contar());
        model.addAttribute("totalCitas", citaServicio.contar());
        model.addAttribute("totalAtenciones", atencionServicio.contar());

        // Métricas para las gráficas (calculadas desde la BD)
        java.util.List<com.policlinico.model.CitaModelo> citas = citaServicio.listar();
        model.addAttribute("citasPendientes", citas.stream().filter(c -> "PENDIENTE".equalsIgnoreCase(c.getEstado())).count());
        model.addAttribute("citasConfirmadas", citas.stream().filter(c -> "CONFIRMADA".equalsIgnoreCase(c.getEstado())).count());
        model.addAttribute("citasAtendidas", citas.stream().filter(c -> "ATENDIDA".equalsIgnoreCase(c.getEstado())).count());
        model.addAttribute("citasCanceladas", citas.stream().filter(c -> "CANCELADA".equalsIgnoreCase(c.getEstado())).count());

        long medicosActivos = medicoServicio.listar().stream().filter(m -> Boolean.TRUE.equals(m.getActivo())).count();
        model.addAttribute("medicosActivos", medicosActivos);
        model.addAttribute("medicosInactivos", medicoServicio.contar() - medicosActivos);

        java.util.List<com.policlinico.model.UsuarioModelo> usuarios = usuarioServicio.listar();
        model.addAttribute("usuariosAdmin", usuarios.stream().filter(u -> "ADMIN".equals(u.getRol())).count());
        model.addAttribute("usuariosMedico", usuarios.stream().filter(u -> "MEDICO".equals(u.getRol())).count());
        model.addAttribute("usuariosPaciente", usuarios.stream().filter(u -> "PACIENTE".equals(u.getRol())).count());

        return new ModelAndView("admin/inicio");
    }

    private boolean rolValido(HttpSession session) {
        Long usuarioId = (Long) session.getAttribute("usuarioId");
        return usuarioId != null && "ADMIN".equals(session.getAttribute("rol"));
    }
}

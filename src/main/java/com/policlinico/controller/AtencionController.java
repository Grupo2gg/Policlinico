package com.policlinico.controller;

import com.policlinico.model.Atencion;
import com.policlinico.model.Cita;
import com.policlinico.service.AtencionService;
import com.policlinico.service.CitaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/medico/atenciones")
public class AtencionController {

    @Autowired
    private CitaService citaService;

    @Autowired
    private AtencionService atencionService;

    @GetMapping
    public String listarCitas(Model model) {
        // Obtenemos todas las citas. Lo mas sencillo para el listado.
        List<Cita> citas = citaService.obtenerTodas();
        model.addAttribute("citas", citas);
        return "medico/atenciones";
    }

    @GetMapping("/nueva/{citaId}")
    public String nuevaAtencion(@PathVariable("citaId") int citaId, Model model) {
        Cita cita = citaService.obtenerPorId(citaId);
        Atencion atencion = new Atencion();
        atencion.setCitaId(citaId);
        
        model.addAttribute("cita", cita);
        model.addAttribute("atencion", atencion);
        
        return "medico/atencion_form";
    }

    @PostMapping("/guardar")
    public String guardarAtencion(@ModelAttribute Atencion atencion) {
        // Guardar la atencion
        atencionService.registrar(atencion);
        
        // Cambiar estado de la cita
        Cita cita = citaService.obtenerPorId(atencion.getCitaId());
        if(cita != null) {
            cita.setEstado("ATENDIDO");
            citaService.actualizarCita(cita);
        }
        
        return "redirect:/medico/atenciones";
    }
}

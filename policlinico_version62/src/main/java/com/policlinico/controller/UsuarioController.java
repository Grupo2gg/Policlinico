package com.policlinico.controller;

import com.policlinico.model.UsuarioModelo;
import com.policlinico.service.UsuarioServicio;
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
@RequestMapping
public class UsuarioController {
    private final UsuarioServicio usuarioServicio;

    public UsuarioController(UsuarioServicio usuarioServicio) {
        this.usuarioServicio = usuarioServicio;
    }

    @GetMapping("/")
    public ModelAndView raiz(HttpSession session) {
        if (session.getAttribute("usuarioId") == null) return new ModelAndView("redirect:/login");
        return redirigirPorRol((String) session.getAttribute("rol"));
    }

    @GetMapping("/login")
    public ModelAndView login(HttpSession session) {
        if (session.getAttribute("usuarioId") != null) return redirigirPorRol((String) session.getAttribute("rol"));
        return new ModelAndView("autenticacion/login");
    }

    @PostMapping("/login")
    public ModelAndView autenticar(String email, String password, HttpSession session, Model model) {
        UsuarioModelo usuario = usuarioServicio.iniciarSesion(email, password);
        if (usuario == null) return errorLogin(model);
        guardarSesion(session, usuario);
        return redirigirPorRol(usuario.getRol());
    }

    @GetMapping("/registro")
    public ModelAndView registro(HttpSession session, Model model) {
        if (session.getAttribute("usuarioId") != null) return redirigirPorRol((String) session.getAttribute("rol"));
        model.addAttribute("usuario", new UsuarioModelo());
        return new ModelAndView("autenticacion/registro");
    }

    @PostMapping("/registro")
    public ModelAndView guardarRegistro(@ModelAttribute UsuarioModelo usuario) {
        if (!usuarioServicio.esGmail(usuario.getEmail())) {
            ModelAndView vista = new ModelAndView("autenticacion/registro");
            vista.addObject("error", "Solo se permite registro con correo Gmail");
            vista.addObject("usuario", usuario);
            return vista;
        }
        usuario.setRol("PACIENTE");
        usuarioServicio.guardar(usuario);
        return new ModelAndView("redirect:/login");
    }

    @PostMapping("/logout")
    public ModelAndView salir(HttpSession session) {
        session.invalidate();
        return new ModelAndView("redirect:/login");
    }

    @GetMapping("/logout")
    public ModelAndView salirGet(HttpSession session) {
        session.invalidate();
        return new ModelAndView("redirect:/login");
    }

    @GetMapping("/admin/usuarios")
    public ModelAndView usuarios(HttpSession session, Model model) {
        if (!rolAdmin(session)) return new ModelAndView("redirect:/login");
        cargarUsuarios(model, new UsuarioModelo(), false);
        return new ModelAndView("admin/usuarios");
    }

    @GetMapping("/admin/usuarios/ver/{id}")
    public ModelAndView verUsuario(@PathVariable Long id, HttpSession session, Model model) {
        if (!rolAdmin(session)) return new ModelAndView("redirect:/login");
        model.addAttribute("usuarioDetalle", usuarioServicio.buscar(id));
        cargarUsuarios(model, new UsuarioModelo(), false);
        return new ModelAndView("admin/usuarios");
    }

    @GetMapping("/admin/usuarios/editar/{id}")
    public ModelAndView editarUsuario(@PathVariable Long id, HttpSession session, Model model) {
        if (!rolAdmin(session)) return new ModelAndView("redirect:/login");
        cargarUsuarios(model, usuarioServicio.buscar(id), true);
        return new ModelAndView("admin/usuarios");
    }

    @PostMapping("/admin/usuarios/guardar")
    public ModelAndView guardarUsuario(@ModelAttribute UsuarioModelo usuario, HttpSession session, Model model) {
        if (!rolAdmin(session)) return new ModelAndView("redirect:/login");

        // 1. Validar que el formato de correo sea Gmail
        if (!usuarioServicio.esGmail(usuario.getEmail())) {
            model.addAttribute("error", "Solo se permite registro con correo Gmail");
            cargarUsuarios(model, usuario, usuario.getId() != null);
            return new ModelAndView("admin/usuarios");
        }

        // 2. VALIDACIÓN DE DUPLICADOS (Solo aplica si es un registro NUEVO, es decir, id == null)
        if (usuario.getId() == null) {

            // Comprobar si el DNI ya existe
            if (usuarioServicio.existePorDni(usuario.getDni())) {
                model.addAttribute("error", "El DNI '" + usuario.getDni() + "' ya se encuentra registrado por otro usuario.");
                cargarUsuarios(model, usuario, false);
                return new ModelAndView("admin/usuarios");
            }

            // Comprobar si el Correo ya existe
            if (usuarioServicio.existePorEmail(usuario.getEmail())) {
                model.addAttribute("error", "El correo electrónico '" + usuario.getEmail() + "' ya está siendo utilizado.");
                cargarUsuarios(model, usuario, false);
                return new ModelAndView("admin/usuarios");
            }
        }

        // 3. Si no hay duplicados, guarda con normalidad
        usuarioServicio.guardar(usuario);
        return new ModelAndView("redirect:/admin/usuarios");
    }

    @GetMapping("/admin/usuarios/eliminar/{id}")
    public ModelAndView eliminarUsuario(@PathVariable Long id, HttpSession session) {
        if (!rolAdmin(session)) return new ModelAndView("redirect:/login");
        usuarioServicio.eliminar(id);
        return new ModelAndView("redirect:/admin/usuarios");
    }

    @GetMapping("/mi-perfil")
    public ModelAndView perfil(HttpSession session, Model model) {
        if (!rolAutenticado(session)) return new ModelAndView("redirect:/login");
        Long usuarioId = (Long) session.getAttribute("usuarioId");
        model.addAttribute("usuario", usuarioServicio.buscar(usuarioId));
        return new ModelAndView("compartido/perfil");
    }

    @PostMapping("/mi-perfil")
    public ModelAndView actualizarMiPerfil(@ModelAttribute UsuarioModelo usuario, HttpSession session, Model model) {
        if (!rolAutenticado(session)) return new ModelAndView("redirect:/login");
        usuario.setId((Long) session.getAttribute("usuarioId"));
        UsuarioModelo usuarioActualizado = usuarioServicio.actualizarPerfil(usuario);
        guardarSesion(session, usuarioActualizado);
        model.addAttribute("usuario", usuarioActualizado);
        model.addAttribute("exito", "Datos actualizados correctamente");
        return new ModelAndView("compartido/perfil");
    }

    @GetMapping("/paciente/inicio")
    public ModelAndView inicioPaciente(HttpSession session) {
        if (!rolPaciente(session)) return new ModelAndView("redirect:/login");
        return new ModelAndView("paciente/inicio");
    }

    @GetMapping("/paciente/mi-perfil")
    public ModelAndView perfilPaciente(HttpSession session, Model model) {
        if (!rolPaciente(session)) return new ModelAndView("redirect:/login");
        return perfil(session, model);
    }

    @PostMapping("/paciente/mi-perfil")
    public ModelAndView actualizarPerfil(@ModelAttribute UsuarioModelo usuario, HttpSession session, Model model) {
        if (!rolPaciente(session)) return new ModelAndView("redirect:/login");
        return actualizarMiPerfil(usuario, session, model);
    }

    @GetMapping("/contacto")
    public ModelAndView contacto() {
        return new ModelAndView("paciente/contacto");
    }

    @GetMapping("/promociones")
    public ModelAndView promociones() {
        return new ModelAndView("paciente/promociones");
    }

    private ModelAndView errorLogin(Model model) {
        model.addAttribute("error", "Ingrese un Gmail y password validos");
        return new ModelAndView("autenticacion/login");
    }

    private void guardarSesion(HttpSession session, UsuarioModelo usuario) {
        session.setAttribute("usuarioId", usuario.getId());
        session.setAttribute("rol", usuario.getRol());
        session.setAttribute("nombreUsuario", usuario.getNombre());
    }

    private boolean rolAdmin(HttpSession session) {
        Long usuarioId = (Long) session.getAttribute("usuarioId");
        return usuarioId != null && "ADMIN".equals(session.getAttribute("rol"));
    }

    private boolean rolAutenticado(HttpSession session) {
        return session.getAttribute("usuarioId") != null;
    }

    private boolean rolPaciente(HttpSession session) {
        Long usuarioId = (Long) session.getAttribute("usuarioId");
        return usuarioId != null && "PACIENTE".equals(session.getAttribute("rol"));
    }

    private void cargarUsuarios(Model model, UsuarioModelo usuario, boolean editando) {
        model.addAttribute("usuarios", usuarioServicio.listar());
        model.addAttribute("usuarioFormulario", usuario);
        model.addAttribute("editando", editando);
    }

    private ModelAndView redirigirPorRol(String rol) {
        if ("ADMIN".equals(rol)) return new ModelAndView("redirect:/admin/inicio");
        if ("MEDICO".equals(rol)) return new ModelAndView("redirect:/medico/inicio");
        return new ModelAndView("redirect:/paciente/inicio");
    }
}

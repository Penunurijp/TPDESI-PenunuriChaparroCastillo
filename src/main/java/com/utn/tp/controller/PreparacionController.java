package com.utn.tp.controller;

import com.utn.tp.model.Preparacion;
import com.utn.tp.service.IRecetaService;
import com.utn.tp.service.PreparacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/preparaciones")
public class PreparacionController {

    @Autowired
    private PreparacionService preparacionService;

    @Autowired
    private IRecetaService recetaService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("preparaciones", preparacionService.listar());
        return "preparaciones/listado";
    }

    @GetMapping("/nueva")
    public String nueva(Model model) {
        model.addAttribute("preparacion", new Preparacion());
        model.addAttribute("recetas", recetaService.listarRecetas());
        return "preparaciones/form";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Preparacion p, RedirectAttributes redir) {
        try {
            preparacionService.guardar(p);
            redir.addFlashAttribute("exito", "Preparación guardada correctamente");
        } catch (Exception e) {
            redir.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/preparaciones";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("preparacion", preparacionService.buscarPorId(id));
        return "preparaciones/editar";
    }

    @PostMapping("/modificar")
    public String modificar(@ModelAttribute Preparacion p) throws Exception {
        preparacionService.actualizarFecha(p);
        return "redirect:/preparaciones";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        preparacionService.eliminar(id);
        return "redirect:/preparaciones";
    }
}

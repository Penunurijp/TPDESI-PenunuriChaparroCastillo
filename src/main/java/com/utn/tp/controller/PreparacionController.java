package com.utn.tp.controller;

import com.utn.tp.model.Preparacion;
import com.utn.tp.repository.RecetaRepository;
import com.utn.tp.service.PreparacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/preparaciones")
public class PreparacionController {

    @Autowired
    private PreparacionService preparacionService;

    @Autowired
    private RecetaRepository recetaRepository;

    @GetMapping
    public String listar(@RequestParam(required = false) String nombre,
                         @RequestParam(required = false) String fecha,
                         Model model) {

        List<Preparacion> preparaciones = preparacionService.listar();

        // Filtro por nombre de receta
        if (nombre != null && !nombre.isEmpty()) {
            preparaciones = preparaciones.stream()
                    .filter(p -> p.getReceta().getNombre().toLowerCase().contains(nombre.toLowerCase()))
                    .toList();
        }

        // Filtro por fecha
        if (fecha != null && !fecha.isEmpty()) {
            try {
                LocalDate fechaBuscar = LocalDate.parse(fecha);
                preparaciones = preparaciones.stream()
                        .filter(p -> p.getFecha().equals(fechaBuscar))
                        .toList();
            } catch (Exception e) {
                model.addAttribute("error", "Formato de fecha inválido. Use yyyy-MM-dd");
            }
        }

        model.addAttribute("preparaciones", preparaciones);
        model.addAttribute("caloriasPorPlato", preparacionService::calcularCaloriasPorPlato);

        return "preparaciones/listado";
    }

    @GetMapping("/nueva")
    public String nueva(Model model) {
        model.addAttribute("preparacion", new Preparacion());
        model.addAttribute("recetas", recetaRepository.findAll());
        return "preparaciones/form";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Preparacion preparacion, Model model) {
        try {
            preparacionService.guardar(preparacion);
            return "redirect:/preparaciones";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("preparacion", preparacion);
            model.addAttribute("recetas", recetaRepository.findAll());
            return "preparaciones/form";
        }
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("preparacion", preparacionService.buscarPorId(id));
        return "preparaciones/editar";
    }

    @PostMapping("/modificar")
    public String modificar(@ModelAttribute Preparacion preparacion, Model model) {
        try {
            preparacionService.actualizarFecha(preparacion);
            return "redirect:/preparaciones";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("preparacion", preparacion);
            return "preparaciones/editar";
        }
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        preparacionService.eliminar(id);
        return "redirect:/preparaciones";
    }
}



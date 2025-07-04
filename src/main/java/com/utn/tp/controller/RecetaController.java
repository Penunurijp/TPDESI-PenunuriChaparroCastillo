package com.utn.tp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.utn.tp.model.Receta;
import com.utn.tp.service.IRecetaService;

@Controller
@RequestMapping("/recetas")
public class RecetaController {

	@Autowired
	private IRecetaService recetaService;

	@GetMapping("/inicio")
	private String mostrarInicio() {
		return "recetas/MenuRecetas";
	}

	@GetMapping("/listar")
	private String listarRecetas(Model model) {
		List<Receta> recetas = recetaService.listarRecetas();
		model.addAttribute("recetas", recetas);
		return "recetas/Receta";
	}

	@GetMapping("/nueva")
	private String listarRecestas() {
		return "recetas/MenuAltaReceta";
	}

	@PostMapping("/guardar")
	private String guardarReceta(@ModelAttribute Receta receta, Model model) {
		recetaService.save(receta);
		model.addAttribute("mensajeExitoso", "Receta guardada con éxito");
		return "recetas/MenuAltaReceta";
	}

	@GetMapping("/modificar")
	private String listarRecetasModificar(Model model) {
		model.addAttribute("recetas", recetaService.listarRecetas());
		return "recetas/ModificarReceta";
	}

	@PostMapping("/modificar")
	private String modificarReceta(@ModelAttribute Receta receta, RedirectAttributes redirectAttributes) {
		recetaService.save(receta);
		redirectAttributes.addFlashAttribute("mensajeExitoso", "Receta actualizada con éxito");
		return "redirect:/recetas/modificar";
	}

	@GetMapping("/eliminar")
	private String listarRecetasEliminar(Model model) {
		model.addAttribute("recetas", recetaService.listarRecetas());
		return "recetas/EliminarReceta";
	}

	@PostMapping("/eliminar")
	private String eliminarReceta(RedirectAttributes redirectAttributes, @RequestParam long id) {
		recetaService.delete(id);
		redirectAttributes.addFlashAttribute("mensajeExitoso", "Receta eliminada con éxito");
		return "redirect:/recetas/eliminar";
	}

}

package com.utn.tp.controller;

import java.util.ArrayList;
import java.util.List;

import com.utn.tp.model.Ingrediente;
import com.utn.tp.service.IngredienteService;
import com.utn.tp.service.PreparacionService;
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

	@Autowired
	private IngredienteService ingredienteService;

	@GetMapping("/inicio")
	private String mostrarInicio() {
		return "recetas/MenuRecetas";
	}

	@GetMapping("/listar")
	private String listarRecetas(Model model) {
		List<Receta> recetas = recetaService.findByActivoTrue();
		model.addAttribute("recetas", recetas);
		return "recetas/Receta";
	}

	@GetMapping("/filtrar")
	public String filtrar(@RequestParam(required = false) String nombre,
						  @RequestParam(required = false) Integer minCalorias,
						  @RequestParam(required = false) Integer maxCalorias,
						  Model model) {
		List<Receta> recetas = recetaService.buscarFiltradas(nombre, minCalorias, maxCalorias);
		model.addAttribute("recetas", recetas);
		return "recetas/Receta";
	}

	@GetMapping("/nueva")
	private String listarRecestas(Model model) {
			List<Ingrediente> listaIngredientes = ingredienteService.listarIngredientes();;
			List<Ingrediente> listaIngredientesConStock = new ArrayList<Ingrediente>();
			for(Ingrediente ing : listaIngredientes){
				if(ing.getStock() > 0){
					listaIngredientesConStock.add(ing);
				}
			}
			model.addAttribute("ingredientes", listaIngredientesConStock);
			model.addAttribute("receta", new Receta()); // si usás th:object
			return "recetas/MenuAltaReceta";
		}

	@PostMapping("/guardar")
	private String guardarReceta(@ModelAttribute Receta receta, @ModelAttribute List<Ingrediente> ingredientes, Model model) {
		if(recetaService.findByNombre(receta.getNombre()).isEmpty()) {
			int cantCalorias = 0;
			//El HTML no me está trayendo el model con los ingredientes y por eso rompe
			for(Ingrediente ing : ingredientes) {
				cantCalorias+= ing.getCalorias();
			}
			receta.setCalorias(cantCalorias);
			receta.setIngredientes(ingredientes);
			receta.setActivo(true);
			recetaService.save(receta);
			model.addAttribute("mensajeExitoso", "Receta guardada con éxito");
		}else{
			model.addAttribute("mensajeError", "Receta ya existe");
		}
		return "recetas/MenuAltaReceta";
	}

	@GetMapping("/modificar")
	private String listarRecetasModificar(Model model) {
		model.addAttribute("recetas", recetaService.findByActivoTrue());
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
		model.addAttribute("recetas", recetaService.findByActivoTrue());
		return "recetas/EliminarReceta";
	}

	@PostMapping("/eliminar")
	private String eliminarReceta(RedirectAttributes redirectAttributes, @RequestParam long id) {
		Receta receta = recetaService.findById(id);
		receta.setActivo(false);
		recetaService.save(receta);
		redirectAttributes.addFlashAttribute("mensajeExitoso", "Receta eliminada con éxito");
		return "redirect:/recetas/eliminar";
	}

}

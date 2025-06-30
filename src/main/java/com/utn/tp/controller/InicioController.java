package com.utn.tp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InicioController {
	@GetMapping("/")
	private String mostrarInicio(Model model) {
		return "Inicio";
	}
}

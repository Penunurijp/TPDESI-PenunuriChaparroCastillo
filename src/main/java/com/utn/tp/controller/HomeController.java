package com.utn.tp.controller;


import com.utn.tp.service.FamiliaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    
    @Autowired
    private FamiliaService familiaService;
    
    @GetMapping("/inicioFamilia")
    public String home(Model model) {
        long totalFamilias = familiaService.contarFamiliasActivas();
        model.addAttribute("totalFamilias", totalFamilias);
        return "inicioFamilia";
    }
}

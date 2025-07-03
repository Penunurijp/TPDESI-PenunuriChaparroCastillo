package com.utn.tp.controller;

import com.utn.tp.model.Asistido;
import com.utn.tp.model.Familia;
import com.utn.tp.model.Ocupacion;
import com.utn.tp.service.AsistidoService;
import com.utn.tp.service.FamiliaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDate;
import java.util.Optional;

@Controller
@RequestMapping("/integrantes")
public class IntegranteController {
    
    @Autowired
    private AsistidoService asistidoService;
    
    @Autowired
    private FamiliaService familiaService;
    
    /**
     * Mostrar formulario para agregar integrante a una familia existente
     */
    @GetMapping("/familia/{nroFamilia}/nuevo")
    public String mostrarFormularioNuevoIntegrante(@PathVariable Long nroFamilia, Model model) {
        Optional<Familia> familiaOpt = familiaService.buscarFamiliaActiva(nroFamilia);
        
        if (familiaOpt.isEmpty()) {
            return "redirect:/familias";
        }
        
        Asistido asistido = new Asistido();
        asistido.setFamilia(familiaOpt.get());
        
        model.addAttribute("asistido", asistido);
        model.addAttribute("familia", familiaOpt.get());
        model.addAttribute("ocupaciones", Ocupacion.values());
        
        return "integrantes/formulario";
    }
    
    /**
     * Guardar nuevo integrante en familia existente
     */
    @PostMapping("/guardar")
    public String guardarIntegrante(@Valid @ModelAttribute Asistido asistido,
                                   BindingResult result,
                                   RedirectAttributes redirectAttributes,
                                   Model model) {
        
        if (result.hasErrors()) {
            model.addAttribute("ocupaciones", Ocupacion.values());
            return "integrantes/formulario";
        }
        
        try {
            // Validar DNI único
            if (asistidoService.existePorDni(asistido.getDni())) {
                redirectAttributes.addFlashAttribute("mensaje", 
                    "Ya existe una persona con DNI: " + asistido.getDni());
                redirectAttributes.addFlashAttribute("tipoMensaje", "error");
                return "redirect:/integrantes/familia/" + asistido.getFamilia().getNroFamilia() + "/nuevo";
            }
            
            asistidoService.agregarIntegrante(asistido);
            redirectAttributes.addFlashAttribute("mensaje", 
                "Integrante " + asistido.getNombreCompleto() + " agregado exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
            
            return "redirect:/familias/" + asistido.getFamilia().getNroFamilia();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al agregar integrante: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
            return "redirect:/familias/" + asistido.getFamilia().getNroFamilia();
        }
    }
    
    /**
     * Eliminar integrante (eliminación lógica)
     */
    @PostMapping("/{dni}/eliminar")
    public String eliminarIntegrante(@PathVariable Long dni, 
                                   @RequestParam Long nroFamilia,
                                   RedirectAttributes redirectAttributes) {
        try {
            familiaService.eliminarIntegrante(dni);
            redirectAttributes.addFlashAttribute("mensaje", 
                "Integrante eliminado exitosamente (eliminación lógica)");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }
        
        return "redirect:/familias/" + nroFamilia;
    }
}

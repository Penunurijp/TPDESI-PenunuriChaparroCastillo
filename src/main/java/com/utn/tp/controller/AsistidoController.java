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
import java.util.Optional;
import java.time.LocalDate;

@Controller
@RequestMapping("/asistidos")
public class AsistidoController {
    
    @Autowired
    private AsistidoService asistidoService;
    
    @Autowired
    private FamiliaService familiaService;
    
    /**
     * Mostrar formulario para agregar integrante a una familia existente
     */
    @GetMapping("/familia/{nroFamilia}/nuevo")
    public String mostrarFormularioNuevoIntegrante(@PathVariable Long nroFamilia, Model model, RedirectAttributes redirectAttributes) {
        Optional<Familia> familiaOpt = familiaService.buscarFamiliaActiva(nroFamilia);
        
        if (familiaOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("mensaje", "❌ La familia no existe o está inactiva");
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
            return "redirect:/familias";
        }
        
        Asistido asistido = new Asistido();
        asistido.setFamilia(familiaOpt.get());
        // Asegurar que la fecha esté establecida desde el inicio
        asistido.setFechaRegistro(LocalDate.now());
        
        model.addAttribute("asistido", asistido);
        model.addAttribute("familia", familiaOpt.get());
        model.addAttribute("ocupaciones", Ocupacion.values());
        
        return "asistidos/formulario";
    }
    
    /**
     * Guardar nuevo integrante en familia existente
     */
    @PostMapping("/guardar")
    public String guardarIntegrante(@ModelAttribute Asistido asistido,
                                   BindingResult result,
                                   RedirectAttributes redirectAttributes,
                                   Model model) {
        
        // Verificar que la familia existe y está activa
        if (asistido.getFamilia() == null || asistido.getFamilia().getNroFamilia() == null) {
            redirectAttributes.addFlashAttribute("mensaje", "❌ Error: Familia no especificada");
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
            return "redirect:/familias";
        }
        
        Long nroFamilia = asistido.getFamilia().getNroFamilia();
        Optional<Familia> familiaOpt = familiaService.buscarFamiliaActiva(nroFamilia);
        
        if (familiaOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("mensaje", "❌ Error: La familia no existe o está inactiva");
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
            return "redirect:/familias";
        }
        
        // Asegurar que el objeto familia esté completo
        asistido.setFamilia(familiaOpt.get());

        // ESTABLECER FECHA ACTUAL AUTOMÁTICAMENTE
        asistido.setFechaRegistro(LocalDate.now());
        asistido.setActivo(true);

        // Ahora validar manualmente los campos principales
        if (asistido.getDni() == null || asistido.getDni() <= 0) {
            redirectAttributes.addFlashAttribute("mensaje", "❌ Error: DNI es obligatorio y debe ser positivo");
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
            return "redirect:/asistidos/familia/" + nroFamilia + "/nuevo";
        }
        
        if (asistido.getApellido() == null || asistido.getApellido().trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("mensaje", "❌ Error: Apellido es obligatorio");
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
            return "redirect:/asistidos/familia/" + nroFamilia + "/nuevo";
        }
        
        if (asistido.getNombre() == null || asistido.getNombre().trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("mensaje", "❌ Error: Nombre es obligatorio");
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
            return "redirect:/asistidos/familia/" + nroFamilia + "/nuevo";
        }
        
        if (asistido.getFechaNacimiento() == null) {
            redirectAttributes.addFlashAttribute("mensaje", "❌ Error: Fecha de nacimiento es obligatoria");
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
            return "redirect:/asistidos/familia/" + nroFamilia + "/nuevo";
        }
        
        if (asistido.getOcupacion() == null) {
            redirectAttributes.addFlashAttribute("mensaje", "❌ Error: Ocupación es obligatoria");
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
            return "redirect:/asistidos/familia/" + nroFamilia + "/nuevo";
        }
        
        try {
            // Validar DNI único
            if (asistidoService.existePorDni(asistido.getDni())) {
                redirectAttributes.addFlashAttribute("mensaje", 
                    "❌ Ya existe una persona con DNI: " + asistido.getDni());
                redirectAttributes.addFlashAttribute("tipoMensaje", "error");
                return "redirect:/asistidos/familia/" + nroFamilia + "/nuevo";
            }
            
            Asistido integranteGuardado = asistidoService.agregarIntegrante(asistido);
            
            redirectAttributes.addFlashAttribute("mensaje", 
                "✅ Integrante " + asistido.getNombreCompleto() + " agregado exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
            
            return "redirect:/familias/" + nroFamilia;
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "❌ Error al agregar integrante: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
            return "redirect:/asistidos/familia/" + nroFamilia + "/nuevo";
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
                "✅ Integrante eliminado exitosamente (eliminación lógica)");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "❌ Error: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }
        
        return "redirect:/familias/" + nroFamilia;
    }
}

package com.utn.tp.controller;

import com.utn.tp.model.Familia;
import com.utn.tp.model.Asistido;
import com.utn.tp.model.Ocupacion;
import com.utn.tp.service.FamiliaService;
import com.utn.tp.service.AsistidoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.time.LocalDate;

@Controller
@RequestMapping("/familias")
public class FamiliaController {
    
    @Autowired
    private FamiliaService familiaService;
    
    @Autowired
    private AsistidoService asistidoService;
    
    /**
     * Criterio: Listado con todas las familias asistidas activas
     * Filtros por nro de familia y nombre
     */
    @GetMapping
    public String listarFamilias(@RequestParam(required = false) String nombre,
                                @RequestParam(required = false) Long nroFamilia,
                                Model model) {
        List<Familia> familias;
        
        if (nroFamilia != null) {
            familias = familiaService.buscarPorNumero(nroFamilia);
            model.addAttribute("filtroNumero", nroFamilia);
        } else if (nombre != null && !nombre.trim().isEmpty()) {
            familias = familiaService.buscarPorNombre(nombre);
            model.addAttribute("filtroNombre", nombre);
        } else {
            familias = familiaService.listarFamiliasActivas();
        }
        
        model.addAttribute("familias", familias);
        return "familias/lista";
    }
    
    /**
     * Criterio: Alta de una Familia
     * Mostrar formulario con lista de integrantes
     */
    @GetMapping("/nueva")
    public String mostrarFormularioAlta(Model model) {
        Familia familia = new Familia();
        familia.setFechaAlta(LocalDate.now()); // Establecer fecha actual por defecto
        model.addAttribute("familia", familia);
        model.addAttribute("integrantes", new ArrayList<Asistido>());
        model.addAttribute("ocupaciones", Ocupacion.values());
        model.addAttribute("esNueva", true);
        return "familias/formulario";
    }
    
    /**
     * Método auxiliar para convertir ocupación
     */
    private Ocupacion convertirOcupacion(String ocupacionStr) {
        try {
            // Primero intentar por nombre del enum (EMPLEADO, DESEMPLEADO, etc.)
            return Ocupacion.valueOf(ocupacionStr);
        } catch (IllegalArgumentException e1) {
            try {
                // Si falla, intentar por descripción (Empleado, Desempleado, etc.)
                return Ocupacion.fromDescripcion(ocupacionStr);
            } catch (IllegalArgumentException e2) {
                throw new IllegalArgumentException("Ocupación inválida: " + ocupacionStr + 
                    ". Valores válidos: " + java.util.Arrays.toString(Ocupacion.values()));
            }
        }
    }
    
    /**
     * Criterio: Alta de una Familia
     * Procesar alta con integrantes y mensajes de confirmación
     */
    @PostMapping("/guardar")
    public String altaFamilia(@Valid @ModelAttribute Familia familia,
                     BindingResult result,
                     @RequestParam(required = false) List<Long> integranteDni,
                     @RequestParam(required = false) List<String> integranteApellido,
                     @RequestParam(required = false) List<String> integranteNombre,
                     @RequestParam(required = false) List<String> integranteFechaNacimiento,
                     @RequestParam(required = false) List<String> integranteOcupacion,
                     RedirectAttributes redirectAttributes,
                     Model model) {

    // Validar fecha de alta no anterior a hoy (solo para nuevas familias)
    if (familia.getNroFamilia() == null && familia.getFechaAlta() != null && familia.getFechaAlta().isBefore(LocalDate.now())) {
        result.rejectValue("fechaAlta", "error.familia", "La fecha de alta no puede ser anterior a la fecha actual");
    }

    if (result.hasErrors()) {
        model.addAttribute("esNueva", familia.getNroFamilia() == null);
        model.addAttribute("ocupaciones", Ocupacion.values());
        
        // Si es edición, recargar integrantes
        if (familia.getNroFamilia() != null) {
            List<Asistido> integrantes = asistidoService.buscarIntegrantesActivos(familia.getNroFamilia());
            model.addAttribute("integrantes", integrantes);
        }
        
        return "familias/formulario";
    }

try {
    List<Asistido> integrantes = new ArrayList<>();
    
    // Validar que haya al menos un integrante
    if (integranteDni == null || integranteDni.isEmpty()) {
        redirectAttributes.addFlashAttribute("mensaje", "❌ Error: Debe agregar al menos un integrante a la familia");
        redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        return "redirect:/familias/nueva";
    }
    
    // Procesar cada integrante
    for (int i = 0; i < integranteDni.size(); i++) {
        if (integranteDni.get(i) != null && integranteDni.get(i) > 0) {
            // Validar que todos los campos estén completos
            if (integranteApellido.get(i) == null || integranteApellido.get(i).trim().isEmpty() ||
                integranteNombre.get(i) == null || integranteNombre.get(i).trim().isEmpty() ||
                integranteFechaNacimiento.get(i) == null || integranteFechaNacimiento.get(i).trim().isEmpty() ||
                integranteOcupacion.get(i) == null || integranteOcupacion.get(i).trim().isEmpty()) {
                
                redirectAttributes.addFlashAttribute("mensaje", "❌ Error: Todos los datos de los integrantes son obligatorios");
                redirectAttributes.addFlashAttribute("tipoMensaje", "error");
                return "redirect:/familias/nueva";
            }
            
            Asistido asistido = new Asistido();
            asistido.setDni(integranteDni.get(i));
            asistido.setApellido(integranteApellido.get(i).trim());
            asistido.setNombre(integranteNombre.get(i).trim());
            asistido.setFechaNacimiento(LocalDate.parse(integranteFechaNacimiento.get(i)));
            
            // Convertir la ocupación con manejo mejorado de errores
            try {
                Ocupacion ocupacion = convertirOcupacion(integranteOcupacion.get(i));
                asistido.setOcupacion(ocupacion);
            } catch (IllegalArgumentException e) {
                redirectAttributes.addFlashAttribute("mensaje", "❌ " + e.getMessage());
                redirectAttributes.addFlashAttribute("tipoMensaje", "error");
                return "redirect:/familias/nueva";
            }
            
            integrantes.add(asistido);
        }
    }
    
    if (integrantes.isEmpty()) {
        redirectAttributes.addFlashAttribute("mensaje", "❌ Error: Debe agregar al menos un integrante válido a la familia");
        redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        return "redirect:/familias/nueva";
    }
    
    if (familia.getNroFamilia() == null) {
        // Alta de nueva familia con integrantes
        Familia familiaGuardada = familiaService.altaFamilia(familia, integrantes);
        redirectAttributes.addFlashAttribute("mensaje", 
            "✅ ¡Éxito! Familia '" + familiaGuardada.getNombre() + "' registrada correctamente con " + integrantes.size() + " integrante(s). Nro. Familia: " + familiaGuardada.getNroFamilia());
        redirectAttributes.addFlashAttribute("tipoMensaje", "success");
    } else {
        // Modificación de familia existente
        Familia familiaModificada = familiaService.modificarFamilia(familia);
        redirectAttributes.addFlashAttribute("mensaje", 
            "✅ ¡Éxito! Familia '" + familiaModificada.getNombre() + "' modificada correctamente");
        redirectAttributes.addFlashAttribute("tipoMensaje", "success");
    }
} catch (IllegalArgumentException e) {
    redirectAttributes.addFlashAttribute("mensaje", "❌ Error de validación: " + e.getMessage());
    redirectAttributes.addFlashAttribute("tipoMensaje", "error");
    return "redirect:/familias/nueva";
} catch (Exception e) {
    redirectAttributes.addFlashAttribute("mensaje", "❌ Error inesperado: " + e.getMessage());
    redirectAttributes.addFlashAttribute("tipoMensaje", "error");
    return "redirect:/familias/nueva";
}

return "redirect:/familias";
}
    
    /**
     * Ver detalle de familia
     */
    @GetMapping("/{nroFamilia}")
    public String verDetalleFamilia(@PathVariable Long nroFamilia, Model model) {
        Optional<Familia> familiaOpt = familiaService.buscarFamiliaActiva(nroFamilia);
        
        if (familiaOpt.isEmpty()) {
            return "redirect:/familias";
        }
        
        Familia familia = familiaOpt.get();
        List<Asistido> integrantes = asistidoService.buscarIntegrantesActivos(nroFamilia);
        
        model.addAttribute("familia", familia);
        model.addAttribute("integrantes", integrantes);
        model.addAttribute("cantidadIntegrantes", integrantes.size());
        
        return "familias/detalle";
    }
    
    /**
     * Criterio: Modificación de una Familia
     * Mostrar formulario para editar (excepto nro familia)
     */
    @GetMapping("/{nroFamilia}/editar")
    public String mostrarFormularioModificacion(@PathVariable Long nroFamilia, Model model) {
        Optional<Familia> familiaOpt = familiaService.buscarFamiliaActiva(nroFamilia);
        
        if (familiaOpt.isEmpty()) {
            return "redirect:/familias";
        }
        
        Familia familia = familiaOpt.get();
        List<Asistido> integrantes = asistidoService.buscarIntegrantesActivos(nroFamilia);
        
        // Asegurar que la fecha de alta se mantenga (no debe ser null)
        if (familia.getFechaAlta() == null) {
            familia.setFechaAlta(LocalDate.now());
        }
        
        model.addAttribute("familia", familia);
        model.addAttribute("integrantes", integrantes); // Cargar integrantes existentes
        model.addAttribute("ocupaciones", Ocupacion.values());
        model.addAttribute("esNueva", false);
        return "familias/formulario";
    }
    
    /**
     * Criterio: Eliminación de una Familia
     * Eliminación lógica
     */
    @PostMapping("/{nroFamilia}/eliminar")
    public String eliminarFamilia(@PathVariable Long nroFamilia, RedirectAttributes redirectAttributes) {
        try {
            familiaService.eliminarFamilia(nroFamilia);
            redirectAttributes.addFlashAttribute("mensaje", "✅ Familia eliminada exitosamente (eliminación lógica)");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "❌ Error: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }
        
        return "redirect:/familias";
    }
}

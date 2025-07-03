package com.utn.tp.service;


import com.utn.tp.model.Asistido;
import com.utn.tp.repository.AsistidoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class AsistidoService {
    
    @Autowired
    private AsistidoRepo asistidoRepository;
    
    /**
     * Buscar integrantes activos de una familia
     */
    public List<Asistido> buscarIntegrantesActivos(Long nroFamilia) {
        return asistidoRepository.findByFamiliaNroFamiliaAndFamiliaActivaTrueAndActivoTrue(nroFamilia);
    }
    
    /**
     * Agregar integrante con validaciones
     */
    public Asistido agregarIntegrante(Asistido asistido) {
        System.out.println("DEBUG Service - Agregando integrante con DNI: " + asistido.getDni());
        System.out.println("DEBUG Service - Familia: " + (asistido.getFamilia() != null ? asistido.getFamilia().getNroFamilia() : "NULL"));
        
        // Validar DNI único (criterio de aceptación)
        if (asistidoRepository.existsById(asistido.getDni())) {
            throw new IllegalArgumentException("Ya existe una persona con DNI: " + asistido.getDni());
        }
        
        // Validar que tenga familia asignada
        if (asistido.getFamilia() == null || asistido.getFamilia().getNroFamilia() == null) {
            throw new IllegalArgumentException("El integrante debe tener una familia asignada");
        }
        
        if (asistido.getFechaRegistro() == null) {
            asistido.setFechaRegistro(LocalDate.now());
        }
        asistido.setActivo(true);
        
        System.out.println("DEBUG Service - Guardando en base de datos...");
        Asistido resultado = asistidoRepository.save(asistido);
        System.out.println("DEBUG Service - Guardado exitoso");
        
        return resultado;
    }

    /**
     * Verificar si existe una persona con el DNI dado
     */
    public boolean existePorDni(Long dni) {
        return asistidoRepository.existsById(dni);
    }
}


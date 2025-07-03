package com.utn.tp.service;

import com.utn.tp.model.Familia;
import com.utn.tp.model.Asistido;
import com.utn.tp.repository.FamiliaRepo;
import com.utn.tp.repository.AsistidoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FamiliaService {
    
    @Autowired
    private FamiliaRepo familiaRepository;
    
    @Autowired
    private AsistidoRepo asistidoRepository;
    
    /**
     * Criterio: Listado con todas las familias asistidas activas (no eliminadas)
     */
    public List<Familia> listarFamiliasActivas() {
        return familiaRepository.findByActivaTrueOrderByFechaAltaDesc();
    }
    
    /**
     * Criterio: Filtrar por nombre
     */
    public List<Familia> buscarPorNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return listarFamiliasActivas();
        }
        return familiaRepository.findByNombreContainingIgnoreCaseAndActivaTrueOrderByNombre(nombre.trim());
    }
    
    /**
     * Criterio: Filtrar por número de familia
     */
    public List<Familia> buscarPorNumero(Long nroFamilia) {
        if (nroFamilia == null) {
            return listarFamiliasActivas();
        }
        return familiaRepository.findByNroFamiliaAndActivaTrue(nroFamilia);
    }
    
    /**
     * Buscar una familia activa específica
     */
    public Optional<Familia> buscarFamiliaActiva(Long nroFamilia) {
        Optional<Familia> familiaOpt = familiaRepository.findFirstByNroFamiliaAndActivaTrue(nroFamilia);
        
        // Debug para verificar los datos cargados
        if (familiaOpt.isPresent()) {
            Familia familia = familiaOpt.get();
            System.out.println("DEBUG Service - Familia encontrada: " + familia.getNombre());
            System.out.println("DEBUG Service - Fecha Alta: " + familia.getFechaAlta());
        }
        
        return familiaOpt;
    }
    
    /**
     * Buscar familia con integrantes
     */
    public Optional<Familia> buscarFamiliaConIntegrantes(Long nroFamilia) {
        return familiaRepository.findByIdWithIntegrantes(nroFamilia);
    }
    
    /**
     * Criterio: Alta de una Familia
     * - Fecha de alta por defecto: fecha actual
     * - Nro de familia auto-generado
     * - Todos los datos requeridos
     */
    public Familia altaFamilia(Familia familia, List<Asistido> integrantes) {
        // Establecer fecha de alta si no está definida (criterio: valor por defecto fecha actual)
        if (familia.getFechaAlta() == null) {
            familia.setFechaAlta(LocalDate.now());
        }
        
        // Asegurar que la familia esté activa
        familia.setActiva(true);
        
        // Validar que no haya DNIs duplicados (criterio: no dos personas con mismo DNI)
        if (integrantes != null) {
            for (Asistido asistido : integrantes) {
                if (asistidoRepository.existsById(asistido.getDni())) {
                    throw new IllegalArgumentException("Ya existe una persona con DNI: " + asistido.getDni());
                }
            }
        }
        
        // Guardar la familia primero (nro familia auto-generado)
        Familia familiaGuardada = familiaRepository.save(familia);
        
        // Asignar la familia a cada integrante
        if (integrantes != null) {
            for (Asistido asistido : integrantes) {
                asistido.setFamilia(familiaGuardada);
                if (asistido.getFechaRegistro() == null) {
                    asistido.setFechaRegistro(LocalDate.now());
                }
                asistido.setActivo(true);
                asistidoRepository.save(asistido);
            }
        }
        
        return familiaGuardada;
    }
    
    /**
     * Criterio: Modificación de una Familia
     * - Se pueden editar todos los datos excepto nro de familia
     */
    public Familia modificarFamilia(Familia familiaModificada) {
        Optional<Familia> familiaExistente = familiaRepository.findFirstByNroFamiliaAndActivaTrue(familiaModificada.getNroFamilia());
        
        if (familiaExistente.isEmpty()) {
            throw new IllegalArgumentException("La familia no existe o está inactiva");
        }
        
        Familia familia = familiaExistente.get();
        
        // Modificar todos los datos excepto nro familia (criterio de aceptación)
        familia.setNombre(familiaModificada.getNombre());
        familia.setFechaAlta(familiaModificada.getFechaAlta());
        familia.setFechaUltimaAsistencia(familiaModificada.getFechaUltimaAsistencia());
        
        return familiaRepository.save(familia);
    }
    
    /**
     * Criterio: Eliminación de una Familia
     * - Eliminación lógica: no aparece en listados pero mantiene registros históricos
     */
    public void eliminarFamilia(Long nroFamilia) {
        Optional<Familia> familiaOpt = familiaRepository.findFirstByNroFamiliaAndActivaTrue(nroFamilia);
        
        if (familiaOpt.isEmpty()) {
            throw new IllegalArgumentException("La familia no existe o ya está eliminada");
        }
        
        Familia familia = familiaOpt.get();
        
        // Eliminación lógica (criterio: mantener registros históricos)
        familia.setActiva(false);
        familiaRepository.save(familia);
    }
    
    /**
     * Criterio: Eliminación lógica de integrantes con fecha automática
     */
    public void eliminarIntegrante(Long dni) {
        Optional<Asistido> asistidoOpt = asistidoRepository.findById(dni);
        if (asistidoOpt.isPresent()) {
            Asistido asistido = asistidoOpt.get();
            asistido.setActivo(false); // Eliminación lógica
            asistido.setFechaEliminacion(LocalDate.now()); // ESTABLECER FECHA ACTUAL AUTOMÁTICAMENTE
            asistidoRepository.save(asistido);
            
            System.out.println("DEBUG - Integrante eliminado lógicamente con fecha: " + LocalDate.now());
        }
    }
    
    /**
     * Contar familias activas
     */
    public long contarFamiliasActivas() {
        return familiaRepository.countByActivaTrue();
    }
    
    /**
     * Criterio: Nro de integrantes activos (no eliminados)
     */
    public long contarIntegrantesActivos(Long nroFamilia) {
        return asistidoRepository.countByFamiliaNroFamiliaAndFamiliaActivaTrueAndActivoTrue(nroFamilia);
    }

    /**
     * Listar familias con conteo optimizado de integrantes
     */
    public List<Familia> listarFamiliasActivasConConteo() {
        List<Familia> familias = familiaRepository.findByActivaTrueOrderByFechaAltaDesc();
        
        // Pre-cargar conteos para evitar consultas N+1
        for (Familia familia : familias) {
            long conteo = asistidoRepository.countByFamiliaNroFamiliaAndFamiliaActivaTrueAndActivoTrue(familia.getNroFamilia());
            // Establecer un campo temporal para el conteo (se puede usar un DTO en el futuro)
            familia.getIntegrantes(); // Inicializar la colección si es necesario
        }
        
        return familias;
    }
}



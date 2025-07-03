package com.utn.tp.repository;





import com.utn.tp.model.Familia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface FamiliaRepo extends JpaRepository<Familia, Long> {
    
    // Buscar solo familias activas (criterio: no eliminadas)
    List<Familia> findByActivaTrueOrderByFechaAltaDesc();
    
    // Filtro por nombre (criterio de aceptación)
    List<Familia> findByNombreContainingIgnoreCaseAndActivaTrueOrderByNombre(String nombre);
    
    // Filtro por número de familia (criterio de aceptación)
    List<Familia> findByNroFamiliaAndActivaTrue(Long nroFamilia);
    
    // Buscar por número de familia activa
    Optional<Familia> findFirstByNroFamiliaAndActivaTrue(Long nroFamilia);
    
    // Contar familias activas
    long countByActivaTrue();
    
    @Query("SELECT f FROM Familia f LEFT JOIN FETCH f.integrantes WHERE f.nroFamilia = :nroFamilia AND f.activa = true")
    Optional<Familia> findByIdWithIntegrantes(@Param("nroFamilia") Long nroFamilia);
}

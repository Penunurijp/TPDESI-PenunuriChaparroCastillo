package com.utn.tp.repository;



import com.utn.tp.model.Asistido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AsistidoRepo extends JpaRepository<Asistido, Long> {
   
   // Buscar integrantes activos de una familia activa
   List<Asistido> findByFamiliaNroFamiliaAndFamiliaActivaTrueAndActivoTrue(Long nroFamilia);
   
   // Contar integrantes activos de una familia
   long countByFamiliaNroFamiliaAndFamiliaActivaTrueAndActivoTrue(Long nroFamilia);
}

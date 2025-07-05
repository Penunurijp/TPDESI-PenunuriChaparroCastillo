package com.utn.tp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.utn.tp.model.Receta;

import java.util.List;

@Repository
public interface IRecetaRepository extends JpaRepository<Receta, Long> {
    List<Receta> findByActivo(boolean activo);

    List<Receta> findByActivoTrue();

    List<Receta> findByNombre(String nombre);

    @Query(value = "select * from receta WHERE activo= 1 and (nombre LIKE CONCAT('%', :nombre ,'%')) and (calorias >= :minCalorias) and (calorias <= :maxCalorias)", nativeQuery=true)
    List<Receta> findByNombreAndCalorias(String nombre, Integer minCalorias, Integer maxCalorias);
}

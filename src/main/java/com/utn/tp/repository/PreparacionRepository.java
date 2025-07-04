package com.utn.tp.repository;

import com.utn.tp.model.Preparacion;
import com.utn.tp.model.Receta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PreparacionRepository extends JpaRepository<Preparacion, Long> {
    boolean existsByFechaAndRecetaAndEliminadoFalse(LocalDate fecha, Receta receta);
    List<Preparacion> findByEliminadoFalse();
}


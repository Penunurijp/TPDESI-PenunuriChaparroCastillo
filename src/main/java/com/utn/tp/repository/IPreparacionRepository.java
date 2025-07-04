package com.utn.tp.repository;

import com.utn.tp.model.Preparacion;
import com.utn.tp.model.Receta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface IPreparacionRepository extends JpaRepository<Preparacion, Long> {
    boolean existsByRecetaAndFechaAndEliminadoFalse(Receta receta, LocalDate fecha);
    boolean existsByFechaAndRecetaAndEliminadoFalse(LocalDate fecha, Receta receta);
    List<Preparacion> findByEliminadoFalse();
}
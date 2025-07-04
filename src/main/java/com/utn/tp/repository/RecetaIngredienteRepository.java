package com.utn.tp.repository;

import com.utn.tp.model.Receta;
import com.utn.tp.model.RecetaIngrediente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecetaIngredienteRepository extends JpaRepository<RecetaIngrediente, Long> {
    List<RecetaIngrediente> findByReceta(Receta receta);
}

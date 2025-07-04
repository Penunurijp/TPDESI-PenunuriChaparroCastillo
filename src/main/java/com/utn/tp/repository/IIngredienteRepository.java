package com.utn.tp.repository;

import com.utn.tp.model.Ingrediente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IIngredienteRepository extends JpaRepository<Ingrediente, Long> {
}
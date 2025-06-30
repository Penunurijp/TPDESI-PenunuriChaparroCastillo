package com.utn.tp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.utn.tp.model.Receta;

@Repository
public interface IRecetaRepository extends JpaRepository<Receta, Long> {
}

package com.utn.tp.service;

import java.util.List;
import java.util.Optional;

import com.utn.tp.model.Receta;

public interface IRecetaService {
	public List<Receta> listarRecetas();

	public Receta save(Receta receta);

	public Receta findById(long id);

	List<Receta> findByActivoTrue();

	List<Receta> buscarFiltradas(String nombre, Integer minCalorias, Integer maxCalorias);

	List<Receta> findByNombre(String nombre);
}


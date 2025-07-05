package com.utn.tp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.utn.tp.model.Receta;
import com.utn.tp.repository.IRecetaRepository;

@Service
public class RecetaService implements IRecetaService {

	@Autowired
	private IRecetaRepository recetaRepository;

	@Override
	public List<Receta> listarRecetas() {
		return recetaRepository.findAll();
	}

	@Override
	public Receta save(Receta receta) {
		return recetaRepository.save(receta);
	}

	@Override
	public Receta findById(long id) {
		return recetaRepository.findById(id).orElse(null);
	}

	@Override
	public List<Receta> findByActivoTrue() {
		return recetaRepository.findByActivoTrue();
	}

	@Override
	public List<Receta> buscarFiltradas(String nombre, Integer minCalorias, Integer maxCalorias) {
			return recetaRepository.findByNombreAndCalorias(nombre, minCalorias, maxCalorias) ;
	}

	@Override
	public List<Receta> findByNombre(String nombre) {
		return recetaRepository.findByNombre(nombre);
	}


}

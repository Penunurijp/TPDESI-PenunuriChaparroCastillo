package com.utn.tp.service;

import java.util.List;

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
	public void delete(long receta) {
		recetaRepository.deleteById(receta);
	}

}

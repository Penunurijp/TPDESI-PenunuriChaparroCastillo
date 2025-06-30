package com.utn.tp.service;

import java.util.List;

import com.utn.tp.model.Receta;

public interface IRecetaService {
	public List<Receta> listarRecetas();

	public Receta save(Receta receta);

	public void delete(long id);
}

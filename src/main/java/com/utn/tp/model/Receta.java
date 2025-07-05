package com.utn.tp.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table (name = "receta")
public class Receta {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String nombre;
	private String descripcion;
	private int calorias;
	@ManyToMany
	private List<Ingrediente> ingredientes;
	private boolean activo;

	public Receta() {
	}

	public Receta(long id, String nombre, int calorias, String descripcion, boolean activo, List<Ingrediente> ingredientes) {
		this.id = id;
		this.nombre = nombre;
		this.calorias = calorias;
		this.descripcion = descripcion;
		this.activo = activo;
		this.ingredientes = ingredientes;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Integer getCalorias() {
		return calorias;
	}

	public void setCalorias(int calorias) {
		this.calorias = calorias;
	}

	public List<Ingrediente> getIngredientes() {
		return ingredientes;
	}

	public void setIngredientes(List<Ingrediente> ingredientes) {
		this.ingredientes = ingredientes;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}
}

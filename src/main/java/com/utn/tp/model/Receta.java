package com.utn.tp.model;

import jakarta.persistence.*;

@Entity
@Table (name = "receta")
public class Receta {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String nombre;
	private String descripcion;
	private int calorias;

	public Receta() {
		super();
	}

	public Receta(long id, String nombre, String descripcion, Integer calorias) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.calorias = calorias;
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

}

package com.utn.tp.model;

public enum Ocupacion {
    DESEMPLEADO("Desempleado"),
    EMPLEADO("Empleado"),
    ESTUDIANTE("Estudiante"),
    AMA_DE_CASA("Ama de Casa"),
    OTRO("Otro");
    
	 private final String descripcion;
	    
	    Ocupacion(String descripcion) {
	        this.descripcion = descripcion;
	    }
	    
	    public String getDescripcion() {
	        return descripcion;
	    }
	    
	    @Override
	    public String toString() {
	        return descripcion;
	    }
	    
	    // Método para obtener el enum por descripción (útil para el formulario)
	    public static Ocupacion fromDescripcion(String descripcion) {
	        for (Ocupacion ocupacion : Ocupacion.values()) {
	            if (ocupacion.getDescripcion().equals(descripcion)) {
	                return ocupacion;
	            }
	        }
	        throw new IllegalArgumentException("No se encontró la ocupación: " + descripcion);
	    }
	}

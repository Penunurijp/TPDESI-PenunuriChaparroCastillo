package com.utn.tp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;

@Entity
@Table(name = "personas")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_persona", discriminatorType = DiscriminatorType.STRING)
public abstract class Persona {
    
    @Id
    @NotNull(message = "El DNI es obligatorio")
    @Positive(message = "El DNI debe ser un número positivo")
    private Long dni;
    
    @NotBlank(message = "El apellido es obligatorio")
    @Column(nullable = false)
    private String apellido;
    
    @NotBlank(message = "El nombre es obligatorio")
    @Column(nullable = false)
    private String nombre;
    
    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @Column(nullable = false)
    private LocalDate fechaNacimiento;
    
    @NotNull(message = "La ocupación es obligatoria")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Ocupacion ocupacion;
    
    // Campo para eliminación lógica de integrantes
    @Column(nullable = false)
    private Boolean activo = true;
    
    // Constructores
    public Persona() {}
    
    public Persona(Long dni, String apellido, String nombre, LocalDate fechaNacimiento, Ocupacion ocupacion) {
        this.dni = dni;
        this.apellido = apellido;
        this.nombre = nombre;
        this.fechaNacimiento = fechaNacimiento;
        this.ocupacion = ocupacion;
        this.activo = true;
    }
    
    // Getters y Setters
    public Long getDni() { return dni; }
    public void setDni(Long dni) { this.dni = dni; }
    
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    
    public Ocupacion getOcupacion() { return ocupacion; }
    public void setOcupacion(Ocupacion ocupacion) { this.ocupacion = ocupacion; }
    
    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
    
    public String getNombreCompleto() {
        return apellido + ", " + nombre;
    }
}



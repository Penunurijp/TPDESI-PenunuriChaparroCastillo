package com.utn.tp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@DiscriminatorValue("ASISTIDO")
public class Asistido extends Persona {
    
    @NotNull(message = "La fecha de registro es obligatoria")
    @Column(nullable = false)
    private LocalDate fechaRegistro;
    
    // Campo para registrar cuándo fue eliminado lógicamente
    @Column
    private LocalDate fechaEliminacion;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nro_familia")
    private Familia familia;
    
    // Constructores
    public Asistido() {
        super();
    }
    
    public Asistido(Long dni, String apellido, String nombre, LocalDate fechaNacimiento, 
                   Ocupacion ocupacion, LocalDate fechaRegistro) {
        super(dni, apellido, nombre, fechaNacimiento, ocupacion);
        this.fechaRegistro = fechaRegistro;
    }
    
    // Getters y Setters
    public LocalDate getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDate fechaRegistro) { this.fechaRegistro = fechaRegistro; }
    
    public LocalDate getFechaEliminacion() { return fechaEliminacion; }
    public void setFechaEliminacion(LocalDate fechaEliminacion) { this.fechaEliminacion = fechaEliminacion; }
    
    public Familia getFamilia() { return familia; }
    public void setFamilia(Familia familia) { this.familia = familia; }
}

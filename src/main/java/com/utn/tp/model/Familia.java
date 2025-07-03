package com.utn.tp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "familias")
public class Familia {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long nroFamilia;
    
    @NotBlank(message = "El nombre de la familia es obligatorio")
    @Column(nullable = false)
    private String nombre;
    
    @NotNull(message = "La fecha de alta es obligatoria")
    @Column(nullable = false)
    private LocalDate fechaAlta;
    
    // Fecha de última asistencia recibida (criterio de aceptación)
    @Column
    private LocalDate fechaUltimaAsistencia;
    
    @OneToMany(mappedBy = "familia", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Asistido> integrantes;
    
    // Campo para eliminación lógica
    @Column(nullable = false)
    private Boolean activa = true;
    
    // Constructores
    public Familia() {}
    
    public Familia(String nombre, LocalDate fechaAlta) {
        this.nombre = nombre;
        this.fechaAlta = fechaAlta;
        this.activa = true;
    }
    
    // Getters y Setters
    public Long getNroFamilia() { return nroFamilia; }
    public void setNroFamilia(Long nroFamilia) { this.nroFamilia = nroFamilia; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public LocalDate getFechaAlta() { return fechaAlta; }
    public void setFechaAlta(LocalDate fechaAlta) { this.fechaAlta = fechaAlta; }
    
    public LocalDate getFechaUltimaAsistencia() { return fechaUltimaAsistencia; }
    public void setFechaUltimaAsistencia(LocalDate fechaUltimaAsistencia) { this.fechaUltimaAsistencia = fechaUltimaAsistencia; }
    
    public List<Asistido> getIntegrantes() { return integrantes; }
    public void setIntegrantes(List<Asistido> integrantes) { this.integrantes = integrantes; }
    
    public Boolean getActiva() { return activa; }
    public void setActiva(Boolean activa) { this.activa = activa; }
    
    // Método para contar integrantes activos (criterio de aceptación)
    public int getCantidadIntegrantesActivos() {
        if (integrantes == null) return 0;
        return (int) integrantes.stream()
            .filter(integrante -> integrante != null && Boolean.TRUE.equals(integrante.getActivo()))
            .count();
    }
}

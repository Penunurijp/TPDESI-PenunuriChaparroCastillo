package com.utn.tp.service;

import com.utn.tp.model.*;
import com.utn.tp.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class PreparacionService {

    @Autowired
    private PreparacionRepository preparacionRepository;

    @Autowired
    private RecetaIngredienteRepository recetaIngredienteRepository;

    @Autowired
    private IngredienteRepository ingredienteRepository;

    public List<Preparacion> listar() {
        return preparacionRepository.findByEliminadoFalse();
    }

    @Transactional
    public void guardar(Preparacion preparacion) throws Exception {
        validar(preparacion);

        // Verificar si ya existe una preparación para esa receta y fecha
        if (preparacionRepository.existsByFechaAndRecetaAndEliminadoFalse(preparacion.getFecha(), preparacion.getReceta())) {
            throw new Exception("Ya existe una preparación para esta receta en la fecha indicada.");
        }

        // Verificar stock y descontar
        List<RecetaIngrediente> ingredientes = recetaIngredienteRepository.findByReceta(preparacion.getReceta());

        for (RecetaIngrediente ri : ingredientes) {
            Ingrediente ing = ri.getIngrediente();
            double totalNecesario = ri.getCantidad() * preparacion.getCantidadRaciones();
            if (ing.getStock() < totalNecesario) {
                throw new Exception("Stock insuficiente para el ingrediente: " + ing.getNombre());
            }
        }

        for (RecetaIngrediente ri : ingredientes) {
            Ingrediente ing = ri.getIngrediente();
            double totalNecesario = ri.getCantidad() * preparacion.getCantidadRaciones();
            ing.setStock(ing.getStock() - totalNecesario);
            ingredienteRepository.save(ing);
        }

        preparacion.setEliminado(false);
        preparacionRepository.save(preparacion);
    }

    public Preparacion buscarPorId(Long id) {
        return preparacionRepository.findById(id).orElse(null);
    }

    public void eliminar(Long id) {
        Preparacion p = buscarPorId(id);
        if (p != null) {
            p.setEliminado(true);
            preparacionRepository.save(p);
        }
    }

    public void actualizarFecha(Preparacion nueva) throws Exception {
        if (nueva.getFecha().isAfter(LocalDate.now())) {
            throw new Exception("La fecha no puede ser futura.");
        }
        Preparacion actual = buscarPorId(nueva.getId());
        if (actual != null && !actual.isEliminado()) {
            actual.setFecha(nueva.getFecha());
            preparacionRepository.save(actual);
        }
    }

    private void validar(Preparacion p) throws Exception {
        if (p.getFecha() == null || p.getFecha().isAfter(LocalDate.now())) {
            throw new Exception("La fecha es inválida o futura.");
        }
        if (p.getCantidadRaciones() < 1) {
            throw new Exception("Debe indicar al menos 1 ración.");
        }
    }
}

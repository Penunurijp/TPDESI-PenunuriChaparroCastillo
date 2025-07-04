package com.utn.tp.service;

import com.utn.tp.model.Ingrediente;
import com.utn.tp.model.Receta;
import com.utn.tp.model.RecetaIngrediente;
import com.utn.tp.repository.IngredienteRepository;
import com.utn.tp.repository.RecetaIngredienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockService {

    @Autowired
    public IngredienteRepository ingredienteRepo;

    @Autowired
    public RecetaIngredienteRepository recetaIngredienteRepo;

    /**
     * Verifica si hay stock suficiente para preparar una receta en la cantidad indicada.
     */
    public boolean hayStockSuficiente(Receta receta, int raciones) {
        List<RecetaIngrediente> ingredientes = recetaIngredienteRepo.findByReceta(receta);
        for (RecetaIngrediente ri : ingredientes) {
            Ingrediente ing = ri.getIngrediente();
            double requerido = ri.getCantidad() * raciones;
            if (ing.getStock() < requerido) {
                return false;
            }
        }
        return true;
    }

    /**
     * Descuenta el stock de ingredientes según la cantidad de raciones.
     */
    public void descontarIngredientes(Receta receta, int raciones) {
        List<RecetaIngrediente> ingredientes = recetaIngredienteRepo.findByReceta(receta);
        for (RecetaIngrediente ri : ingredientes) {
            Ingrediente ing = ri.getIngrediente();
            double requerido = ri.getCantidad() * raciones;
            ing.setStock(ing.getStock() - requerido);
            ingredienteRepo.save(ing);
        }
    }

    /**
     * Devuelve al stock los ingredientes usados previamente.
     */
    public void devolverIngredientes(Receta receta, int raciones) {
        List<RecetaIngrediente> ingredientes = recetaIngredienteRepo.findByReceta(receta);
        for (RecetaIngrediente ri : ingredientes) {
            Ingrediente ing = ri.getIngrediente();
            double devolver = ri.getCantidad() * raciones;
            ing.setStock(ing.getStock() + devolver);
            ingredienteRepo.save(ing);
        }
    }
}



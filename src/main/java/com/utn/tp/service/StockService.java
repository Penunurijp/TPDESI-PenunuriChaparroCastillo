package com.utn.tp.service;

import com.utn.tp.model.Ingrediente;
import com.utn.tp.model.Receta;
import com.utn.tp.model.RecetaIngrediente;
import com.utn.tp.repository.IIngredienteRepository;
import com.utn.tp.repository.IRecetaIngredienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockService {

    @Autowired
    private IIngredienteRepository ingredienteRepo;

    @Autowired
    private IRecetaIngredienteRepository recetaIngredienteRepo;

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

    public void descontarIngredientes(Receta receta, int raciones) {
        List<RecetaIngrediente> ingredientes = recetaIngredienteRepo.findByReceta(receta);
        for (RecetaIngrediente ri : ingredientes) {
            Ingrediente ing = ri.getIngrediente();
            double requerido = ri.getCantidad() * raciones;
            ing.setStock(ing.getStock() - requerido);
            ingredienteRepo.save(ing);
        }
    }
}

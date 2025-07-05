package com.utn.tp.service;

import com.utn.tp.model.Ingrediente;
import com.utn.tp.repository.IIngredienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IngredienteService {

    @Autowired
    private IIngredienteRepository ingredienteRepository;

    public List<Ingrediente > listarIngredientes(){
        return ingredienteRepository.findAll();
    }
}

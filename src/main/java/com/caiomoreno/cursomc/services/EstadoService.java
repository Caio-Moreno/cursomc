package com.caiomoreno.cursomc.services;

import com.caiomoreno.cursomc.domain.Estado;
import com.caiomoreno.cursomc.repositories.EstadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EstadoService {

    @Autowired
    private EstadoRepository repository;

    public List<Estado> findAll(){
        return repository.findAllByOrderByNome();
    }
}

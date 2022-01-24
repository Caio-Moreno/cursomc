package com.caiomoreno.cursomc.services;

import com.caiomoreno.cursomc.domain.Cidade;
import com.caiomoreno.cursomc.repositories.CidadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CidadeService {

    @Autowired
    private CidadeRepository repository;

    public List<Cidade> findAllCidades(Integer estado){
        return repository.findCidades(estado);
    }

}

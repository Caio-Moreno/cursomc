package com.caiomoreno.cursomc.repositories;

import com.caiomoreno.cursomc.domain.Categoria;
import com.caiomoreno.cursomc.domain.Cidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CidadeRepository extends JpaRepository<Cidade, Integer> {
}

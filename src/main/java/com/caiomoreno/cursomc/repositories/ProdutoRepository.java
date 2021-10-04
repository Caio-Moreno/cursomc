package com.caiomoreno.cursomc.repositories;

import com.caiomoreno.cursomc.domain.Categoria;
import com.caiomoreno.cursomc.domain.Produto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Integer> {

    @Transactional(readOnly = true)
    @Query("select distinct obj from Produto obj inner join obj.categorias cat where obj.nome like %:nome% and cat in  :categorias")
    Page<Produto> findDistinctByNomeContainingAndCategoriasIn(@Param("nome") String nome,@Param("categorias") List<Categoria> categorias, Pageable pageRequest);

}
package com.caiomoreno.cursomc.resources;

import com.caiomoreno.cursomc.domain.Categoria;
import com.caiomoreno.cursomc.domain.Produto;
import com.caiomoreno.cursomc.dto.CategoriaDTO;
import com.caiomoreno.cursomc.dto.ProdutoDTO;
import com.caiomoreno.cursomc.resources.utils.URL;
import com.caiomoreno.cursomc.services.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;


@RestController
@RequestMapping(value = "/produtos")
public class ProdutoResource {

    @Autowired
    private ProdutoService pedidoService;

    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public ResponseEntity<Produto> listar(@PathVariable Integer id){

        Produto obj = pedidoService.find(id);

        return ResponseEntity.ok().body(obj);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Page<ProdutoDTO>> findPage(
            @RequestParam(value = "nome", defaultValue = "") String nome,
            @RequestParam(value = "categorias", defaultValue = "") String categorias,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage,
            @RequestParam(value = "orderBy", defaultValue = "nome") String orderBy,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction){

        String nomeDecoded = URL.decodeParam(nome);
        List<Integer> ids = URL.decodeIntList(categorias);

        Page<Produto> list = pedidoService.search(nome,ids,page,linesPerPage,orderBy,direction);
        Page<ProdutoDTO> categoriaDTOS = list.map(ProdutoDTO::new);

        return ResponseEntity.ok().body(categoriaDTOS);
    }
}

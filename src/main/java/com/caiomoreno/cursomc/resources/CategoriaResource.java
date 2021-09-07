package com.caiomoreno.cursomc.resources;

import com.caiomoreno.cursomc.domain.Categoria;
import com.caiomoreno.cursomc.services.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/categorias")
public class CategoriaResource {

    @Autowired
    private CategoriaService categoriaService;

    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public ResponseEntity<?> listar(@PathVariable Integer id){

        Categoria obj = categoriaService.buscar(id);

        return ResponseEntity.ok().body(obj);
    }
}

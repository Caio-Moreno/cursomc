package com.caiomoreno.cursomc.resources;

import com.caiomoreno.cursomc.domain.Cidade;
import com.caiomoreno.cursomc.domain.Estado;
import com.caiomoreno.cursomc.dto.CidadeDTO;
import com.caiomoreno.cursomc.dto.EstadoDTO;
import com.caiomoreno.cursomc.services.CidadeService;
import com.caiomoreno.cursomc.services.EstadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/estados")
public class EstadoResource {

    @Autowired
    private EstadoService estadoService;

    @Autowired
    private CidadeService cidadeService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<EstadoDTO>> findAll() {
        List<Estado> list = estadoService.findAll();
        List<EstadoDTO> listDto = list.stream().map(EstadoDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

    @RequestMapping(value = "/{estadoId}/cidades",method = RequestMethod.GET)
    public ResponseEntity<List<CidadeDTO>> findAllCidade(@PathVariable Integer estadoId) {
        List<Cidade> list = cidadeService.findAllCidades(estadoId);
        List<CidadeDTO> listDto = list.stream().map(CidadeDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(listDto);
    }

}

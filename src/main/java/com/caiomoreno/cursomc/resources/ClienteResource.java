package com.caiomoreno.cursomc.resources;


import com.caiomoreno.cursomc.domain.Cliente;
import com.caiomoreno.cursomc.dto.ClienteDTO;
import com.caiomoreno.cursomc.dto.ClienteNewDTO;
import com.caiomoreno.cursomc.services.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping(value = "/clientes")
public class ClienteResource {

    @Autowired
    private ClienteService service;

    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public ResponseEntity<Cliente> findById(@PathVariable Integer id){
        Cliente obj = service.find(id);
        return ResponseEntity.ok().body(obj);
    }

    @RequestMapping(value = "/email",method = RequestMethod.GET)
    public ResponseEntity<Cliente> findById(@RequestParam(value = "value") String email){
        Cliente obj = service.findbyEmail(email);
        return ResponseEntity.ok().body(obj);
    }


    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> insert(@Valid @RequestBody ClienteNewDTO objDTO){
        Cliente obj = service.fromDTO(objDTO);
        obj = service.insert(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(obj.getId()).toUri();

        return ResponseEntity.created(uri).build();
    }

    @RequestMapping(value = "/{id}",method = RequestMethod.PUT)
    public ResponseEntity<Void> update(@Valid @RequestBody ClienteDTO objDto, @PathVariable Integer id){
        Cliente obj = service.fromDTO(objDto);
        obj.setId(id);
        obj = service.update(obj);

        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/{id}",method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@PathVariable Integer id){
        service.delete(id);

        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<ClienteDTO>> findAll(){

        List<Cliente> clientes = service.findAll();
        //Era assim  //categorias.stream().map(obj -> new ClienteDTO(obj)).collect(Collectors.toList());
        List<ClienteDTO> clienteDTOS = clientes.stream().map(ClienteDTO::new).collect(Collectors.toList());



        return ResponseEntity.ok().body(clienteDTOS);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/page",method = RequestMethod.GET)
    public ResponseEntity<Page<ClienteDTO>> findPage(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage,
            @RequestParam(value = "orderBy", defaultValue = "nome") String orderBy,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction){

        Page<Cliente> clientes = service.findPage(page,linesPerPage,orderBy,direction);
        Page<ClienteDTO> clienteDTOS = clientes.map(ClienteDTO::new);

        return ResponseEntity.ok().body(clienteDTOS);
    }

    @RequestMapping(value = "/picture",method = RequestMethod.POST)
    public ResponseEntity<Void> uploadProfilePicture(@RequestParam(name = "file") MultipartFile multipartFile){
        URI uri = service.uploadProfilePicture(multipartFile);
        return ResponseEntity.created(uri).build();
    }


}

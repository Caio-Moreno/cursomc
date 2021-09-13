package com.caiomoreno.cursomc.services;

import com.caiomoreno.cursomc.domain.Cidade;
import com.caiomoreno.cursomc.domain.Cliente;
import com.caiomoreno.cursomc.domain.Endereco;
import com.caiomoreno.cursomc.domain.enums.TipoCliente;
import com.caiomoreno.cursomc.dto.ClienteDTO;
import com.caiomoreno.cursomc.dto.ClienteNewDTO;
import com.caiomoreno.cursomc.repositories.ClienteRepository;
import com.caiomoreno.cursomc.repositories.EnderecoRepository;
import com.caiomoreno.cursomc.services.exceptions.DataIntegrityException;
import com.caiomoreno.cursomc.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository repository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    public Cliente find(Integer id) {
        Optional<Cliente> obj = repository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException(
                "Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName()));
    }

    @Transactional
    public Cliente insert(Cliente obj) {
        obj.setId(null);
        obj = repository.save(obj);

        enderecoRepository.saveAll(obj.getEnderecos());

        return obj;
    }

    public Cliente update(Cliente obj) {
        Cliente newObj = find(obj.getId());

        updateData(newObj, obj);

        return repository.save(newObj);
    }

    public void delete(Integer id) {
        find(id);
        try {
            repository.deleteById(id);
        }catch (DataIntegrityViolationException e){
            throw new DataIntegrityException("Não é possível excluir pois há entidades relacionadas");
        }
    }

    public List<Cliente> findAll() {
        return repository.findAll();
    }

    public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction){
        PageRequest pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction),orderBy);

        return repository.findAll(pageRequest);
    }

    public Cliente fromDTO(ClienteDTO objDto){
        return new Cliente(objDto.getId(),objDto.getNome(),objDto.getEmail(),null,null);
    }

    public Cliente fromDTO(ClienteNewDTO objDto){
        Cliente cli = new Cliente(null, objDto.getNome(),objDto.getEmail(),objDto.getCpfOuCnpj(), TipoCliente.toEnum(objDto.getTipo()));
        Cidade cid = new Cidade(objDto.getCidadeId(),null,null);
        Endereco end = new Endereco(null, objDto.getLogradouro(), objDto.getNumero(), objDto.getComplemento(),objDto.getBairro(),objDto.getCep(),cli,cid);

        cli.getEnderecos().add(end);
        cli.getTelefones().add(objDto.getTelefone1());

        if(objDto.getTelefone2() != null) cli.getTelefones().add(objDto.getTelefone2());
        if(objDto.getTelefone3() != null) cli.getTelefones().add(objDto.getTelefone3());


        return cli;
    }


    private void updateData(Cliente newObj, Cliente obj){
        newObj.setNome(obj.getNome());
        newObj.setEmail(obj.getEmail());
    }
}

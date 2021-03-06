package com.lcorreia.cursomc.services;

import com.lcorreia.cursomc.domain.Categoria;
import com.lcorreia.cursomc.domain.Cidade;
import com.lcorreia.cursomc.domain.Cliente;
import com.lcorreia.cursomc.domain.Endereco;
import com.lcorreia.cursomc.domain.enums.Perfil;
import com.lcorreia.cursomc.domain.enums.TipoCliente;
import com.lcorreia.cursomc.dto.CategoriaDTO;
import com.lcorreia.cursomc.dto.ClienteDTO;
import com.lcorreia.cursomc.dto.ClienteNewDTO;
import com.lcorreia.cursomc.repositories.ClienteRepository;
import com.lcorreia.cursomc.repositories.EnderecoRepository;
import com.lcorreia.cursomc.security.UserSS;
import com.lcorreia.cursomc.services.exceptions.AuthorizationException;
import com.lcorreia.cursomc.services.exceptions.DataIntegrityException;
import com.sun.security.auth.UnixNumericUserPrincipal;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository repo;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private BCryptPasswordEncoder pe;

    public Cliente find(Integer id) {

        UserSS user = UserService.autenticated();
        if (user == null || !user.hasRole(Perfil.ADMIN) && !id.equals(user.getId())){
            throw new AuthorizationException("ACESSO NEGADO!");
        }

        Optional<Cliente> obj = repo.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException(" Objeto não encontrado! ID: " + id, Cliente.class.getName()));

    }

    @Transactional
    public Cliente insert(Cliente obj) {
        obj.setId(null);
        obj = repo.save(obj);
        enderecoRepository.saveAll(obj.getEnderecos());
        return obj;
    }

    public Cliente update(Cliente obj) {
        Cliente newObj = find(obj.getId());
        updateData(newObj, obj);
        return repo.save(newObj);
    }

    public void delete(Integer id) {
        find(id);
        try {
            repo.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Não é possivel excluir porque há pedidos relacionadas");
        }
    }

    public List<Cliente> findAll() {
        return repo.findAll();
    }

    public Page<Cliente> findPage(Integer page, Integer linePerPage, String ordeBy, String direction) {
        PageRequest pageRequest = PageRequest.of(page, linePerPage, Sort.Direction.valueOf(direction), ordeBy);
        return repo.findAll(pageRequest);
    }

    public Cliente fromDTO(ClienteDTO objDto){
        return new Cliente(objDto.getId(), objDto.getNome(), objDto.getEmail(), null, null, null);
    }

    public Cliente fromDTO(ClienteNewDTO objDto){
        Cliente cli = new Cliente(null, objDto.getNome(), objDto.getEmail(),
                objDto.getCpfOuCnpj(), TipoCliente.toEnum(objDto.getTipo()),
                pe.encode(objDto.getSenha()));

        Cidade cid = new Cidade(objDto.getCidadeId(), null, null);
        Endereco end = new Endereco(null, objDto.getLogradouro(), objDto.getNumero(), objDto.getComplemento(), objDto.getBairro(), objDto.getCep(), cli, cid);
        cli.getEnderecos().add(end);
        cli.getTelefones().add(objDto.getTelefone1());
        if (objDto.getTelefone2() != null){
            cli.getTelefones().add(objDto.getTelefone2());
        }
        if (objDto.getTelefone3() != null){
            cli.getTelefones().add(objDto.getTelefone3());
        }

        return cli;
    }

    private void updateData(Cliente newObj, Cliente obj){
        newObj.setNome(obj.getNome());
        newObj.setEmail(obj.getEmail());
    }
}

package com.lcorreia.cursomc.services;

import com.lcorreia.cursomc.domain.Cliente;
import com.lcorreia.cursomc.repositories.ClienteRepository;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository repo;

    public Cliente find(Integer id) {

        Optional<Cliente> obj = repo.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException(" Objeto não encontrado! ID: " + id, Cliente.class.getName()));

    }

}

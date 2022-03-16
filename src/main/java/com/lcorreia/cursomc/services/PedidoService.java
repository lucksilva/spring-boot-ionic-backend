package com.lcorreia.cursomc.services;

import com.lcorreia.cursomc.domain.Pedido;
import com.lcorreia.cursomc.repositories.PedidoRepository;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository repo;

    public Pedido buscar(Integer id) {

        Optional<Pedido> obj = repo.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException(" Objeto não encontrado! ID: " + id, Pedido.class.getName()));

    }

}

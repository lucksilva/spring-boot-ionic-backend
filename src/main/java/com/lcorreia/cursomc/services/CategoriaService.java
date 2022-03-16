package com.lcorreia.cursomc.services;

import com.lcorreia.cursomc.domain.Categoria;
import com.lcorreia.cursomc.repositories.CategoriaRepository;
import com.lcorreia.cursomc.services.exceptions.DataIntegrityException;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository repo;

    public Categoria find(Integer id) {

        Optional<Categoria> obj = repo.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException(" Objeto não encontrado! ID: " + id, Categoria.class.getName()));

    }

    public Categoria insert(Categoria obj) {
        obj.setId(null);
        return repo.save(obj);
    }

    public Categoria update(Categoria obj) {
        find(obj.getId());
        return repo.save(obj);
    }

    public void delete(Integer id) {
        find(id);
        try {
            repo.deleteById(id);
        } catch (DataIntegrityViolationException e) {
                throw new DataIntegrityException("Não é possivel excluir categorias com produtos cadastrado");
        }
    }

    public List<Categoria> findAll() {
        return repo.findAll();
    }

    public Page<Categoria> findPage(Integer page, Integer linePerPage, String ordeBy, String direction) {
        PageRequest pageRequest = PageRequest.of(page, linePerPage, Sort.Direction.valueOf(direction), ordeBy);
        return repo.findAll(pageRequest);
    }
//
}

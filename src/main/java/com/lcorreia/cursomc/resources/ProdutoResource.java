package com.lcorreia.cursomc.resources;

import com.lcorreia.cursomc.domain.Produto;
import com.lcorreia.cursomc.dto.ProdutoDTO;
import com.lcorreia.cursomc.resources.utils.URL;
import com.lcorreia.cursomc.services.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value ="/produtos")
public class ProdutoResource {

    @Autowired
    private ProdutoService service;

    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public ResponseEntity<Produto> find (@PathVariable Integer id) {
        Produto obj = service.find(id);
        return ResponseEntity.ok().body(obj);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Page<ProdutoDTO>> findPage(
            @RequestParam(value = "nome", defaultValue = "") String nome,
            @RequestParam(value = "categorias", defaultValue = "") String categorias,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "linePerPage", defaultValue = "24") Integer linePerPage,
            @RequestParam(value = "orderBy", defaultValue = "nome") String ordeBy,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction) {
        String nomeDecoded = URL.decodeParam(nome);
        List<Integer> ids = URL.decodeIntList(categorias);
        Page<Produto> list = service.search(nomeDecoded, ids,page, linePerPage, ordeBy, direction);
        Page<ProdutoDTO> listDto = list.map(obj -> new ProdutoDTO(obj));
        return ResponseEntity.ok().body(listDto);
    }

}

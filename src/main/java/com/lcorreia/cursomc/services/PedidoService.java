package com.lcorreia.cursomc.services;

import com.lcorreia.cursomc.domain.*;
import com.lcorreia.cursomc.domain.enums.EstadoPagamento;
import com.lcorreia.cursomc.repositories.ItemPedidoRepository;
import com.lcorreia.cursomc.repositories.PagamentoRepository;
import com.lcorreia.cursomc.repositories.PedidoRepository;
import com.lcorreia.cursomc.repositories.ProdutoRepository;
import com.lcorreia.cursomc.security.UserSS;
import com.lcorreia.cursomc.services.exceptions.AuthorizationException;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository repo;

    @Autowired
    private BoletoService boletoService;

    @Autowired
    private PagamentoRepository pagamentoRepository;

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private ItemPedidoRepository itemPedidoRepository;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private EmailService emailService;

    public Pedido find(Integer id) {
        Optional<Pedido> obj = repo.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException(" Objeto não encontrado! ID: " + id, Pedido.class.getName()));

    }

    @Transactional
    public Pedido insert(Pedido obj) {
        obj.setId(null);
        obj.setInstante(new Date());
        obj.setCliente(clienteService.find(obj.getCliente().getId()));
        obj.getPagamento().setEstado(EstadoPagamento.PENDENTE);
        obj.getPagamento().setPedido(obj);
        if (obj.getPagamento() instanceof PagamentoComBoleto) {
            PagamentoComBoleto pagto = (PagamentoComBoleto) obj.getPagamento();
            boletoService.preencherPagamentoComBoleto(pagto, obj.getInstante());
        }

        obj = repo.save(obj);
        pagamentoRepository.save(obj.getPagamento());
        for (ItemPedido ip : obj.getItens()){
            ip.setDesconto(0.0);
            ip.setProduto(produtoService.find(ip.getProduto().getId()));
            ip.setPreco(ip.getProduto().getPreco());
            ip.setPedido(obj);
        }
        itemPedidoRepository.saveAll(obj.getItens());
        emailService.sendOrderConfirmationHtmlEmail(obj);
        return obj;
    }

    public Page<Pedido> findPage(Integer page, Integer linePerPage, String ordeBy, String direction) {
        UserSS user = UserService.autenticated();

        if (user == null) {
            throw new AuthorizationException("ACESSO NEGADO!");
        }

        PageRequest pageRequest = PageRequest.of(page, linePerPage, Sort.Direction.valueOf(direction), ordeBy);
        Cliente cliente = clienteService.find(user.getId());

        return repo.findByCliente(cliente, pageRequest);
    }
}
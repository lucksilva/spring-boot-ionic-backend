package com.lcorreia.cursomc.domain.enums;

import com.lcorreia.cursomc.domain.Pagamento;
import com.lcorreia.cursomc.domain.Pedido;

import javax.persistence.Entity;

@Entity
public class PagamentoComCartão extends Pagamento {
    private static final long serialVersionUID = 1L;

    private Integer numeroDeParcelas;

    public PagamentoComCartão(){}

    public PagamentoComCartão(Integer id, EstadoPagamento estado, Pedido pedido, Integer numeroDeParcelas) {
        super(id, estado, pedido);
        this.numeroDeParcelas = numeroDeParcelas;
    }

    public Integer getNumeroDeParcelas() {
        return numeroDeParcelas;
    }

    public void setNumeroDeParcelas(Integer numeroDeParcelas) {
        this.numeroDeParcelas = numeroDeParcelas;
    }
}

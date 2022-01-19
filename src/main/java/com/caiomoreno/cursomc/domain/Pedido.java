package com.caiomoreno.cursomc.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

@Entity
public class Pedido implements Serializable {
    private static final long serialVersionUID = 1L;


    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private Date instante;


    @OneToOne(cascade = CascadeType.ALL, mappedBy = "pedido")
    private Pagamento pagamento;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "endereco_de_entrega_id")
    private Endereco enderecoDeEntrega;

    @OneToMany(mappedBy = "id.pedido")
    private Set<ItemPedido> itens = new HashSet<>();

    public Pedido() {
    }

    public Pedido(Integer id, Date instante, Cliente cliente, Endereco enderecoDeEntrega) {
        this.id = id;
        this.instante = instante;
        this.cliente = cliente;
        this.enderecoDeEntrega = enderecoDeEntrega;
    }

    public double getValorTotal(){
       return itens.stream().mapToDouble(ItemPedido::getSubTotal).sum();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getInstante() {
        return instante;
    }

    public void setInstante(Date instante) {
        this.instante = instante;
    }

    public Pagamento getPagamento() {
        return pagamento;
    }

    public void setPagamento(Pagamento pagamento) {
        this.pagamento = pagamento;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Endereco getEnderecoDeEntrega() {
        return enderecoDeEntrega;
    }

    public void setEnderecoDeEntrega(Endereco enderecoDeEntrega) {
        this.enderecoDeEntrega = enderecoDeEntrega;
    }

    public Set<ItemPedido> getItens() {
        return itens;
    }

    public void setItens(Set<ItemPedido> itens) {
        this.itens = itens;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pedido)) return false;

        Pedido pedido = (Pedido) o;

        return getId() != null ? getId().equals(pedido.getId()) : pedido.getId() == null;
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }

    @Override
    public String toString() {
        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt","BR"));
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        StringBuilder builder = new StringBuilder();
                builder.append("Pedido numero: ");
                builder.append(getId());
                builder.append(", Instante: ");
                builder.append(sdf.format(getInstante()));
                builder.append(", Cliente: ");
                builder.append(getCliente().getNome());
                builder.append(", Situacao do pagamento");
                builder.append(getPagamento().getEstadoPagamento().getDescricao());
                builder.append("\nDetalhes:\n");

                for (ItemPedido ip : getItens()){
                    builder.append(ip.toString());
                }
                builder.append("Valor total: ");
                builder.append(nf.format(getValorTotal()));
                return builder.toString();
    }
}

package subequipe03.ecommerce;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Pedido implements PedidoObservavel {
    private int idPedido;
    private Date dataPedido;
    private BigDecimal valorTotal;
    private String status;

    private Cliente cliente;
    private Carrinho carrinho;
    private Frete frete;
    private FormaPagamento formaPagamento;

    private EstadoPedido estado;

    private final List<PedidoObserver> observadores = new ArrayList<>();

    public Pedido(int idPedido, Cliente cliente, Carrinho carrinho, Frete frete) {
        this.idPedido = idPedido;
        this.cliente = cliente;
        this.carrinho = carrinho;
        this.frete = frete;
        this.dataPedido = new Date();
        this.estado = new EstadoCriado();
        this.status = "Ativo";
    }

    public void setFormaPagamento(FormaPagamento formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public BigDecimal calcularTotal() {
        BigDecimal subtotal = carrinho.calcularSubtotal();
        BigDecimal valorFrete = frete.getValorFrete();
        if (valorFrete == null) {
            valorFrete = BigDecimal.ZERO;
        }
        this.valorTotal = subtotal.add(valorFrete);
        return this.valorTotal;
    }

    public boolean processarPagamento() {
        calcularTotal();
        if (formaPagamento == null) {
            throw new IllegalStateException("Nenhuma forma de pagamento definida para o pedido.");
        }
        return formaPagamento.processarPagamento(this.valorTotal);
    }

    public void finalizar() {
        estado.pagar(this);
    }

    public void enviar() {
        estado.enviar(this);
    }

    public void entregar() {
        estado.entregar(this);
    }

    public void cancelar() {
        estado.cancelar(this);
    }

    public void mudarEstado(EstadoPedido novoEstado) {
        String estadoAnterior = this.estado.getNome();
        this.estado = novoEstado;
        notificarObservadores(estadoAnterior);
    }

    @Override
    public void adicionarObservador(PedidoObserver observador) {
        if (observador == null) {
            throw new IllegalArgumentException("Observador não pode ser nulo.");
        }
        if (!observadores.contains(observador)) {
            observadores.add(observador);
        }
    }

    @Override
    public void removerObservador(PedidoObserver observador) {
        observadores.remove(observador);
    }

    @Override
    public void notificarObservadores(String estadoAnterior) {
        for (PedidoObserver observador : new ArrayList<>(observadores)) {
            try {
                observador.atualizar(this, estadoAnterior);
            } catch (RuntimeException e) {
                System.out.println("   [Aviso] " + observador.getClass().getSimpleName()
                        + " falhou: " + e.getMessage());
            }
        }
    }

    public int getIdPedido() { return idPedido; }
    public Date getDataPedido() { return dataPedido; }
    public String getStatusPedido() { return estado.getNome(); }
    public BigDecimal getValorTotal() { return valorTotal; }
    public BigDecimal getValorFrete() { return frete.getValorFrete(); }
    public Cliente getCliente() { return cliente; }
    public Carrinho getCarrinho() { return carrinho; }
    public FormaPagamento getFormaPagamento() { return formaPagamento; }
}
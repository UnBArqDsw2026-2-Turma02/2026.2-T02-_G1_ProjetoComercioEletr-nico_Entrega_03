package subequipe03.ecommerce;

import java.math.BigDecimal;
import java.util.Date;

public class Pedido {
    private int idPedido;
    private Date dataPedido;
    private String statusPedido;
    private BigDecimal valorTotal;
    private String status;

    private Cliente cliente;
    private Carrinho carrinho;
    private Frete frete;
    private FormaPagamento formaPagamento;

    public Pedido(int idPedido, Cliente cliente, Carrinho carrinho, Frete frete) {
        this.idPedido = idPedido;
        this.cliente = cliente;
        this.carrinho = carrinho;
        this.frete = frete;
        this.dataPedido = new Date();
        this.statusPedido = "CRIADO";
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

    public void finalizar() {
        calcularTotal();
        if (formaPagamento != null) {
            boolean pago = formaPagamento.processarPagamento(this.valorTotal);
            if (pago) {
                this.statusPedido = "PAGO";
                System.out.println("Pedido #" + idPedido + " finalizado com sucesso! Total: R$ " + this.valorTotal);
            } else {
                this.statusPedido = "FALHA_PAGAMENTO";
                System.out.println("Falha ao processar o pagamento do pedido #" + idPedido);
            }
        } else {
            throw new IllegalStateException("Nenhuma forma de pagamento definida para o pedido.");
        }
    }

    // Getters
    public int getIdPedido() { return idPedido; }
    public Date getDataPedido() { return dataPedido; }
    public String getStatusPedido() { return statusPedido; }
    public BigDecimal getValorTotal() { return valorTotal; }
    public BigDecimal getValorFrete() { return frete.getValorFrete(); }
}
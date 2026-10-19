package subequipe03.ecommerce;

public class EstadoFalhaPagamento implements EstadoPedido {

    @Override
    public String getNome() {
        return "FALHA_PAGAMENTO";
    }

    @Override
    public void pagar(Pedido pedido) {
        // Nova tentativa: o comportamento é o mesmo de um pedido recém-criado.
        new EstadoCriado().pagar(pedido);
    }

    @Override
    public void cancelar(Pedido pedido) {
        pedido.mudarEstado(new EstadoCancelado());
    }
}
package subequipe03.ecommerce;

public class EstadoCriado implements EstadoPedido {

    @Override
    public String getNome() {
        return "CRIADO";
    }

    @Override
    public void pagar(Pedido pedido) {
        boolean pago = pedido.processarPagamento();
        if (pago) {
            System.out.println("Pedido #" + pedido.getIdPedido()
                    + " finalizado com sucesso! Total: R$ " + pedido.getValorTotal());
            pedido.mudarEstado(new EstadoPago());
        } else {
            System.out.println("Falha ao processar o pagamento do pedido #" + pedido.getIdPedido());
            pedido.mudarEstado(new EstadoFalhaPagamento());
        }
    }

    @Override
    public void cancelar(Pedido pedido) {
        pedido.mudarEstado(new EstadoCancelado());
    }
}
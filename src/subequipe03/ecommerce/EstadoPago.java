package subequipe03.ecommerce;

/**
 * Pagamento confirmado. O pedido pode ser enviado ou cancelado (com estorno
 * e devolução ao estoque).
 */
public class EstadoPago implements EstadoPedido {

    @Override
    public String getNome() {
        return "PAGO";
    }

    @Override
    public void enviar(Pedido pedido) {
        pedido.mudarEstado(new EstadoEnviado());
    }

    @Override
    public void cancelar(Pedido pedido) {
        pedido.mudarEstado(new EstadoCancelado());
    }
}

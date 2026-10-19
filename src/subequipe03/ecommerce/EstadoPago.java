package subequipe03.ecommerce;

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

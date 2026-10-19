package subequipe03.ecommerce;

public class EstadoEnviado implements EstadoPedido {

    @Override
    public String getNome() {
        return "ENVIADO";
    }

    @Override
    public void entregar(Pedido pedido) {
        pedido.mudarEstado(new EstadoEntregue());
    }
}

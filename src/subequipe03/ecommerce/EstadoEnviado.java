package subequipe03.ecommerce;

/**
 * Pedido entregue à transportadora. A partir daqui não é mais possível
 * cancelar; só resta confirmar a entrega.
 */
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

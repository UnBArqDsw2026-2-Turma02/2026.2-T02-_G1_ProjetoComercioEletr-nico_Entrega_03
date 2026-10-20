package subequipe03.ecommerce;

/** Estado final: pedido cancelado. Nenhuma transição é permitida. */
public class EstadoCancelado implements EstadoPedido {

    @Override
    public String getNome() {
        return "CANCELADO";
    }
}

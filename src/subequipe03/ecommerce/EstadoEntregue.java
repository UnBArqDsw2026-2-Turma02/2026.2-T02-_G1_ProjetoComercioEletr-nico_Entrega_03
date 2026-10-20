package subequipe03.ecommerce;

/** Estado final: pedido entregue ao cliente. Nenhuma transição é permitida. */
public class EstadoEntregue implements EstadoPedido {

    @Override
    public String getNome() {
        return "ENTREGUE";
    }
}

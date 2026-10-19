package subequipe03.ecommerce;

public class EstadoCancelado implements EstadoPedido {

    @Override
    public String getNome() {
        return "CANCELADO";
    }
}
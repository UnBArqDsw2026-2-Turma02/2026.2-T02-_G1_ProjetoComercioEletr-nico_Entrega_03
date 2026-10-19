package subequipe03.ecommerce;

public class EstadoEntregue implements EstadoPedido {

    @Override
    public String getNome() {
        return "ENTREGUE";
    }
}
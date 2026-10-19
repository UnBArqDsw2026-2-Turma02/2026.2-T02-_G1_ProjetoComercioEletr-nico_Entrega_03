package subequipe03.ecommerce;

public interface PedidoObserver {

    void atualizar(Pedido pedido, String estadoAnterior);
}
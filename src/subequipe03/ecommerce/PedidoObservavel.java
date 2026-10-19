package subequipe03.ecommerce;

public interface PedidoObservavel {

    void adicionarObservador(PedidoObserver observador);

    void removerObservador(PedidoObserver observador);

    void notificarObservadores(String estadoAnterior);
}
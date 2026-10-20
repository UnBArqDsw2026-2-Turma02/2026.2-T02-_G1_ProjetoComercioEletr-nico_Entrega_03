package subequipe03.ecommerce;

/**
 * Padrão Observer (GoF comportamental) - interface "Subject".
 *
 * Contrato de quem pode ser observado: registrar, remover e notificar
 * observadores. No projeto, quem cumpre esse papel é o {@link Pedido}.
 */
public interface PedidoObservavel {

    void adicionarObservador(PedidoObserver observador);

    void removerObservador(PedidoObserver observador);

    void notificarObservadores(String estadoAnterior);
}

package subequipe03.ecommerce;

/**
 * Padrão Observer (GoF comportamental) - interface "Observer".
 *
 * Qualquer parte do sistema interessada em saber que um pedido mudou de estado
 * implementa esta interface e se registra no pedido. O pedido não conhece as
 * classes concretas dos observadores (baixo acoplamento).
 *
 * Variante "pull": o observador recebe o pedido e consulta o que precisar
 * (por exemplo, o novo estado via {@code pedido.getStatusPedido()}).
 */
public interface PedidoObserver {

    /**
     * Chamado logo após o pedido mudar de estado.
     *
     * @param pedido         pedido que mudou (já no novo estado)
     * @param estadoAnterior nome do estado em que o pedido estava
     */
    void atualizar(Pedido pedido, String estadoAnterior);
}

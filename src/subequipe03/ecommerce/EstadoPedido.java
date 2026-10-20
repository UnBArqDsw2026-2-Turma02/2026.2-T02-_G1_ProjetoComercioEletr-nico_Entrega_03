package subequipe03.ecommerce;

/**
 * Padrão State (GoF comportamental) - interface "State".
 *
 * Declara as operações do ciclo de vida de um Pedido. Cada estado concreto
 * sobrescreve apenas as operações que são válidas para ele; as demais caem na
 * implementação padrão, que rejeita a transição.
 */
public interface EstadoPedido {

    /** Nome do estado, usado para exibição e para os observadores. */
    String getNome();

    default void pagar(Pedido pedido) {
        throw transicaoInvalida("pagar");
    }

    default void enviar(Pedido pedido) {
        throw transicaoInvalida("enviar");
    }

    default void entregar(Pedido pedido) {
        throw transicaoInvalida("entregar");
    }

    default void cancelar(Pedido pedido) {
        throw transicaoInvalida("cancelar");
    }

    private IllegalStateException transicaoInvalida(String operacao) {
        return new IllegalStateException(
                "Não é possível " + operacao + " um pedido no estado " + getNome() + ".");
    }
}

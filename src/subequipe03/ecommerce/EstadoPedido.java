package subequipe03.ecommerce;

public interface EstadoPedido {

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
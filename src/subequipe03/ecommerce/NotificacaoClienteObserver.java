package subequipe03.ecommerce;

/**
 * Observador que avisa o cliente a cada mudança relevante do pedido.
 * O envio é simulado com uma linha no terminal (sem e-mail real).
 */
public class NotificacaoClienteObserver implements PedidoObserver {

    @Override
    public void atualizar(Pedido pedido, String estadoAnterior) {
        String mensagem = switch (pedido.getStatusPedido()) {
            case "PAGO" -> "pagamento confirmado.";
            case "FALHA_PAGAMENTO" -> "não conseguimos processar o pagamento. Tente novamente.";
            case "ENVIADO" -> "seu pedido saiu para entrega.";
            case "ENTREGUE" -> "seu pedido foi entregue. Obrigado pela compra!";
            case "CANCELADO" -> "seu pedido foi cancelado.";
            default -> "seu pedido está agora em " + pedido.getStatusPedido() + ".";
        };

        System.out.println("   [E-mail para " + pedido.getCliente().getEmail() + "] Pedido #"
                + pedido.getIdPedido() + ": " + mensagem);
    }
}

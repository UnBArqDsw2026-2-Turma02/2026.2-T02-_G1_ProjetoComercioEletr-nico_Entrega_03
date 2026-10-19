package subequipe03.ecommerce;

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

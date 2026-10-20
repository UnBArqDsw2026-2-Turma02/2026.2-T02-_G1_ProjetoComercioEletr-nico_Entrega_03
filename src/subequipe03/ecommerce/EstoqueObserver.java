package subequipe03.ecommerce;

/**
 * Observador responsável pelo estoque: dá baixa quando o pedido é pago e
 * devolve os itens se um pedido já pago for cancelado.
 */
public class EstoqueObserver implements PedidoObserver {

    @Override
    public void atualizar(Pedido pedido, String estadoAnterior) {
        String novoEstado = pedido.getStatusPedido();

        if (novoEstado.equals("PAGO")) {
            for (ItemCarrinho item : pedido.getCarrinho().getItens()) {
                item.getProduto().baixarEstoque(item.getQuantidade());
                System.out.println("   [Estoque] Baixa de " + item.getQuantidade() + "x "
                        + item.getProduto().getNomeProduto()
                        + " (restam " + item.getProduto().getEstoqueProduto() + ").");
            }
        } else if (novoEstado.equals("CANCELADO") && estadoAnterior.equals("PAGO")) {
            for (ItemCarrinho item : pedido.getCarrinho().getItens()) {
                item.getProduto().reporEstoque(item.getQuantidade());
                System.out.println("   [Estoque] Devolução de " + item.getQuantidade() + "x "
                        + item.getProduto().getNomeProduto()
                        + " (agora " + item.getProduto().getEstoqueProduto() + ").");
            }
        }
    }
}

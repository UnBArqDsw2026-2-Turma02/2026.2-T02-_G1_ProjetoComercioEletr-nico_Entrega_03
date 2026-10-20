package subequipe03.ecommerce;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Demonstração dos padrões State e Observer sobre o Pedido do Fluxo C.
 * Roteiro fixo (sem digitação), pensado para a gravação do vídeo.
 *
 * Compilar e executar a partir da raiz do repositório:
 *   javac -d out src/subequipe03/ecommerce/*.java
 *   java -cp out subequipe03.ecommerce.MainStateObserver
 */
public class MainStateObserver {

    public static void main(String[] args) {
        cenario1FluxoCompleto();
        cenario2FalhaEPagamentoNovamente();
        cenario3TransicoesInvalidas();
        cenario4CancelamentoAposPagamento();
        cenario5RemoverObservador();
    }

    // ---------------------------------------------------------------
    // Cenário 1: caminho feliz CRIADO -> PAGO -> ENVIADO -> ENTREGUE
    // ---------------------------------------------------------------
    private static void cenario1FluxoCompleto() {
        titulo("CENÁRIO 1 - Fluxo completo (CRIADO > PAGO > ENVIADO > ENTREGUE)");

        Produto teclado = new Produto(1, "Teclado Mecânico", "Teclado gamer", new BigDecimal("150.00"), 10);
        HistoricoPedidoObserver historico = new HistoricoPedidoObserver();
        Pedido pedido = novoPedido(1001, teclado, 2);
        pedido.adicionarObservador(new NotificacaoClienteObserver());
        pedido.adicionarObservador(new EstoqueObserver());
        pedido.adicionarObservador(historico);

        System.out.println("Estado inicial: " + pedido.getStatusPedido());
        pedido.setFormaPagamento(new PagamentoPix("camile@exemplo.com", LocalDateTime.now().plusHours(1)));

        passo("pedido.finalizar()");
        pedido.finalizar();
        passo("pedido.enviar()");
        pedido.enviar();
        passo("pedido.entregar()");
        pedido.entregar();

        System.out.println("\nEstado final: " + pedido.getStatusPedido());
        historico.imprimirHistorico();
    }

    // ---------------------------------------------------------------
    // Cenário 2: pagamento recusado, troca da estratégia e nova tentativa
    // ---------------------------------------------------------------
    private static void cenario2FalhaEPagamentoNovamente() {
        titulo("CENÁRIO 2 - Pagamento recusado e nova tentativa (State + Strategy)");

        Produto mouse = new Produto(2, "Mouse Gamer", "Mouse RGB", new BigDecimal("80.00"), 5);
        Pedido pedido = novoPedido(1002, mouse, 1);
        pedido.adicionarObservador(new NotificacaoClienteObserver());
        pedido.adicionarObservador(new EstoqueObserver());

        // Estratégia que sempre recusa (simula cartão negado)
        FormaPagamento cartaoRecusado = valor -> {
            System.out.println("Cartão recusado pela operadora (simulação) - valor R$ " + valor);
            return false;
        };
        pedido.setFormaPagamento(cartaoRecusado);

        passo("pedido.finalizar()  [cartão recusado]");
        pedido.finalizar();
        System.out.println("Estado: " + pedido.getStatusPedido());

        passo("troca a estratégia para Pix e chama pedido.finalizar() de novo");
        pedido.setFormaPagamento(new PagamentoPix("11999990000", LocalDateTime.now().plusHours(1)));
        pedido.finalizar();
        System.out.println("Estado: " + pedido.getStatusPedido());
    }

    // ---------------------------------------------------------------
    // Cenário 3: o State impede transições que não fazem sentido
    // ---------------------------------------------------------------
    private static void cenario3TransicoesInvalidas() {
        titulo("CENÁRIO 3 - Transições inválidas são bloqueadas pelo State");

        Produto teclado = new Produto(1, "Teclado Mecânico", "Teclado gamer", new BigDecimal("150.00"), 10);
        Pedido pedido = novoPedido(1003, teclado, 1);
        pedido.adicionarObservador(new NotificacaoClienteObserver());
        pedido.setFormaPagamento(new PagamentoPix("camile@exemplo.com", LocalDateTime.now().plusHours(1)));

        tentar(pedido, "enviar um pedido que ainda não foi pago", pedido::enviar);

        passo("pedido.finalizar()  [agora com pagamento válido]");
        pedido.finalizar();
        tentar(pedido, "pagar de novo um pedido já pago", pedido::finalizar);

        passo("pedido.enviar()");
        pedido.enviar();
        tentar(pedido, "cancelar um pedido já enviado", pedido::cancelar);

        passo("pedido.entregar()");
        pedido.entregar();
        tentar(pedido, "enviar um pedido já entregue", pedido::enviar);
        System.out.println("\nEstado final: " + pedido.getStatusPedido());
    }

    // ---------------------------------------------------------------
    // Cenário 4: cancelamento após o pagamento devolve o estoque
    // ---------------------------------------------------------------
    private static void cenario4CancelamentoAposPagamento() {
        titulo("CENÁRIO 4 - Cancelamento após pagamento (Observer devolve o estoque)");

        Produto mouse = new Produto(2, "Mouse Gamer", "Mouse RGB", new BigDecimal("80.00"), 5);
        Pedido pedido = novoPedido(1004, mouse, 3);
        pedido.adicionarObservador(new NotificacaoClienteObserver());
        pedido.adicionarObservador(new EstoqueObserver());
        pedido.setFormaPagamento(new PagamentoPix("camile@exemplo.com", LocalDateTime.now().plusHours(1)));

        System.out.println("Estoque antes: " + mouse.getEstoqueProduto());
        passo("pedido.finalizar()");
        pedido.finalizar();
        passo("pedido.cancelar()");
        pedido.cancelar();
        System.out.println("\nEstoque depois: " + mouse.getEstoqueProduto() + " | Estado: " + pedido.getStatusPedido());
    }

    // ---------------------------------------------------------------
    // Cenário 5: observadores podem ser removidos em tempo de execução
    // ---------------------------------------------------------------
    private static void cenario5RemoverObservador() {
        titulo("CENÁRIO 5 - Remover um observador em tempo de execução");

        Produto teclado = new Produto(1, "Teclado Mecânico", "Teclado gamer", new BigDecimal("150.00"), 10);
        NotificacaoClienteObserver notificacao = new NotificacaoClienteObserver();
        HistoricoPedidoObserver historico = new HistoricoPedidoObserver();
        Pedido pedido = novoPedido(1005, teclado, 1);
        pedido.adicionarObservador(notificacao);
        pedido.adicionarObservador(historico);
        pedido.setFormaPagamento(new PagamentoPix("camile@exemplo.com", LocalDateTime.now().plusHours(1)));

        passo("pedido.finalizar()  [com notificação ao cliente]");
        pedido.finalizar();

        passo("cliente desativa os e-mails: removerObservador(notificacao)");
        pedido.removerObservador(notificacao);

        passo("pedido.enviar()  [sem e-mail, mas o histórico continua registrando]");
        pedido.enviar();
        historico.imprimirHistorico();
    }

    // ---------------------------------------------------------------
    // Apoio
    // ---------------------------------------------------------------

    private static Pedido novoPedido(int id, Produto produto, int quantidade) {
        Cliente cliente = new Cliente(1, "Camile Barbosa", "camile@exemplo.com", "61999990000");
        Endereco endereco = new Endereco("Ed Chateau", "120", "Apto 2", "Brasília", "DF", "70000-000", "Asa Sul");
        cliente.cadastrarEndereco(endereco);

        Carrinho carrinho = cliente.montarCarrinho();
        carrinho.adicionarProduto(produto, quantidade);

        Frete frete = new Frete(1, "TransBrasil");
        frete.calcular(endereco, carrinho.calcularSubtotal());

        return new Pedido(id, cliente, carrinho, frete);
    }

    private static void tentar(Pedido pedido, String descricao, Runnable acao) {
        passo("tentar " + descricao + " (estado atual: " + pedido.getStatusPedido() + ")");
        try {
            acao.run();
            System.out.println("   (permitido)");
        } catch (IllegalStateException e) {
            System.out.println("   BLOQUEADO: " + e.getMessage());
        }
    }

    private static void titulo(String texto) {
        System.out.println("\n==================================================================");
        System.out.println(texto);
        System.out.println("==================================================================");
    }

    private static void passo(String texto) {
        System.out.println("\n> " + texto);
    }
}

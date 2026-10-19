package subequipe03.ecommerce;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== SISTEMA DE E-COMMERCE INTERATIVO ===\n");

        // 1. Coletando dados do Cliente
        System.out.print("Digite o seu nome: ");
        String nomeCliente = scanner.nextLine();

        System.out.print("Digite o seu e-mail: ");
        String emailCliente = scanner.nextLine();

        System.out.print("Digite o seu telefone: ");
        String telefoneCliente = scanner.nextLine();

        Cliente cliente = new Cliente(1, nomeCliente, emailCliente, telefoneCliente);

        // Endereço fixo ou simulado para entrega
        Endereco end = new Endereco("Ed Chateau", "120", "Apto 2", "Brasília", "DF", "70000-000", "Asa Sul");
        cliente.cadastrarEndereco(end);
        Endereco enderecoEntrega = cliente.getEnderecos().get(0);

        System.out.println("\nCliente cadastrado com sucesso!");
        System.out.println(cliente.getDados());
        System.out.println("Endereço de Entrega: " + enderecoEntrega.getEndereco() + "\n");

        // 2. Produtos disponíveis na loja
        Produto teclado = new Produto(1, "Teclado Mecânico", "Teclado gamer", new BigDecimal("150.00"), 10);
        Produto mouse = new Produto(2, "Mouse Gamer", "Mouse RGB", new BigDecimal("80.00"), 5);

        Carrinho carrinho = cliente.montarCarrinho();

        // 3. Escolhendo produtos de forma interativa
        boolean continuarComprando = true;
        while (continuarComprando) {
            System.out.println("--- PRODUTOS DISPONÍVEIS ---");
            System.out.println("1 - Teclado Mecânico (R$ 150,00)");
            System.out.println("2 - Mouse Gamer (R$ 80,00)");
            System.out.print("Escolha o número do produto que deseja adicionar ao carrinho: ");
            int opcaoProduto = scanner.nextInt();

            System.out.print("Digite a quantidade: ");
            int qtd = scanner.nextInt();

            if (opcaoProduto == 1) {
                carrinho.adicionarProduto(teclado, qtd);
                System.out.println("-> " + qtd + "x Teclado(s) adicionado(s)!");
            } else if (opcaoProduto == 2) {
                carrinho.adicionarProduto(mouse, qtd);
                System.out.println("-> " + qtd + "x Mouse(s) adicionado(s)!");
            } else {
                System.out.println("Opção inválida!");
            }

            System.out.print("Deseja adicionar mais algum produto? (1-Sim / 2-Não): ");
            int resposta = scanner.nextInt();
            if (resposta != 1) {
                continuarComprando = false;
            }
        }

        // Cupom de desconto opcional
        scanner.nextLine(); // Limpa o buffer do teclado
        System.out.print("\nPossui cupom de desconto? (Digite DESCONTO10 ou aperte Enter para pular): ");
        String cupom = scanner.nextLine();
        if (!cupom.isBlank()) {
            try {
                carrinho.aplicarCupom(cupom);
                System.out.println("Cupom aplicado com sucesso!");
            } catch (Exception e) {
                System.out.println("Aviso: " + e.getMessage());
            }
        }

        BigDecimal subtotal = carrinho.calcularSubtotal();
        System.out.println("\nSubtotal do Carrinho: R$ " + subtotal);

        // 4. Calculando o Frete
        Frete frete = new Frete(1, "TransBrasil");
        frete.calcular(enderecoEntrega, subtotal);
        System.out.println("Frete para Brasília: R$ " + frete.getValorFrete() + " (" + frete.getPrazoEntrega() + ")\n");

        // 5. Criando o Pedido
        Pedido pedido = new Pedido(1001, cliente, carrinho, frete);

        // 6. Escolha da Forma de Pagamento (Demonstrando o Strategy de forma interativa!)
        System.out.println("--- ESCOLHA A FORMA DE PAGAMENTO ---");
        System.out.println("1 - PIX");
        System.out.println("2 - Cartão de Crédito");
        System.out.println("3 - Boleto Bancário");
        System.out.print("Digite a opção de pagamento: ");
        int opcaoPagamento = scanner.nextInt();
        scanner.nextLine(); // Limpa o buffer

        FormaPagamento formaPagamento = null;

        if (opcaoPagamento == 1) {
            System.out.print("Digite sua chave Pix: ");
            String chave = scanner.nextLine();
            formaPagamento = new PagamentoPix(chave, LocalDateTime.now().plusHours(1));
        } else if (opcaoPagamento == 2) {
            System.out.print("Digite o número do cartão (16 dígitos): ");
            String numCartao = scanner.nextLine();
            System.out.print("Nome do titular: ");
            String titular = scanner.nextLine();
            System.out.print("Quantidade de parcelas: ");
            int parcelas = scanner.nextInt();
            formaPagamento = new PagamentoCartaoCredito(numCartao, titular, "12/28", 123, "12345678900", parcelas);
        } else {
            formaPagamento = new PagamentoBoleto("34191.79001 01043.510047 91020.150002 6", "34191790010104351004791020150002", LocalDateTime.now().plusDays(3), "E-commerce UnB");
            System.out.println("Boleto selecionado.");
        }

        // Vinculando ao pedido
        pedido.setFormaPagamento(formaPagamento);

        // 7. Finalizando o Pedido
        System.out.println("\n--- PROCESSANDO COMPRA ---");
        pedido.finalizar();

        System.out.println("\n=== FIM DA EXECUÇÃO INTERATIVA ===");
        scanner.close();
    }
}
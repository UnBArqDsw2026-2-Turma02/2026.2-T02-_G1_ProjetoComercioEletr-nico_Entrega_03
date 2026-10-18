package subequipe03.ecommerce;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== INICIANDO SISTEMA DE E-COMMERCE ==-\n");

        // 1. Cadastrando o Cliente e seu Endereço
        Cliente cliente = new Cliente(1, "Maria Eduarda", "mariaed@email.com", "61995161578");
        Endereco end = new Endereco("Ed Chateau", "120", "Apto 2", "Brasília", "DF", "70000-000", "Asa Sul");
        cliente.cadastrarEndereco(end);
        Endereco enderecoEntrega = cliente.getEnderecos().get(0);

        System.out.println(cliente.getDados());
        System.out.println("Endereço de Entrega: " + enderecoEntrega.getEndereco() + "\n");

        // 2. Criando Produtos
        Produto teclado = new Produto(1, "Teclado", "Teclado mecânico", new BigDecimal("150.00"), 10);
        Produto mouse = new Produto(2, "Mouse", "Mouse gamer", new BigDecimal("80.00"), 5);

        // 3. Montando o Carrinho e adicionando produtos
        Carrinho carrinho = cliente.montarCarrinho();
        carrinho.adicionarProduto(teclado, 1);
        carrinho.adicionarProduto(mouse, 2);
        
        // Aplicando um cupom de desconto opcional
        carrinho.aplicarCupom("DESCONTO10");
        BigDecimal subtotal = carrinho.calcularSubtotal();
        System.out.println("Subtotal do Carrinho (com cupom): R$ " + subtotal);

        // 4. Calculando o Frete
        Frete frete = new Frete(1, "TransBrasil");
        frete.calcular(enderecoEntrega, subtotal);
        System.out.println("Frete: R$ " + frete.getValorFrete() + " (" + frete.getPrazoEntrega() + ")\n");

        // 5. Criando o Pedido
        Pedido pedido = new Pedido(1001, cliente, carrinho, frete);

        // 6. Escolhendo a Forma de Pagamento (Padrão Strategy - Exemplo com PIX)
        FormaPagamento pagamentoPix = new PagamentoPix("maria@pix.com", LocalDateTime.now().plusHours(1));
        
        // outros testes:
        // FormaPagamento pagamentoCartao = new PagamentoCartaoCredito("1234567890123456", "Maria E", "12/28", 123, "12345678900", 2);

        // Vinculando a forma de pagamento ao pedido
        pedido.setFormaPagamento(pagamentoPix);

        // 7. Finalizando o Pedido (Processa o pagamento e calcula o total)
        System.out.println("--- PROCESSANDO PEDIDO ---");
        pedido.finalizar();
        
        System.out.println("\n=== FIM DA EXECUÇÃO ===");
    }
}
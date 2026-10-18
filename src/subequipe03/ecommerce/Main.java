package subequipe03.ecommerce;

import java.math.BigDecimal;


public class Main {
    public static void main(String[] args) {
        Cliente cliente = new Cliente(1, "Maria Eduarda", "mariaed@email.com", "61995161578");
        Endereco end = new Endereco("Ed Chateau", "120", "Apto 2", "Brasília", "DF", "70000-000", "Asa Sul");
        cliente.cadastrarEndereco(end);
        Endereco enderecoEntrega = cliente.getEnderecos().get(0);


        Produto teclado = new Produto(1, "Teclado", "Teclado mecânico", new BigDecimal("150.00"), 10);
        Produto mouse = new Produto(2, "Mouse", "Mouse gamer", new BigDecimal("80.00"), 5);

        Carrinho carrinho = cliente.montarCarrinho();
        carrinho.adicionarProduto(teclado, 1);
        carrinho.adicionarProduto(mouse, 2);
        carrinho.aplicarCupom("DESCONTO10");

        BigDecimal subtotal = carrinho.calcularSubtotal();
        Frete frete = new Frete(1, "TransBrasil");
        frete.calcular(enderecoEntrega, subtotal);

        System.out.println(cliente.getDados());
        System.out.println(enderecoEntrega.getEndereco());
        System.out.println("Subtotal c/ cupom: " + subtotal);
        System.out.println("Frete: " + frete.getValorFrete() + " (" + frete.getPrazoEntrega() + ")");
    }
}

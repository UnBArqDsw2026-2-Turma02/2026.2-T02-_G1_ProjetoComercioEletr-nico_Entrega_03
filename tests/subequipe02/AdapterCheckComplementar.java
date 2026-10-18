package subequipe02;

import java.math.BigDecimal;
import java.util.List;

/** Testes complementares do Adapter, sem dependências externas. */
public final class AdapterCheckComplementar {

    private AdapterCheckComplementar() {
    }

    public static void main(String[] args) {
        verificarBuscaPorSapato();
        verificarBuscaPorTrecho();
        verificarTermoVazio();
        verificarTermoNulo();
        verificarCatalogoNulo();
        verificarFornecedorNulo();

        System.out.println("Todos os 6 cenarios complementares passaram.");
    }

    private static BuscaProdutos criarBusca() {
        return new BuscaProdutos(
                new CatalogoAdapter(new CatalogoExternoSimulado()));
    }

    private static void verificarBuscaPorSapato() {
        List<Produto> produtos = criarBusca().pesquisar("Sapato");

        verificar(produtos.size() == 1,
                "Sapato deve retornar exatamente um produto");

        verificarProduto(produtos.get(0),
                "SAPATO-01", "Sapato de corrida", "249.90");

        aprovado("busca por Sapato e conversao dos campos");
    }

    private static void verificarBuscaPorTrecho() {
        List<Produto> produtos = criarBusca().pesquisar("corrida");

        verificar(produtos.size() == 1,
                "corrida deve retornar exatamente um produto");

        verificarProduto(produtos.get(0),
                "SAPATO-01", "Sapato de corrida", "249.90");

        aprovado("busca por trecho da descricao");
    }

    private static void verificarTermoVazio() {
        List<Produto> produtos = criarBusca().pesquisar("");

        verificar(produtos.size() == 3,
                "termo vazio deve retornar os tres produtos");

        verificarProduto(produtos.get(0),
                "BOLA-01", "Bola de futebol", "129.90");
        verificarProduto(produtos.get(1),
                "BOLA-02", "Bola de basquete", "89.90");
        verificarProduto(produtos.get(2),
                "SAPATO-01", "Sapato de corrida", "249.90");

        aprovado("termo vazio preserva todos os produtos e sua ordem");
    }

    private static void verificarTermoNulo() {
        BuscaProdutos busca = criarBusca();

        esperarNullPointer(
                () -> busca.pesquisar(null),
                "a pesquisa deve rejeitar termo nulo");

        aprovado("rejeicao de termo nulo");
    }

    private static void verificarCatalogoNulo() {
        esperarNullPointer(
                () -> new BuscaProdutos(null),
                "BuscaProdutos deve rejeitar catalogo nulo");

        aprovado("rejeicao de catalogo nulo no cliente");
    }

    private static void verificarFornecedorNulo() {
        esperarNullPointer(
                () -> new CatalogoAdapter(null),
                "CatalogoAdapter deve rejeitar fornecedor nulo");

        aprovado("rejeicao de fornecedor nulo no Adapter");
    }

    private static void verificarProduto(
            Produto produto, String id, String nome, String preco) {

        verificar(id.equals(produto.getId()),
                "identificador incorreto: " + id);

        verificar(nome.equals(produto.getNome()),
                "nome incorreto: " + id);

        verificar(new BigDecimal(preco).equals(produto.getPrecoAtual()),
                "preco ou escala decimal incorretos: " + id);
    }

    private static void esperarNullPointer(
            Runnable acao, String mensagem) {

        try {
            acao.run();
        } catch (NullPointerException esperada) {
            return;
        }

        throw new AssertionError(mensagem);
    }

    private static void verificar(boolean condicao, String mensagem) {
        if (!condicao) {
            throw new AssertionError(mensagem);
        }
    }

    private static void aprovado(String cenario) {
        System.out.println("OK - " + cenario);
    }
}
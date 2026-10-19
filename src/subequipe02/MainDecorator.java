package subequipe02;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/** Executa a mesma busca sem e com o Decorator de disponibilidade. */
public final class MainDecorator {
    private MainDecorator() {
    }

    public static void main(String[] args) {
        String termo = args.length > 0 ? String.join(" ", args) : "Bola";

        CatalogoProdutos adaptado = new CatalogoAdapter(new CatalogoExternoSimulado());
        CatalogoProdutos decorado = new CatalogoComDisponibilidade(adaptado, new EstoqueSimulado());

        executarBusca("Sem Decorator", new BuscaProdutos(adaptado), termo);
        executarBusca("Com Decorator (somente disponiveis)", new BuscaProdutos(decorado), termo);
    }

    private static void executarBusca(String titulo, BuscaProdutos busca, String termo) {
        System.out.println("== " + titulo + " | Busca: " + termo);
        List<Produto> produtos = busca.pesquisar(termo);
        if (produtos.isEmpty()) {
            System.out.println("Nenhum produto encontrado.");
            return;
        }

        NumberFormat moeda = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-BR"));
        for (Produto produto : produtos) {
            String preco = moeda.format(produto.getPrecoAtual())
                    .replace(' ', ' ')
                    .replace(' ', ' ');
            System.out.println(produto.getId() + " | " + produto.getNome() + " | " + preco);
        }
    }
}

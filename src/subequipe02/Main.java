package subequipe02;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/** Executa os cenários positivo e vazio da demonstração. */
public final class Main {
    private Main() {
    }

    public static void main(String[] args) {
        CatalogoExternoSimulado fornecedor = new CatalogoExternoSimulado();
        CatalogoProdutos catalogo = new CatalogoAdapter(fornecedor);
        BuscaProdutos busca = new BuscaProdutos(catalogo);

        if (args.length > 0) {
            executarBusca(busca, String.join(" ", args));
            return;
        }

        executarBusca(busca, "Bola");
        executarBusca(busca, "sem-correspondencia");
    }

    private static void executarBusca(BuscaProdutos busca, String termo) {
        System.out.println("Busca: " + termo);
        List<Produto> produtos = busca.pesquisar(termo);
        if (produtos.isEmpty()) {
            System.out.println("Nenhum produto encontrado.");
            return;
        }

        NumberFormat moeda = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-BR"));
        for (Produto produto : produtos) {
            String precoFormatado = moeda.format(produto.getPrecoAtual())
                    .replace('\u00a0', ' ')
                    .replace('\u202f', ' ');
            System.out.println(produto.getId() + " | " + produto.getNome() + " | "
                    + precoFormatado);
        }
    }
}

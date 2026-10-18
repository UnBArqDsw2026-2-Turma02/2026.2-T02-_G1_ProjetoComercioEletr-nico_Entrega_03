package subequipe02;

import java.util.List;
import java.util.Objects;

/** Cliente que conhece somente o contrato interno de catálogo. */
public final class BuscaProdutos {
    private final CatalogoProdutos catalogo;

    public BuscaProdutos(CatalogoProdutos catalogo) {
        this.catalogo = Objects.requireNonNull(catalogo, "catalogo");
    }

    public List<Produto> pesquisar(String termo) {
        return catalogo.buscarProdutos(termo);
    }
}

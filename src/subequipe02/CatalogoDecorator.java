package subequipe02;

import java.util.List;
import java.util.Objects;

/** Decorator base: implementa o mesmo contrato e repassa a chamada ao catálogo envolvido. */
public abstract class CatalogoDecorator implements CatalogoProdutos {
    protected final CatalogoProdutos catalogo;

    protected CatalogoDecorator(CatalogoProdutos catalogo) {
        this.catalogo = Objects.requireNonNull(catalogo, "catalogo");
    }

    @Override
    public List<Produto> buscarProdutos(String termo) {
        return catalogo.buscarProdutos(termo);
    }
}

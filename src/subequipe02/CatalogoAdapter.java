package subequipe02;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/** Adapter de objeto: traduz a chamada e os dados do catálogo simulado. */
public final class CatalogoAdapter implements CatalogoProdutos {
    private final CatalogoExternoSimulado catalogoExterno;

    public CatalogoAdapter(CatalogoExternoSimulado catalogoExterno) {
        this.catalogoExterno = Objects.requireNonNull(catalogoExterno, "catalogoExterno");
    }

    @Override
    public List<Produto> buscarProdutos(String termo) {
        return catalogoExterno.consultarItens(termo).stream()
                .map(item -> new Produto(
                        item.getCodigo(),
                        item.getDescricao(),
                        BigDecimal.valueOf(item.getValorEmCentavos(), 2)))
                .toList();
    }
}

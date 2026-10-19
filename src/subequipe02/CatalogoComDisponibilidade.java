package subequipe02;

import java.util.List;
import java.util.Objects;

/** Decorator concreto: acrescenta a regra de exibir somente produtos disponíveis. */
public final class CatalogoComDisponibilidade extends CatalogoDecorator {
    private final ConsultaEstoque estoque;

    public CatalogoComDisponibilidade(CatalogoProdutos catalogo, ConsultaEstoque estoque) {
        super(catalogo);
        this.estoque = Objects.requireNonNull(estoque, "estoque");
    }

    @Override
    public List<Produto> buscarProdutos(String termo) {
        return super.buscarProdutos(termo).stream()
                .filter(produto -> estoque.estaDisponivel(produto.getId()))
                .toList();
    }
}

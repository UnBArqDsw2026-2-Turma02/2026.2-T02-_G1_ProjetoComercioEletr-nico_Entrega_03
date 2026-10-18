package subequipe02;

import java.util.List;

/** Contrato interno esperado pela busca de produtos (Target). */
public interface CatalogoProdutos {
    List<Produto> buscarProdutos(String termo);
}

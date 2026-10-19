package subequipe02;

import java.util.Map;
import java.util.Objects;

/** Estoque fictício usado apenas nesta demonstração do Decorator. */
public final class EstoqueSimulado implements ConsultaEstoque {
    private static final Map<String, Integer> QUANTIDADES = Map.of(
            "BOLA-01", 5,
            "BOLA-02", 0,
            "SAPATO-01", 2);

    /** Produto sem registro de estoque é tratado como indisponível. */
    @Override
    public boolean estaDisponivel(String idProduto) {
        Objects.requireNonNull(idProduto, "idProduto");
        return QUANTIDADES.getOrDefault(idProduto, 0) > 0;
    }
}

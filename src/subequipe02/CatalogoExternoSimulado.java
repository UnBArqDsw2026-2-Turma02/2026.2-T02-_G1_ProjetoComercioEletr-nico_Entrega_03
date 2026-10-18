package subequipe02;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

/** Contrato incompatível e dados fictícios usados apenas nesta demonstração (Adaptee). */
public final class CatalogoExternoSimulado {
    private static final List<ItemExterno> ITENS = List.of(
            new ItemExterno("BOLA-01", "Bola de futebol", 12990L),
            new ItemExterno("BOLA-02", "Bola de basquete", 8990L),
            new ItemExterno("SAPATO-01", "Sapato de corrida", 24990L));

    public List<ItemExterno> consultarItens(String termo) {
        String trecho = Objects.requireNonNull(termo, "termo").toLowerCase(Locale.ROOT);
        return ITENS.stream()
                .filter(item -> item.getDescricao().toLowerCase(Locale.ROOT).contains(trecho))
                .toList();
    }
}

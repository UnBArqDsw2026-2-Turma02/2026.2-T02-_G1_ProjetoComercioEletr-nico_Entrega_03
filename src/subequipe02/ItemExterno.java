package subequipe02;

import java.util.Objects;

/** Formato de item oferecido pelo catalogo externo simulado. */
public final class ItemExterno {
    private final String codigo;
    private final String descricao;
    private final long valorEmCentavos;

    public ItemExterno(String codigo, String descricao, long valorEmCentavos) {
        this.codigo = Objects.requireNonNull(codigo, "codigo");
        this.descricao = Objects.requireNonNull(descricao, "descricao");
        this.valorEmCentavos = valorEmCentavos;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public long getValorEmCentavos() {
        return valorEmCentavos;
    }
}

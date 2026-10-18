package subequipe02;

import java.math.BigDecimal;
import java.util.Objects;

/** Recorte de Produto utilizado na demonstração do Adapter. */
public final class Produto {
    private final String id;
    private final String nome;
    private final BigDecimal precoAtual;

    public Produto(String id, String nome, BigDecimal precoAtual) {
        this.id = Objects.requireNonNull(id, "id");
        this.nome = Objects.requireNonNull(nome, "nome");
        this.precoAtual = Objects.requireNonNull(precoAtual, "precoAtual");
    }

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public BigDecimal getPrecoAtual() {
        return precoAtual;
    }
}

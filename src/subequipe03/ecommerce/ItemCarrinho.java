package subequipe03.ecommerce;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ItemCarrinho {
    private int quantidade;
    private final BigDecimal precoUnitario;
    private final Produto produto;

    public ItemCarrinho(Produto produto, int quantidade) {
        if (produto == null) {
            throw new IllegalArgumentException("Produto não pode ser nulo.");
        }
        if (quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior que zero.");
        }
        this.produto = produto;
        this.quantidade = quantidade;
        
        this.precoUnitario = produto.getPreco();
    }

    public BigDecimal calcularSubtotal() {
        return precoUnitario
                .multiply(BigDecimal.valueOf(quantidade))
                .setScale(2, RoundingMode.HALF_UP);
    }


    public int getQuantidade() { return quantidade; }
    public BigDecimal getPrecoUnitario() { return precoUnitario; }
    public Produto getProduto() { return produto; }
}

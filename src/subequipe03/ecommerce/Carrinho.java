package subequipe03.ecommerce;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Carrinho {

    
    private static final Map<String, BigDecimal> CUPONS = Map.of(
            "DESCONTO10", new BigDecimal("10"),
            "DESCONTO20", new BigDecimal("20")
    );

  
    private final List<ItemCarrinho> itens = new ArrayList<>();
    private String cupomDesconto;

    public void adicionarProduto(Produto produto, int quantidade) {
        if (produto == null) {
            throw new IllegalArgumentException("Produto não pode ser nulo.");
        }
        if (quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior que zero.");
        }

        else {
            itens.add(new ItemCarrinho(produto, quantidade));
        }
    }

    public void removerProduto(Produto produto) {
        boolean removido = itens.removeIf(i -> i.getProduto().getIdProduto() == produto.getIdProduto());
        if (!removido) {
            throw new IllegalArgumentException("Produto não está no carrinho.");
        }
    }

  
  public BigDecimal calcularSubtotal() {
    BigDecimal subtotal = itens.stream()
            .map(ItemCarrinho::calcularSubtotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    if (cupomDesconto != null) {
        BigDecimal percentual = CUPONS.get(cupomDesconto);
        BigDecimal desconto = subtotal.multiply(percentual)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        subtotal = subtotal.subtract(desconto);
    }

    return subtotal.setScale(2, RoundingMode.HALF_UP);
}

    public void aplicarCupom(String codigo) {
        if (codigo == null || !CUPONS.containsKey(codigo.trim().toUpperCase())) {
            throw new IllegalArgumentException("Cupom inválido: " + codigo);
        }
        this.cupomDesconto = codigo.trim().toUpperCase();
    }

    
    public List<ItemCarrinho> getItens() { return Collections.unmodifiableList(itens); }
    public String getCupomDesconto() { return cupomDesconto; }
}

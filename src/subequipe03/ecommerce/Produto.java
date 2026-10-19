package subequipe03.ecommerce;

import java.math.BigDecimal;

public class Produto {
    private int idProduto;
    private String nomeProduto;
    private String descricaoProduto;
    private BigDecimal precoProduto;
    private int estoqueProduto;

    public Produto(int idProduto, String nomeProduto, String descricaoProduto,
                   BigDecimal precoProduto, int estoqueProduto) {
        if (precoProduto == null || precoProduto.signum() < 0) {
            throw new IllegalArgumentException("Preço inválido.");
        }
        if (estoqueProduto < 0) {
            throw new IllegalArgumentException("Estoque não pode ser negativo.");
        }
        this.idProduto = idProduto;
        this.nomeProduto = nomeProduto;
        this.descricaoProduto = descricaoProduto;
        this.precoProduto = precoProduto;
        this.estoqueProduto = estoqueProduto;
    }

    public BigDecimal getPreco() { return precoProduto; }
    public boolean temEstoque(int quantidade) {
        return quantidade > 0 && estoqueProduto >= quantidade;
    }

    /** Baixa de estoque (a ser chamada ao finalizar o pedido). */
    public void baixarEstoque(int quantidade) {
        if (!temEstoque(quantidade)) {
            throw new IllegalStateException("Estoque insuficiente para: " + nomeProduto);
        }
        estoqueProduto -= quantidade;
    }
 
    /** Devolução ao estoque (ex.: cancelamento de um pedido já pago). */
    public void reporEstoque(int quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior que zero.");
        }
        estoqueProduto += quantidade;
    }

    public int getIdProduto() { return idProduto; }
    public String getNomeProduto() { return nomeProduto; }
    public void setNomeProduto(String nomeProduto) { this.nomeProduto = nomeProduto; }
    public String getDescricaoProduto() { return descricaoProduto; }
    public void setDescricaoProduto(String descricaoProduto) { this.descricaoProduto = descricaoProduto; }
    public BigDecimal getPrecoProduto() { return precoProduto; }
    public void setPrecoProduto(BigDecimal precoProduto) { this.precoProduto = precoProduto; }
    public int getEstoqueProduto() { return estoqueProduto; }
}

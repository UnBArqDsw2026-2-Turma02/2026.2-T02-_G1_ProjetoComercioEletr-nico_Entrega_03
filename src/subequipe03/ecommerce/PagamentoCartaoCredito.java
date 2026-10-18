package subequipe03.ecommerce;

import java.math.BigDecimal;

public class PagamentoCartaoCredito implements FormaPagamento {
    private String numeroCartao;
    private String titularCartao;
    private String validadeCartao;
    private Integer cvvCartao;
    private String cpfTitular;
    private Integer qtdParcelas;

    public PagamentoCartaoCredito(String numeroCartao, String titularCartao, String validadeCartao, Integer cvvCartao, String cpfTitular, Integer qtdParcelas) {
        this.numeroCartao = numeroCartao;
        this.titularCartao = titularCartao;
        this.validadeCartao = validadeCartao;
        this.cvvCartao = cvvCartao;
        this.cpfTitular = cpfTitular;
        this.qtdParcelas = qtdParcelas;
    }

    @Override
    public boolean processarPagamento(BigDecimal valor) {
        System.out.println("Processando Cartão de Crédito para " + titularCartao + " em " + qtdParcelas + "x de R$ " + (valor.divide(BigDecimal.valueOf(qtdParcelas), 2, java.math.RoundingMode.HALF_UP)));
        return true;
    }
}
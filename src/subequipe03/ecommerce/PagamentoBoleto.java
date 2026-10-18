package subequipe03.ecommerce;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PagamentoBoleto implements FormaPagamento {
    private String codigoBarras;
    private String linhaDigitavel;
    private LocalDateTime dataVencimento;
    private String nomeBeneficiario;

    public PagamentoBoleto(String codigoBarras, String linhaDigitavel, LocalDateTime dataVencimento, String nomeBeneficiario) {
        this.codigoBarras = codigoBarras;
        this.linhaDigitavel = linhaDigitavel;
        this.dataVencimento = dataVencimento;
        this.nomeBeneficiario = nomeBeneficiario;
    }

    @Override
    public boolean processarPagamento(BigDecimal valor) {
        System.out.println("Gerando Boleto para o beneficiário " + nomeBeneficiario + " no valor de R$ " + valor);
        return true;
    }

    public void gerartBoleto(BigDecimal valor) {
        System.out.println("Boleto gerado com linha digitável: " + linhaDigitavel);
    }

    public Boolean estaVencido() {
        return LocalDateTime.now().isAfter(dataVencimento);
    }
}
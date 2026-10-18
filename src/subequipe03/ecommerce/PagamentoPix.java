package subequipe03.ecommerce;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PagamentoPix implements FormaPagamento {
    private LocalDateTime validadePix;
    private String chavePix;

    public PagamentoPix(String chavePix, LocalDateTime validadePix) {
        this.chavePix = chavePix;
        this.validadePix = validadePix;
    }

    @Override
    public boolean processarPagamento(BigDecimal valor) {
        System.out.println("Processando Pix no valor de R$ " + valor + " com a chave " + chavePix);
        return true;
    }

    public LocalDateTime getValidadePix() { return validadePix; }
    public String getChavePix() { return chavePix; }
}
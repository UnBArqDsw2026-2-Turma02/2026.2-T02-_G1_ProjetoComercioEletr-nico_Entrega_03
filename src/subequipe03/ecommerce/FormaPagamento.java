package subequipe03.ecommerce;

import java.math.BigDecimal;

public interface FormaPagamento {
    boolean processarPagamento(BigDecimal valor);
}
package subequipe03.ecommerce;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Frete {
    private static final BigDecimal LIMITE_FRETE_GRATIS = new BigDecimal("300.00");

    private final int idFrete;
    private String transportadora;
    private BigDecimal valorFrete;
    private String prazoEntrega;

    public Frete(int idFrete, String transportadora) {
        this.idFrete = idFrete;
        this.transportadora = transportadora;
        this.valorFrete = BigDecimal.ZERO;
    }

    /**
     * Calcula valor e prazo conforme a região do CEP.
     * Frete grátis para subtotal >= R$ 300,00.
     * Tabela fictícia, apenas para fins didáticos.
     */
    public BigDecimal calcular(Endereco destino, BigDecimal subtotal) {
        if (destino == null || destino.getCep() == null) {
            throw new IllegalArgumentException("Endereço de destino inválido.");
        }
        String cep = destino.getCep().replaceAll("\\D", "");
        if (cep.length() != 8) {
            throw new IllegalArgumentException("CEP inválido: " + destino.getCep());
        }

        int regiao = Character.getNumericValue(cep.charAt(0));
        BigDecimal base;
        int dias;
        if (regiao == 7) {                 
            base = new BigDecimal("15.00"); dias = 3;
        } else if (regiao <= 3) {          
            base = new BigDecimal("25.00"); dias = 5;
        } else if (regiao >= 8) {          
            base = new BigDecimal("30.00"); dias = 6;
        } else {                           
            base = new BigDecimal("35.00"); dias = 8;
        }

        boolean gratis = subtotal != null && subtotal.compareTo(LIMITE_FRETE_GRATIS) >= 0;
        this.valorFrete = gratis ? BigDecimal.ZERO.setScale(2) : base.setScale(2, RoundingMode.HALF_UP);
        this.prazoEntrega = dias + " dias úteis";
        return this.valorFrete;
    }

    public BigDecimal getValorFrete() { return valorFrete; }

    public int getIdFrete() { return idFrete; }
    public String getTransportadora() { return transportadora; }
    public void setTransportadora(String transportadora) { this.transportadora = transportadora; }
    public String getPrazoEntrega() { return prazoEntrega; }
}

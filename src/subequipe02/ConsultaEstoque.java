package subequipe02;

/** Contrato simples para saber se um produto pode ser exibido como disponível. */
public interface ConsultaEstoque {
    boolean estaDisponivel(String idProduto);
}

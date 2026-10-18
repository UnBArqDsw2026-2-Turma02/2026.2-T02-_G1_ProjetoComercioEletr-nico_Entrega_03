package subequipe02;

import java.math.BigDecimal;
import java.util.List;

/** Verifica o comportamento visível do padrão sem dependências de teste externas. */
public final class AdapterCheck {
    private AdapterCheck() {
    }

    public static void main(String[] args) {
        BuscaProdutos busca = new BuscaProdutos(new CatalogoAdapter(new CatalogoExternoSimulado()));

        List<Produto> bolas = busca.pesquisar("Bola");
        verificar(bolas.size() == 2, "a busca por Bola deve retornar dois produtos");
        verificar("BOLA-01".equals(bolas.get(0).getId()), "o primeiro codigo foi perdido");
        verificar("Bola de futebol".equals(bolas.get(0).getNome()), "a descricao nao foi adaptada");
        verificar(new BigDecimal("129.90").equals(bolas.get(0).getPrecoAtual()),
                "o valor em centavos nao foi convertido corretamente");
        verificar("BOLA-02".equals(bolas.get(1).getId()), "a ordem dos resultados mudou");
        verificar(new BigDecimal("89.90").equals(bolas.get(1).getPrecoAtual()),
                "o segundo preco foi convertido incorretamente");

        verificar(busca.pesquisar("sem-correspondencia").isEmpty(),
                "a busca sem resultado deve devolver lista vazia");
        verificar(busca.pesquisar("bOlA").size() == 2,
                "o catalogo simulado deve ignorar maiusculas e minusculas");

        System.out.println("Verificacoes do Adapter: OK");
    }

    private static void verificar(boolean condicao, String mensagem) {
        if (!condicao) {
            throw new AssertionError(mensagem);
        }
    }
}

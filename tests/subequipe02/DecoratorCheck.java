package subequipe02;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/** Verificações do Decorator de disponibilidade, sem dependências externas. */
public final class DecoratorCheck {
    private DecoratorCheck() {
    }

    public static void main(String[] args) {
        verificarFiltraIndisponivel();
        verificarMantemDisponivelSemAlterarDados();
        verificarPreservaOrdem();
        verificarTodosIndisponiveis();
        verificarBuscaSemResultado();
        verificarRepasseDoTermo();
        verificarCatalogoOriginalInalterado();
        verificarProdutoSemRegistroDeEstoque();
        verificarDependenciasNulas();

        System.out.println("Todos os 9 cenarios do Decorator passaram.");
    }

    private static CatalogoProdutos adaptado() {
        return new CatalogoAdapter(new CatalogoExternoSimulado());
    }

    private static CatalogoProdutos decorado() {
        return new CatalogoComDisponibilidade(adaptado(), new EstoqueSimulado());
    }

    private static void verificarFiltraIndisponivel() {
        List<Produto> bolas = decorado().buscarProdutos("Bola");
        verificar(bolas.size() == 1, "BOLA-02 esta sem estoque e deveria ser removida");
        verificar("BOLA-01".equals(bolas.get(0).getId()), "deveria restar apenas BOLA-01");
        aprovado("remove produto sem estoque");
    }

    private static void verificarMantemDisponivelSemAlterarDados() {
        Produto sapato = decorado().buscarProdutos("Sapato").get(0);
        verificar("SAPATO-01".equals(sapato.getId()), "id alterado pelo Decorator");
        verificar("Sapato de corrida".equals(sapato.getNome()), "nome alterado pelo Decorator");
        verificar(new BigDecimal("249.90").equals(sapato.getPrecoAtual()),
                "preco alterado pelo Decorator");
        aprovado("mantem produto disponivel com os mesmos dados");
    }

    private static void verificarPreservaOrdem() {
        CatalogoProdutos fixo = termo -> List.of(
                produto("C"), produto("A"), produto("X"), produto("B"));
        ConsultaEstoque semX = id -> !"X".equals(id);
        List<Produto> resultado = new CatalogoComDisponibilidade(fixo, semX).buscarProdutos("qualquer");
        verificar(ids(resultado).equals(List.of("C", "A", "B")),
                "a ordem dos produtos disponiveis mudou");
        aprovado("preserva a ordem dos disponiveis");
    }

    private static void verificarTodosIndisponiveis() {
        ConsultaEstoque nenhum = id -> false;
        List<Produto> resultado = new CatalogoComDisponibilidade(adaptado(), nenhum).buscarProdutos("");
        verificar(resultado.isEmpty(), "sem estoque, a busca deve devolver lista vazia");
        aprovado("todos indisponiveis devolve lista vazia");
    }

    private static void verificarBuscaSemResultado() {
        verificar(decorado().buscarProdutos("sem-correspondencia").isEmpty(),
                "busca sem correspondencia deve devolver lista vazia");
        aprovado("busca sem resultado continua vazia");
    }

    private static void verificarRepasseDoTermo() {
        List<String> termosRecebidos = new ArrayList<>();
        CatalogoProdutos espiao = termo -> {
            termosRecebidos.add(termo);
            return List.of();
        };
        new CatalogoComDisponibilidade(espiao, id -> true).buscarProdutos("Bola de futebol");
        verificar(termosRecebidos.equals(List.of("Bola de futebol")),
                "o termo deve ser repassado sem alteracao, uma unica vez");
        aprovado("repassa o termo ao catalogo envolvido");
    }

    private static void verificarCatalogoOriginalInalterado() {
        CatalogoProdutos original = adaptado();
        new CatalogoComDisponibilidade(original, new EstoqueSimulado()).buscarProdutos("Bola");
        verificar(original.buscarProdutos("Bola").size() == 2,
                "o catalogo sem Decorator deve continuar retornando os dois produtos");
        aprovado("catalogo sem Decorator continua igual");
    }

    private static void verificarProdutoSemRegistroDeEstoque() {
        verificar(!new EstoqueSimulado().estaDisponivel("INEXISTENTE-01"),
                "produto sem registro de estoque deve ser tratado como indisponivel");
        aprovado("produto sem registro e indisponivel");
    }

    private static void verificarDependenciasNulas() {
        esperarNullPointer(() -> new CatalogoComDisponibilidade(null, new EstoqueSimulado()),
                "deve rejeitar catalogo nulo");
        esperarNullPointer(() -> new CatalogoComDisponibilidade(adaptado(), null),
                "deve rejeitar estoque nulo");
        aprovado("rejeita dependencias nulas");
    }

    private static Produto produto(String id) {
        return new Produto(id, "Produto " + id, new BigDecimal("1.00"));
    }

    private static List<String> ids(List<Produto> produtos) {
        return produtos.stream().map(Produto::getId).toList();
    }

    private static void esperarNullPointer(Runnable acao, String mensagem) {
        try {
            acao.run();
        } catch (NullPointerException esperada) {
            return;
        }
        throw new AssertionError(mensagem);
    }

    private static void verificar(boolean condicao, String mensagem) {
        if (!condicao) {
            throw new AssertionError(mensagem);
        }
    }

    private static void aprovado(String cenario) {
        System.out.println("OK - " + cenario);
    }
}

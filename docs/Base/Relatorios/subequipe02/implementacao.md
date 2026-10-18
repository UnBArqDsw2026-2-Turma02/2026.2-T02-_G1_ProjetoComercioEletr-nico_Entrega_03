# Implementação Java — Adapter de Catálogo

## 1. Código e organização

A implementação do [modelo UML](modelagem.md) está em `src/subequipe02`, no pacote Java `subequipe02`. Ela usa somente a biblioteca padrão do Java 17 ou superior. O catálogo é **simulado** e contém dados fictícios; não consulta a loja estudada nem qualquer serviço externo.

[Código da Subequipe 02 na branch de trabalho](https://github.com/UnBArqDsw2026-2-Turma02/2026.2-T02-_G1_ProjetoComercioEletr-nico_Entrega_03/tree/subequipe02/src/subequipe02) — o link passará a mostrar estes arquivos quando os commits locais forem publicados no fluxo de pull request da subequipe. Até lá, os arquivos estão disponíveis no checkout local.

| Papel no padrão | Arquivo | O que faz |
| --- | --- | --- |
| **Target** | `CatalogoProdutos.java` | Declara `buscarProdutos(String): List<Produto>`, o contrato esperado pela busca. |
| **Client** | `BuscaProdutos.java` | Recebe `CatalogoProdutos` no construtor e chama esse contrato em `pesquisar`. |
| **Adaptee** | `CatalogoExternoSimulado.java` | Oferece `consultarItens(String): List<ItemExterno>` e seleciona dados fictícios pela descrição. |
| **Adapter** | `CatalogoAdapter.java` | Implementa `CatalogoProdutos`, recebe o Adaptee e converte chamada e resultados. |
| Dados | `ItemExterno.java` e `Produto.java` | Representam, respectivamente, o formato do fornecedor simulado e o formato esperado pelo cliente. |
| Demonstração | `Main.java` | Monta os objetos e mostra uma busca com resultados e outra sem resultados. |

## 2. Tradução realizada pelo Adapter

O **Target**, `CatalogoProdutos`, declara a operação e o tipo de retorno que a busca espera:

```java
public interface CatalogoProdutos {
    List<Produto> buscarProdutos(String termo);
}
```

O **Client**, `BuscaProdutos`, recebe esse contrato no construtor e encaminha a pesquisa por ele. A classe não conhece `CatalogoExternoSimulado`, `consultarItens` ou `ItemExterno`.

```java
private final CatalogoProdutos catalogo;

public List<Produto> pesquisar(String termo) {
    return catalogo.buscarProdutos(termo);
}
```

O **Adaptee**, `CatalogoExternoSimulado`, disponibiliza uma operação e um formato de saída diferentes. Ele seleciona os itens fictícios pela descrição; a lista vazia resulta naturalmente quando nenhum item passa pelo filtro.

```java
public List<ItemExterno> consultarItens(String termo) {
    String trecho = Objects.requireNonNull(termo, "termo").toLowerCase(Locale.ROOT);
    return ITENS.stream()
            .filter(item -> item.getDescricao().toLowerCase(Locale.ROOT).contains(trecho))
            .toList();
}
```

Em `Main`, a montagem fornece o **Adapter** ao cliente por meio do Target:

```java
CatalogoExternoSimulado fornecedor = new CatalogoExternoSimulado();
CatalogoProdutos catalogo = new CatalogoAdapter(fornecedor);
BuscaProdutos busca = new BuscaProdutos(catalogo);
```

O **Adapter**, `CatalogoAdapter`, declara `implements CatalogoProdutos` e guarda a instância de `CatalogoExternoSimulado` recebida no construtor. Em `buscarProdutos`, ele traduz a chamada para `consultarItens(termo)` e cria um `Produto` para cada `ItemExterno` retornado:

```java
return catalogoExterno.consultarItens(termo).stream()
        .map(item -> new Produto(
                item.getCodigo(),
                item.getDescricao(),
                BigDecimal.valueOf(item.getValorEmCentavos(), 2)))
        .toList();
```

| Origem simulada | Destino da busca | Conversão |
| --- | --- | --- |
| `codigo: String` | `id: String` | Mesmo texto. |
| `descricao: String` | `nome: String` | Mesmo texto. |
| `valorEmCentavos: long` | `precoAtual: BigDecimal` | `BigDecimal.valueOf(valorEmCentavos, 2)`. |

O `CatalogoExternoSimulado` filtra os itens pela descrição, sem diferenciar maiúsculas e minúsculas. A lista retornada é vazia quando não há correspondência. O adaptador não refaz a busca: apenas traduz cada item, mantendo a quantidade e a ordem dos resultados. `Produto` representa somente os três atributos necessários ao exercício; o modelo da Entrega 02 é mais amplo.

O valor é convertido diretamente de centavos inteiros para `BigDecimal` com escala 2. Isso preserva `12990 → 129.90` sem cálculo intermediário em `float` ou `double`. A formatação **R$ 129,90** ocorre somente em `Main`, para exibição; não altera o valor armazenado no objeto.

## 3. Relação entre modelo e código

| Decisão do UML | Evidência na implementação |
| --- | --- |
| `CatalogoAdapter` realiza `CatalogoProdutos`. | A classe declara `implements CatalogoProdutos` e implementa `buscarProdutos`. |
| `CatalogoAdapter` envolve um `CatalogoExternoSimulado`. | O construtor recebe a instância e a guarda em um campo privado. |
| `BuscaProdutos` depende do Target. | Seu construtor recebe `CatalogoProdutos`, não o tipo do catálogo simulado. |
| Os formatos de entrada e saída diferem. | `consultarItens` devolve `ItemExterno`; `buscarProdutos` devolve `Produto`. |
| O preço representa centavos sem perda decimal. | A conversão usa `BigDecimal.valueOf(long, 2)`. |

Os construtores e métodos de acesso de `Produto` e `ItemExterno` foram omitidos da figura UML para manter o foco no padrão, como indicado na legenda. Eles existem no código para construir e ler os objetos.

## 4. Verificação e limites

`tests/subequipe02/AdapterCheck.java` exercita o fluxo completo `BuscaProdutos → CatalogoProdutos → CatalogoAdapter → CatalogoExternoSimulado`, conferindo os campos convertidos, os valores monetários, a ordem dos resultados, a busca sem correspondência e a busca sem diferenciar maiúsculas e minúsculas. As [instruções e a saída observada](execucao.md) estão na página de execução.

Esta implementação não inclui estoque, carrinho, checkout, APIs externas ou uma interface gráfica. A revisão do código pelos demais integrantes e o registro de suas contribuições individuais permanecem pendentes.

## 5. Histórico de versões

| Versão | Data | Descrição | Revisão |
| --- | --- | --- | --- |
| 1.0 | 18/10/2026 | Implementação Java do Adapter e documentação da correspondência entre modelo e código, com apoio de IA. | Pendente de revisão pela subequipe. |
| 1.1 | 18/10/2026 | Revisão dos trechos de código dos quatro participantes do padrão e da ligação entre eles. | Pendente de revisão pela subequipe. |

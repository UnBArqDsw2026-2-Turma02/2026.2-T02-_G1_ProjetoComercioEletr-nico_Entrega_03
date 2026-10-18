# GoF Estrutural — Adapter

## 1. Escopo e origem do problema

A Subequipe 02 trabalha com o **Fluxo de Navegação B: Busca de Produtos, Carrinho e Compra** do projeto G1_ProjetoComercioEletronico. Na Entrega 01, a engenharia reversa descreveu a pesquisa, a visualização de produtos e a continuação da compra. Na Entrega 02, o diagrama de classes incluiu `Produto` com os atributos `id`, `nome` e `precoAtual`; o diagrama de componentes apresentou **Busca e Catálogo**; e o diagrama de sequência representou a consulta ao Catálogo durante a pesquisa. Esses artefatos fundamentam o domínio e o recorte de **busca de produtos** desta proposta.

As entregas anteriores foram construídas a partir de comportamento observável. Elas **não identificaram a interface de um catálogo externo ou legado**, nem tiveram acesso ao código-fonte ou às APIs internas da loja estudada. A incompatibilidade de interfaces descrita a seguir é um **cenário hipotético, simulado pela subequipe para demonstrar o padrão Adapter**. Não se afirma que ela exista na arquitetura real da fonte de inspiração.

## 2. Contexto e problema de projeto

Considere uma aplicação de busca que espera um catálogo com a operação `buscarProdutos(termo)` e recebe uma representação simplificada de `Produto` com `id`, `nome` e `precoAtual`. Para o exercício, a equipe criará um **catálogo externo simulado** que oferece `consultarItens(termo)` e devolve registros com `codigo`, `descricao` e `valorEmCentavos`. Na simulação, esse contrato representa um fornecedor preexistente: a aplicação cliente não pode alterar sua operação nem o formato dos registros. Essa restrição faz parte da hipótese didática, não de uma descoberta sobre a loja analisada.

| Informação | Formato do catálogo simulado | Formato esperado pela busca |
| --- | --- | --- |
| Operação | `consultarItens(termo)` | `buscarProdutos(termo)` |
| Identificador | `codigo` | `id` |
| Nome | `descricao` | `nome` |
| Valor | `valorEmCentavos` (inteiro) | `precoAtual` (valor decimal) |

Por exemplo, um item com `codigo = "BOLA-01"`, `descricao = "Bola de futebol"` e `valorEmCentavos = 12990` deve ser apresentado à busca com `id = "BOLA-01"`, `nome = "Bola de futebol"` e `precoAtual` equivalente a `BigDecimal.valueOf(12990L, 2)`, exibido como **R$ 129,90**. Essa representação usa somente os atributos necessários à demonstração; não pretende reproduzir todas as propriedades e associações do `Produto` modelado na Entrega 02.

Sem uma camada de adaptação, `BuscaProdutos` teria de chamar uma operação que não pertence ao contrato esperado e conhecer os campos específicos do catálogo simulado. Essa tradução dentro da própria busca aumentaria seu acoplamento ao fornecedor e misturaria a regra de pesquisa com detalhes de integração.

## 3. Escolha e solução proposta

O **Adapter** é um padrão GoF estrutural adequado quando um cliente precisa usar um objeto cuja interface é incompatível com a interface que ele espera, conforme descrito pelos [autores do padrão](https://www.informit.com/articles/article.aspx?p=1398600). Nesta proposta, `CatalogoAdapter` implementará o contrato `CatalogoProdutos`, receberá uma instância de `CatalogoExternoSimulado` e concentrará a tradução da chamada e dos dados retornados. Trata-se da variante **Adapter de objeto**, pois o adaptador envolve uma instância do catálogo simulado.

| Papel no padrão | Elemento proposto | Responsabilidade |
| --- | --- | --- |
| **Client** | `BuscaProdutos` | Pesquisar produtos por meio do contrato esperado, sem conhecer o catálogo simulado. |
| **Target** | `CatalogoProdutos` | Definir `buscarProdutos(termo)` como operação disponível para a busca. |
| **Adapter** | `CatalogoAdapter` | Implementar `CatalogoProdutos`, chamar `consultarItens(termo)` e converter cada registro retornado. |
| **Adaptee** | `CatalogoExternoSimulado` | Disponibilizar `consultarItens(termo)` e itens no formato incompatível. |

O `CatalogoProdutos` é um **contrato interno novo** da demonstração. Ele não substitui a `IBuscaProdutos` que o componente **Busca e Catálogo** expõe no diagrama de componentes da Entrega 02. Essa distinção deverá aparecer na explicação do UML, para que os dois níveis de interface não sejam confundidos.

A implementação planejada será em **Java**, para tornar explícitos o contrato, as classes e os tipos representados no UML. As relações estão detalhadas na [página de Modelagem UML](modelagem.md), antes de serem implementadas. Para o valor monetário, a conversão de centavos para `BigDecimal` deverá preservar duas casas decimais sem utilizar `float` ou `double` no cálculo.

## 4. Método de análise, justificativa e limites

Para formular a proposta, foram retomados o processo de busca da Entrega 01 e os elementos `Produto`, **Busca e Catálogo** e Catálogo da Entrega 02. Em seguida, foi definida a hipótese de um contrato externo fixo e comparada a tradução dentro de `BuscaProdutos` com sua concentração em um adaptador. A decisão foi conferida com a finalidade do padrão descrita na literatura. A revisão pelos integrantes da subequipe e o registro das contribuições individuais ainda estão pendentes.

O problema escolhido é a **incompatibilidade entre duas interfaces de catálogo**: nomes de operações diferentes e estruturas de dados diferentes. O Adapter mantém essa conversão em um único componente e permite que `BuscaProdutos` dependa do contrato `CatalogoProdutos`. Se o formato do fornecedor mudar, a busca poderá conservar seu contrato; o catálogo simulado e o adaptador poderão exigir ajustes. A solução também permite substituir o catálogo por outra implementação do mesmo contrato sem reescrever a busca.

| Situação | Dependência de `BuscaProdutos` | Onde ocorre a tradução |
| --- | --- | --- |
| Sem Adapter | Contrato particular do catálogo simulado. | Na própria busca. |
| Com Adapter | Contrato `CatalogoProdutos`. | Em `CatalogoAdapter`. |

O padrão tem custo: acrescenta uma interface e uma classe, além de exigir manutenção quando o formato adaptado mudar. Para um único catálogo que já oferecesse exatamente o contrato esperado, essa estrutura não se justificaria. Neste exercício, a diferença de operações e dados foi criada deliberadamente para demonstrar o caso de uso do Adapter; ela deve continuar identificada como **simulação acadêmica**, não como achado de engenharia reversa.

O recorte desta demonstração termina na obtenção de resultados de busca. A validação de estoque representada na sequência da Entrega 02, assim como carrinho, checkout, pagamento e integração com serviços reais, fica fora deste exemplo do Adapter. A documentação deverá comparar o problema, o modelo UML e o comportamento do código Java usando o mesmo cenário.

## 5. Rastreabilidade com as entregas anteriores

| Origem | Evidência aproveitada | Uso nesta entrega |
| --- | --- | --- |
| Entrega 01 — Engenharia Reversa e BPMN | Pesquisa, seleção de produtos e continuação do fluxo de compra. | Delimita a funcionalidade de busca dentro do Fluxo B. |
| Entrega 02 — Modelo Estático | `Produto` com `id`, `nome` e `precoAtual`. | Orienta os nomes dos dados entregues à busca; a demonstração usa um subconjunto do modelo. |
| Entrega 02 — Modelos de Componentes e Sequência | **Busca e Catálogo**, sua interface exposta `IBuscaProdutos` e a consulta ao Catálogo durante a pesquisa. | Situa a busca no fluxo anterior; `CatalogoProdutos` é um contrato interno novo desta proposta. |

## 6. Referências

- GAMMA, Erich; HELM, Richard; JOHNSON, Ralph; VLISSIDES, John. **Design Patterns: Elements of Reusable Object-Oriented Software**. Addison-Wesley, 1994.
- GAMMA, Erich et al. [Design Patterns: Adapter](https://www.informit.com/articles/article.aspx?p=1398600). Trecho do livro publicado pela InformIT, 2009.
- ORACLE. [Documentação de `BigDecimal.valueOf(long, int)`](https://docs.oracle.com/en/java/javase/15/docs/api/java.base/java/math/BigDecimal.html#valueOf(long,int)).
- SUBEQUIPE 02. [Entrega 01 — Engenharia Reversa e BPMN](https://github.com/UnBArqDsw2026-2-Turma02/2026.2-T02-_G1_ProjetoComercioEletronico_Entrega_01/blob/main/docs/Base/subequipe2/foco2_SubEquipe_02.md).
- SUBEQUIPE 02. [Entrega 02 — Modelo Estático](https://github.com/UnBArqDsw2026-2-Turma02/2026.2-T02-_G1_ProjetoComercioEletr-nico_Entrega02/blob/main/docs/modulo-2/subequipe-02/foco-01.md).
- SUBEQUIPE 02. [Entrega 02 — Modelos Dinâmicos](https://github.com/UnBArqDsw2026-2-Turma02/2026.2-T02-_G1_ProjetoComercioEletr-nico_Entrega02/blob/main/docs/modulo-2/subequipe-02/foco-02.md).

## 7. Histórico de versões

| Versão | Data | Descrição | Revisão |
| --- | --- | --- | --- |
| 1.0 | 14/10/2026 | [Proposta inicial do Adapter](https://github.com/UnBArqDsw2026-2-Turma02/2026.2-T02-_G1_ProjetoComercioEletr-nico_Entrega_03/commit/0806017026430903b4f95213d20bd0c1eff8e1c6) para a busca de produtos. | Não registrada. |
| 1.1 | 18/10/2026 | Revisão do problema e da justificativa do Adapter, com rastreabilidade às Entregas 01 e 02 e definição de Java para a implementação. Texto elaborado com apoio de IA. | Pendente de revisão pela subequipe. |
| 1.2 | 18/10/2026 | Revisão técnica das premissas, distinção entre interfaces, representação monetária e método de análise, com apoio de IA. | Pendente de revisão pela subequipe. |
| 1.3 | 18/10/2026 | Ligação com a página de Modelagem UML da etapa 02. | Pendente de revisão pela subequipe. |

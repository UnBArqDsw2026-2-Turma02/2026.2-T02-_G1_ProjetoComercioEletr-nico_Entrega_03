# GoF Estrutural — Adapter

## 1. Introdução

Os padrões de projeto estruturais apresentam soluções para organizar classes e objetos, estabelecendo relações entre eles para compor estruturas maiores. Neste trabalho, a Subequipe 02 propõe a aplicação do padrão Adapter no contexto da busca de produtos do projeto G1_ProjetoComercioEletronico, inspirado nas funcionalidades observadas no site da Decathlon.

## 2. Contexto e problema

Uma busca de produtos pode precisar consultar catálogos que possuem interfaces e formatos de dados diferentes dos utilizados pela aplicação.

Para demonstrar esse problema, propomos um catálogo simulado que oferece a operação `consultarItens(termo)` e retorna produtos com os campos `descricao` e `valorEmCentavos`. Entretanto, a busca da aplicação espera a operação `buscarProdutos(termo)` e produtos com os campos `nome` e `preco`.

Essa incompatibilidade impede que o catálogo seja utilizado diretamente pela busca.

## 3. Solução proposta

O padrão Adapter permite converter a interface de uma classe em outra esperada pelo cliente.

Na proposta, a classe `CatalogoAdapter` será responsável por traduzir a chamada da busca para a operação disponível no catálogo e adaptar os dados retornados, incluindo a conversão do valor em centavos para reais.

O catálogo será simulado para fins acadêmicos, sem integração com uma API real da Decathlon.

## 4. Participantes do padrão

| Participante | Elemento proposto | Responsabilidade |
| --- | --- | --- |
| Client | BuscaProdutos | Solicitar produtos pela interface esperada. |
| Target | CatalogoProdutos | Definir a operação buscarProdutos(termo). |
| Adapter | CatalogoAdapter | Adaptar a chamada e os dados do catálogo. |
| Adaptee | CatalogoLegado | Disponibilizar consultarItens(termo), com formato incompatível. |

## 5. Justificativa da escolha

O Adapter foi proposto porque o problema envolve a integração de interfaces incompatíveis. Sua aplicação permite concentrar a tradução no adaptador, evitando que a busca precise conhecer os detalhes do catálogo.

Como limitação, a solução acrescenta uma classe à estrutura e exige manutenção caso a interface do catálogo seja alterada. Portanto, sua utilização se justifica pela incompatibilidade apresentada no exemplo.

## 6. Referências

GAMMA, Erich et al. Design Patterns: Elements of Reusable Object-Oriented Software. Addison-Wesley, 1994.
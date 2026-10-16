## Mapeamento do Fluxo

Com base na análise empírica de engenharia reversa realizada e documentada no relatório de inspeção do fluxo C, foi feito o mapeamento de arquitetura do sistema que compõe o fluxo analisado: Carrinho -> Checkout -> Pagamento. 

O sistema real opera sob uma arquitetura orientada a serviços e microsserviços (plataforma VTEX), na qual o front-end se comunica assincronamente com múltiplos serviços especialistas de back-end e provedores terceiros. 

O modelo completo do sistema engloba três camadas principais de responsabilidade: 

* **Camada de Apresentação e Gestão de Estado (Front-End):** Responsável pela interface do usuário e manutenção do estado da sessão durante a navegação sem exigir recarregamentos completos da página.
* **Camada de Negócio e Serviços Core (Plataforma VTEX):** Conjunto de microsserviços do e-commerce responsáveis pela lógica de negócio e persistência das etapas da compra (serviços de catálogo, disponibilidade de estoque, carrinho, pedido, logística, entre outros).
* **Camada de Serviços Transversais e Integrações Terceiras:** Serviços que atuam de forma paralela ao fluxo principal para enriquecer a experiência, garantir a segurança financeira e coletar dados operacionais (motor de recomendações, IA, camadas de segurança, antifraude, risco, monitoramento).

*(modelagem proposta pela IA)*  
O diagrama acima reflete todas as classes e dependências encontradas durante a engenharia reversa. 

* **Classes de Domínio (`Carrinho`, `ItemCarrinho`, `Cliente`, `Pedido`):** Tratam as regras puras da compra. 
* **Classes de Serviços Core (`LogisticsService`, `PaymentGatewayAPI`, `ViaCEPService`):** Fazem a ponte com as APIs de infraestrutura e e-commerce. 
* **Classes Transversais (`DynamicYieldAdapter`, `KondutoAntiFraude`, `GA4Tracker`, `SentryLogger`):** Representam os conectores com serviços terceiros de Inteligência Artificial, segurança comportamental e telemetria. 

---

## Abstração e Simplificação do Modelo

A análise realizada revelou um ecossistema real de alta complexidade, caracterizado por desacoplamento de microsserviços, telemetria comportamental em tempo real (antifraude), motores de recomendação alimentados por IA e monitoramento de desempenho (analytics e logging).  

No contexto do desenvolvimento de arquitetura de software acadêmica, optou-se por simplificar o modelo do sistema analisado, justificada pela redução da complexidade do sistema e pela independência dos padrões comportamentais em relação ao modelo completo, assegurando que o desenvolvimento do projeto se concentre nos pontos de variação e na lógica de interação entre as classes do domínio.  


#### Remoção de elementos fora do escopo
Para a construção do modelo simplificado, os seguintes componentes mapeados no relatório de inspeção foram intencionalmente desconsiderados:  

* **Serviços Transversais de IA e Recomendação (`DynamicYieldAdapter`):** Removidos por tratarem de personalização de vitrines e UX, sem impacto na regra de negócio core do checkout. 
* **Camada de Antifraude e Telemetria (`KondutoAntiFraude`, `ReCaptchaValidator`):** Removida por ser um serviço de segurança de infraestrutura delegado a terceiros. 
* **Sistemas de Monitoramento e Analytics (`GA4Tracker`, `SentryLogger`, `AmplitudeAnalytics`):** Removidos por não pertencerem à lógica do domínio da aplicação. 
* **Gerenciadores de Estado HTTP e Sincronização de APIs (`OrderForm`, `ViaCEPService`):** Substituídos por abstrações diretas de objetos em memória. 

---

## Modelagem do Sistema
O diagrama a seguir ilustra o escopo modelado do sistema, estruturado especificamente para a aplicação e validação dos padrões comportamentais propostos. 

![Imagem do diagrama de classes](../../../assets/images/DiagramaClassesFluxoC.png)

#### Dicionário de Classes
| Classe | Papel Principal no Domínio |
| :--- | :--- |
| **Carrinho** | Contêiner central da sessão de compras; gerencia itens, cálculos de subtotal e regras de frete grátis. |
| **ItemCarrinho** | Elemento intermediário que conecta o Carrinho a um Produto específico, controlando quantidade e subtotal. |
| **Produto** | Entidade de catálogo que armazena dados essenciais (SKU, título, preço e variações). |
| **Cliente** | Agrupa dados cadastrais e de identificação do usuário coletados no perfil. |
| **Endereço** | Agrupa as informações de entrega e geolocalização validadas pelo serviço de autocompletar. |
| **Frete** | Encapsula a modalidade de entrega escolhida (Econômica, Expressa, etc.) e o respectivo custo. |
| **Pagamento** | Centraliza a transação financeira (Cartão, PIX, etc.), integrando adquirentes e antifraude. |
| **Pedido** | Entidade agregadora central que consolida carrinho, cliente, frete e pagamento para fechamento (`orderForm`). |

#### Análise de Métodos e Semântica
| Classe | Métodos Declarados | Avaliação Técnica & Ajustes Recomendados |
| :--- | :--- | :--- |
| **Cliente** | `+getDados(): String` | Adequado para o escopo simplificado. Avaliar se futuramente precisará de `atualizarCadastro()`. |
| **Endereço** | `+getEndereço(): String` | Atende à formatação. **Atenção:** Evitar caracteres especiais (`ç`) no nome do método para prevenir erros de compilação. |
| **Pagamento** | `+processar(): boolean`<br>`+cancelarPagamento(): void` | Perfeitos. Alinhados diretamente com o padrão *Strategy* para alternância de meios de pagamento. |
| **Frete** | `+calcular(): BigDecimal` | Correto. Reflete a lógica de simulação logística e serve de ponto de variação para o *Strategy*. |
| **Pedido** | `+finalizar(): void`<br>`+calcularTotal(): Double` | **Ajustes:** Corrigir a sintaxe de `+calcularTotal(): Double`. **Sugestão:** Adicionar método de transição de estado (`mudarEstado()`) para suportar o padrão *State*. |
| **Carrinho** | `+adicionarProduto(): void`<br>`+removerProduto(): void`<br>`+calcularSubtotal(): Decimal`<br>`+aplicarCupom(codigo): void` | Completos. **Ajuste:** Padronizar o tipo de retorno de `Decimal` para `BigDecimal`. |
| **ItemCarrinho** | `+calcularSubtotal(): BigDecimal` | Correto. Multiplica a quantidade pelo preço unitário do item. |
| **Produto** | `+getPreco(): BigDecimal` | Adequado para exposição do preço unitário base. |


#### Mapeamento de Relacionamentos
| Relação | Tipo / Multiplicidade | Análise Semântica |
| :--- | :--- | :--- |
| **Cliente ↔ Pedido** | `(1) ──── (0..*)`<br>*realiza / originado de* | Um cliente pode realizar múltiplos pedidos ao longo do tempo; todo pedido pertence obrigatoriamente a um único cliente. |
| **Cliente ↔ Endereço** | `(1) ──── (0..*)`<br>*possui* | Um cliente pode salvar múltiplos endereços de entrega em seu perfil. |
| **Pedido ↔ Endereço** | `(1) ──── (1)`<br>*entrega em* | Cada pedido finalizado vincula-se estritamente a um endereço de entrega. |
| **Pedido ↔ Pagamento** | `(1) ──── (1)`<br>*possui* | Cada pedido fechado está associado a uma única transação de pagamento correspondente. |
| **Pedido ↔ Frete** | `(1) ──── (1)`<br>*possui* | O pedido consolida a modalidade e o custo logístico escolhidos pelo usuário. |
| **Pedido ↔ Carrinho** | `(0..1) ──── (1)`<br>*utiliza* | O pedido consome os dados de um carrinho ativo (que pode ser esvaziado após a finalização). |
| **Carrinho ↔ ItemCarrinho** | `(1) ──── (0..*)`<br>*contém* | Um carrinho agrupa vários itens selecionados (ou nenhum, se estiver vazio). |
| **ItemCarrinho ↔ Produto** | `(*) ──── (1)`<br>*referencia* | Vários itens de carrinho podem apontar para o mesmo produto do catálogo (variando apenas as quantidades). |

## Pontos de Variação e Aplicação de Padrões GoF
Para atender aos requisitos de flexibilidade e extensibilidade do fluxo de transações no e-commerce, adotou-se a aplicação do padrão de projeto comportamental *Strategy* no módulo de pagamento, justificada pela necessidade de suportar diferentes modalidades de liquidação financeira (como Cartão de Crédito com regras de parcelamento via BIN e PIX com geração dinâmica de QR Code) de forma totalmente desacoplada da entidade central do pedido. 

Sem a aplicação de um padrão de projeto, a classe responsável pelo pagamento tenderia a acumular estruturas condicionais complexas (como múltiplos blocos `if/else` ou `switch/case`) para tratar as regras específicas de cada adquirente ou forma de submissão. Isso violaria o princípio aberto/fechado (*Open/Closed Principle - OCP*), tornando o sistema rígido a manutenções e propenso a falhas na inclusão de novos meios de pagamento (como carteiras digitais ou boletos).  

O padrão *Strategy* resolve essa problemática ao encapsular cada família de algoritmos em classes independentes, permitindo que eles sejam trocados em tempo de execução sem impactar o cliente (GAMMA et al., 1994).

---

## Padrão *Strategy*
Com o objetivo de alcançar alta coesão e baixo acoplamento — permitindo que novas formas de pagamento sejam integradas à arquitetura de forma modular —, a modelagem do sistema foi estruturada sob os pilares fundamentais do padrão *Strategy*, conforme formalizado na literatura de padrões de projeto (GAMMA et al., 1994): 

* **Interface Estratégia (`FormaPagamento`):** Define o contrato comportamental comum que todas as modalidades de pagamento devem implementar, estipulando o método central de execução da transação (`processarPagamento()`).
* **Estratégias Concretas (`PagamentoPix`, `PagamentoCartaoCredito`, `PagamentoBoleto`):** Implementam as regras de negócio particulares e os atributos específicos exigidos por每个 modalidade financeira, isolando as complexidades de validação em classes independentes.
* **Contexto (`Pedido`):** Mantém uma referência polimórfica para a estratégia ativa escolhida pelo cliente durante o *checkout*, delegando a execução da transação para o objeto concreto correspondente sem que a classe conheça os detalhes internos de cada meio de pagamento.

![Imagem do diagrama com padrão Gof Comportamental Strategy:](../../../assets/images/GofStrategyFluxoC.png)

### Detalhamento das Modificações do Padrão Strategy

#### 1. Modificações em Classes

#### 2. Modificações em Atributos

#### 3. Modificações em Métodos

#### 4. Modificações em Ligações e Relacionamentos

---

#### Referências Bibliográficas

**GAMMA, Erich et al.** *Design Patterns: Elements of Reusable Object-Oriented Software*. Boston: Addison-Wesley, 1994.

---

> **Histórico de Versões**
> 
> | Versão | Data | Descrição | Autores | Revisor |
> | :---: | :---: | :--- | :--- | :---: |
> | `0.1` | 14/10/2026 | Criação e Estruturação da página | [Rafaela Andrea](https://github.com/radamesGuerra) | - |
> | `0.2` | 15/10/2026 | Elaboração de parte do texto | [Leticia Santos](https://github.com/LeticiaSantosss) | - |
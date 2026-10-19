# IA Generativa & Lições Aprendidas

## 1. Visão Geral do Foco
Esta página consolida as reflexões individuais, lições aprendidas e a análise crítica sobre o uso de Inteligência Artificial Generativa durante todo o processo de modelagem e documentação da Subequipe 03.

---

## 2. Quadro Integrado de Contribuições Individuais

> **Entrega Mínima Obrigatória:** Todos os membros da subequipe registraram individualmente seus pontos de vista com base no embasamento teórico e nas práticas vivenciadas.

| Nome do Membro | Lições Aprendidas | Uso da IA Generativa (Senso Crítico & Embasamento) |
| :--- | :--- | :--- |
| **Rafaela Andrea** | • **Arquitetura Orientada a Padrões de Projeto:** Compreensão prática e teórica sobre como abstrair problemas recorrentes de software, identificando os *trade-offs* e a relevância de aplicar Padrões Comportamentais para promover baixo acoplamento e alta coesão.<br> • **Do Fluxo Operacional à Modelagem Orientada a Objetos:** Habilidade de traduzir relatórios de inspeção e fluxos de dados em diagramas de classes consistentes, garantindo o mapeamento de atributos, métodos, visibilidades e relacionamentos.<br> • **Alinhamento Entre Modelagem e Código Executável:** Aprendizado no refinamento iterativo entre o modelo conceitual UML e a implementação real, garantindo que o código reflita a estrutura projetada.<br> • **Desenvolvimento de Main Interativa=:** Capacidade de estruturar uma interface/ponto de entrada interativo para demonstrar o funcionamento dinâmico do sistema aplicado em cenários práticos.<br> • **Produtividade na Gestão de Documentação Técnica:** Aprimoramento da capacidade de estruturar páginas de documentação técnica de forma ágil e padronizada, mantendo a clareza e o rigor arquitetural do projeto. | • A IA Generativa atuou como um **agente de revisão sintática, arquitetural e de validação contínua**, acelerando o ciclo de modelagem, codificação e documentação sem substituir o senso crítico do desenvolvedor.<br> • **Modelagem Inicial e Diagramação UML:** Utilização da IA para extrair a modelagem conceitual preliminar do sistema completo a partir do relatório do fluxo de inspeção, seguida de auditoria criteriosa dos parâmetros dos diagramas (atributos, tipos, assinaturas de métodos e multiplicidade de relacionamentos).<br> • **Análise Comparativa de Padrões de Projeto:** Consulta à IA para simular e avaliar diferentes abordagens de padrões de projeto aplicáveis ao domínio, compreendendo os impactos de cada padrão na flexibilidade e sustentabilidade da arquitetura antes da tomada de decisão.<br> • **Suporte à Implementação e Code Review:** Utilização da IA na escrita e validação do código-fonte, aplicando testes de mesa e análise estática para assegurar que a implementação estivesse em estrita conformidade com o padrão selecionado.<br> • **Automação de Documentação e Interface Interativa:** Aceleração da escrita das páginas de documentação técnica e estruturação lógica de uma `main` interativa para validação prática dos fluxos do sistema. |
| **Letícia Santos** | Aprendi que o padrão Strategy simplifica o código ao isolar cada forma de pagamento (Pix, Boleto e Cartão de Crédito) atrás da interface `FormaPagamento`. Assim, novas formas de pagamento podem ser adicionadas sem alterar as classes que as utilizam, o que é coerente com a intenção do padrão no catálogo do GoF (Gamma et al., 1994). Na prática, percebi que a qualidade da solução não vem da quantidade de classes ou métodos, e sim da simplicidade arquitetural e da fidelidade ao diagrama de classes definido pelo grupo. Também aprendi que refatorar para código limpo em Java (nomes claros, responsabilidade única por classe, remoção de código redundante) torna a implementação mais fácil de manter e de validar com base no modelo. | Usei a IA em dois momentos. Na modelagem, para discutir e validar refinamentos pontuais no diagrama de classes. No desenvolvimento, para gerar trechos iniciais de código e tirar dúvidas de sintaxe. Em ambos os casos, o resultado passou por refatoração rigorosa antes de entrar no projeto. Minha análise crítica mostrou que a IA tendia a sugerir mais métodos e atributos do que o diagrama de classes previa. Removi o que não constava no modelo do grupo, para não sobrecarregar a responsabilidade das classes, e conferi o restante com o diagrama em vez de aceitar a saída da IA sem revisar. Validei a implementação do Strategy com base no catálogo do GoF (Gamma et al., 1994) e no material da disciplina. |

---

## 3. Prompts Relevantes & Validação Crítica

Para ilustrar a aplicação prática da IA Generativa no fluxo de trabalho do grupo:

- **Prompt Usado (Mapeamento de Evidências DevTools e Rastreabilidade):** 
  - **Resultado da IA:** 
  - **Decisão do Grupo:**

---

> **Histórico de Versões**
> 
> | Versão | Data | Descrição | Autores | 
> | :---: | :---: | :--- | :--- | 
> | `0.1` | 18/10/2026 | Criação e Estruturação da página do Foco 03 | [Rafaela Andrea](https://github.com/radamesGuerra) |
> | `0.2` | 18/10/2026 | atualiza documentação de IA Generativa e lições aprendidas | [Leticia Santos](https://github.com/LeticiaSantosss) |

# IA Generativa & Lições Aprendidas

## 1. Visão Geral do Foco
Esta página consolida as reflexões individuais, lições aprendidas e a análise crítica sobre o uso de Inteligência Artificial Generativa durante todo o processo de modelagem e documentação da Subequipe 03.

---

## 2. Quadro Integrado de Contribuições Individuais

> **Entrega Mínima Obrigatória:** Todos os membros da subequipe registraram individualmente seus pontos de vista com base no embasamento teórico e nas práticas vivenciadas.

| Nome do Membro | Lições Aprendidas | Uso da IA Generativa (Senso Crítico & Embasamento) |
| :--- | :--- | :--- |
| **Rafaela Andrea** | • **Arquitetura Orientada a Padrões de Projeto:** Compreensão prática e teórica sobre como abstrair problemas recorrentes de software, identificando os *trade-offs* e a relevância de aplicar Padrões Comportamentais para promover baixo acoplamento e alta coesão.<br> • **Do Fluxo Operacional à Modelagem Orientada a Objetos:** Habilidade de traduzir relatórios de inspeção e fluxos de dados em diagramas de classes consistentes, garantindo o mapeamento de atributos, métodos, visibilidades e relacionamentos.<br> • **Alinhamento Entre Modelagem e Código Executável:** Aprendizado no refinamento iterativo entre o modelo conceitual UML e a implementação real, garantindo que o código reflita a estrutura projetada.<br> • **Desenvolvimento de Main Interativa=:** Capacidade de estruturar uma interface/ponto de entrada interativo para demonstrar o funcionamento dinâmico do sistema aplicado em cenários práticos.<br> • **Produtividade na Gestão de Documentação Técnica:** Aprimoramento da capacidade de estruturar páginas de documentação técnica de forma ágil e padronizada, mantendo a clareza e o rigor arquitetural do projeto. | • A IA Generativa atuou como um **agente de revisão sintática, arquitetural e de validação contínua**, acelerando o ciclo de modelagem, codificação e documentação sem substituir o senso crítico do desenvolvedor.<br> • **Modelagem Inicial e Diagramação UML:** Utilização da IA para extrair a modelagem conceitual preliminar do sistema completo a partir do relatório do fluxo de inspeção, seguida de auditoria criteriosa dos parâmetros dos diagramas (atributos, tipos, assinaturas de métodos e multiplicidade de relacionamentos).<br> • **Análise Comparativa de Padrões de Projeto:** Consulta à IA para simular e avaliar diferentes abordagens de padrões de projeto aplicáveis ao domínio, compreendendo os impactos de cada padrão na flexibilidade e sustentabilidade da arquitetura antes da tomada de decisão.<br> • **Suporte à Implementação e Code Review:** Utilização da IA na escrita e validação do código-fonte, aplicando testes de mesa e análise estática para assegurar que a implementação estivesse em estrita conformidade com o padrão selecionado.<br> • **Automação de Documentação e Interface Interativa:** Aceleração da escrita das páginas de documentação técnica e estruturação lógica de uma `main` interativa para validação prática dos fluxos do sistema. |
| **Letícia Santos** | - Domínio na implementação prática do padrão GoF Strategy e na refatoração de código limpo em Java.<br>- Entendimento da importância de manter a simplicidade arquitetural  e a fidelidade ao diagrama de classes do projeto.<br>- Capacidade de avaliar criticamente sugestões e códigos gerados por IA. | - **Modelagem e Validação:** Utilização da IA para discutir e validar refinamentos pontuais no diagrama do padrão GoF comportamental.<br>- **Desenvolvimento e Refatoração:** Utilização da IA para geração inicial de trechos de código e consultas sintáticas, seguida de refatoração rigorosa para adequação ao projeto.<br>- **Análise Crítica e Filtragem:** Identificação de que a IA tendia a inflar o código com métodos redundantes e acoplamento desnecessário. Atuação ativa na remoção de sugestões inadequadas para garantir a aderência estrita ao modelo arquitetural do grupo. |
| **Camile Barbosa** | • **State:** aprendi que, em vez de espalhar `if` pelo `Pedido`, cada fase (criado, pago, enviado, entregue, cancelado) pode virar uma classe que sabe quais ações aceita. Isso bloqueia transições que não fazem sentido, como enviar um pedido ainda não pago.<br>• **Observer:** aprendi que o pedido pode avisar vários interessados (e-mail ao cliente, estoque e histórico) sem conhecê-los, e que dá para adicionar ou remover um observador sem mexer no `Pedido`.<br>• **Combinação de padrões:** vi State, Strategy e Observer atuando sobre o mesmo `Pedido`, cada um com uma responsabilidade: se a ação é permitida, como cobrar e quem é avisado.<br>• **Depuração e Git:** aprendi a confiar no compilador (`javac`) e não só nos sublinhados do editor, a ler as mensagens de erro (como a interface `PedidoObservavel` que eu tinha esquecido de criar) e a organizar o trabalho em branch, commits e pull request.<br>• **Retomada após afastamento:** voltar ao projeto depois de um período afastada exigiu primeiro entender o que já estava pronto (código, modelagem e relatório) antes de implementar a minha parte. | • **Apoio guiado à implementação:** a IA analisou o repositório, propôs State e Observer e me passou o código por etapas, explicando cada classe. Eu digitei, compilei e testei cada passo no meu computador.<br>• **Depuração:** usei a IA para interpretar mensagens do compilador, como o `implements` que faltava em uma classe e a interface que não tinha sido criada.<br>• **Documentação e diagramas:** a IA gerou o rascunho da página e dos diagramas, que revisei, ajustei e conferi contra o código.<br>• **Senso crítico:** a IA errou em pontos que precisei validar. Sugeriu um comando de execução com uma opção que só existe no Java 19 ou mais novo, enquanto eu uso o Java 17. Também precisei pedir instruções mais claras quando as primeiras ficaram confusas. Por isso passei a conferir tudo executando: compilar, rodar e comparar a saída com o esperado. |

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

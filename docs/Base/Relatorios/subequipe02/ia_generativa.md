# IA Generativa & Lições Aprendidas — Subequipe 02

## 1. Visão Geral do Foco

Esta página registra o uso de IA generativa e as lições aprendidas durante a aplicação do padrão GoF estrutural **Adapter** à busca de produtos em um catálogo simulado.

O trabalho contempla a justificativa do padrão, a modelagem UML, a implementação Java e a execução das verificações. A IA foi utilizada como apoio à elaboração e à análise dos artefatos, cabendo aos integrantes compreender, conferir e validar as sugestões antes de incorporá-las ao projeto.

A solução possui finalidade acadêmica e utiliza dados fictícios. Não representa a arquitetura interna oficial da Decathlon.

---

## 2. Quadro Integrado de Contribuições Individuais

Cada integrante deve registrar ou validar sua própria reflexão, relacionando o aprendizado às atividades efetivamente realizadas.

| Nome do membro | Lições aprendidas | Uso da IA generativa — senso crítico e validação |
| :--- | :--- | :--- |
| **Diassis Bezerra Nascimento** | A preencher pelo integrante, considerando sua atuação na justificativa, modelagem, implementação e execução inicial do Adapter. | A preencher pelo integrante, descrevendo as sugestões utilizadas, os ajustes realizados e os critérios de validação. |
| **Nayra Silva Nery** | A preencher pela integrante, considerando as atividades e revisões efetivamente realizadas. | A preencher pela integrante, descrevendo seu uso de IA e a análise crítica das sugestões. |
| **Uires Carlos de Oliveira** | Compreendi melhor os papéis Client, Target, Adapter e Adaptee e sua correspondência entre UML e código Java. Aprendi a compilar os arquivos, executar verificações e distinguir uma revisão por leitura de uma validação por execução. Também ampliei os testes em um arquivo complementar, preservando o teste original. | Utilizei o ChatGPT para apoiar a revisão da documentação e do código, analisar uma dependência UML, elaborar testes complementares e orientar a compilação e a execução no PowerShell. As sugestões foram comparadas com os arquivos apresentados e os testes foram executados no ambiente local. A alteração gráfica sugerida para o UML não foi incorporada; ficou documentada como proposta. |

> **Observação:** As reflexões individuais devem ser conferidas pelos respectivos integrantes. A utilização de IA não substitui a participação efetiva nem autoriza atribuir atividades a quem não as realizou.

---

## 3. Prompts Relevantes e Validação Crítica

Os exemplos abaixo sintetizam solicitações realizadas por Uires durante o desenvolvimento. São descrições do processo, não transcrições integrais das conversas.

### 3.1. Análise das dependências no diagrama de classes

- **Solicitação:** Analisar possibilidades de melhoria no diagrama de classes do Adapter.
- **Resultado da IA:** Sugestão de explicitar a dependência de `CatalogoAdapter` para `ItemExterno`, pois o adaptador utiliza seus dados durante a conversão.
- **Decisão adotada:** Documentar a proposta e sua justificativa, mantendo a figura original.
- **Validação:** A leitura de `CatalogoAdapter.java` mostrou o uso de `getCodigo()`, `getDescricao()` e `getValorEmCentavos()` para construir um `Produto`. A proposta explicita uma dependência já existente no código, sem alterar seu comportamento.

### 3.2. Revisão da implementação Java

- **Solicitação:** Conferir a implementação do Adapter e sua correspondência com a modelagem.
- **Resultado da IA:** Análise das responsabilidades das classes, da delegação da pesquisa e da conversão dos campos e dos valores monetários.
- **Decisão adotada:** Preservar a implementação apresentada e registrar a revisão por leitura, distinguindo-a da execução.
- **Validação:** Foram apresentados e analisados os sete arquivos Java da implementação e o teste original. Identificou-se que os construtores de `BuscaProdutos` e `CatalogoAdapter` já rejeitavam dependências nulas, tornando desnecessário acrescentar novamente essa proteção.

### 3.3. Inclusão de testes complementares

- **Solicitação:** Acrescentar novos testes sem substituir o arquivo original.
- **Resultado da IA:** Elaboração de `AdapterCheckComplementar.java`, com seis cenários identificáveis e mensagens individuais de aprovação.
- **Decisão adotada:** Criar um arquivo separado e preservar `AdapterCheck.java`.
- **Validação:** O teste original e o complementar foram compilados e executados no Windows, com PowerShell e JDK 24.0.2. Os seis cenários complementares passaram.

| Cenário complementar | Comportamento verificado |
| :--- | :--- |
| Busca por Sapato | Retorno de um produto com identificador, nome e preço esperados. |
| Busca por trecho | Localização de Sapato de corrida pelo termo `"corrida"`. |
| Termo vazio | Retorno dos três produtos, preservando seus campos e sua ordem. |
| Termo nulo | Rejeição da pesquisa com `NullPointerException`. |
| Catálogo nulo | Rejeição de dependência nula no construtor de `BuscaProdutos`. |
| Fornecedor nulo | Rejeição de dependência nula no construtor de `CatalogoAdapter`. |

### 3.4. Orientação da compilação e execução

- **Solicitação:** Orientar a execução do projeto pelo terminal do VS Code.
- **Resultado da IA:** Instruções para conferir o Java, localizar a raiz do repositório, compilar os arquivos e executar os testes e a demonstração.
- **Decisão adotada:** Executar os comandos no ambiente local e comparar as saídas com os resultados esperados.
- **Validação:** A compilação utilizou `javac --release 17 -encoding UTF-8`. O teste original apresentou `Verificacoes do Adapter: OK`; o complementar informou a aprovação dos seis cenários. A demonstração retornou as duas bolas, a busca sem correspondência e o Sapato de corrida com os preços esperados.

As evidências e os procedimentos estão documentados na página de [Execução e Verificação](/Base/Relatorios/subequipe02/execucao.md).

---

## 4. Análise Crítica do Uso da IA

A IA auxiliou na explicação dos participantes do Adapter, na revisão dos artefatos e na elaboração de cenários de teste. Entretanto, suas respostas precisaram ser confrontadas com o material existente.

Uma sugestão inicial foi verificar a necessidade de proteção contra dependências nulas. A leitura do código demonstrou que essa proteção já existia. Isso evidenciou a importância de examinar a implementação antes de propor alterações.

Também foi necessário distinguir alterações no código de alterações na documentação. A proposta de acrescentar uma dependência ao UML foi registrada em texto, mas a figura original foi mantida. Portanto, o histórico descreve uma proposta documental, sem afirmar que o diagrama publicado foi modificado.

Os testes complementares aumentam a variedade de comportamentos verificados, mas não demonstram correção para todas as entradas possíveis. Também não comprovam integração com serviços externos ou funcionamento de um comércio eletrônico em produção.

---

## 5. Lições Aprendidas

A experiência registrada permitiu destacar os seguintes aprendizados:

- **Coerência entre artefatos:** justificativa, UML, código e execução devem representar a mesma solução.
- **Responsabilidades do Adapter:** o catálogo simulado realiza a pesquisa; o adaptador traduz a chamada e os dados retornados.
- **Representação monetária:** a conversão de centavos inteiros para `BigDecimal` evita cálculos intermediários em ponto flutuante.
- **Revisão e execução:** ler o código e executá-lo são atividades complementares, com evidências diferentes.
- **Testes adicionais:** novos cenários podem ser acrescentados preservando verificações anteriores.
- **Rastreabilidade:** os registros devem identificar o que cada integrante elaborou, revisou ou executou.
- **Uso crítico de IA:** sugestões precisam ser compreendidas e verificadas antes de serem incorporadas.

As reflexões específicas de Diassis e Nayra serão complementadas pelos próprios integrantes no quadro individual.

---

## 6. Considerações Finais

O uso de IA apoiou a análise e a documentação da solução, assim como a elaboração dos testes complementares. A execução local forneceu evidências dos comportamentos cobertos pelos cenários implementados.

A responsabilidade pelas decisões e pela validação permanece com os integrantes. A documentação deve preservar a autoria das contribuições, identificar o apoio de IA e apresentar os limites da demonstração acadêmica.

---

## 7. Uso de Inteligência Artificial Generativa

**Ferramenta utilizada por Uires:** ChatGPT, da OpenAI.

**Finalidades registradas:**

- Apoio à proposta inicial do Adapter e à organização da documentação.
- Explicação e análise das relações UML.
- Revisão por leitura dos arquivos Java e dos testes.
- Elaboração de `AdapterCheckComplementar.java`.
- Orientação dos comandos de compilação e execução.
- Apoio à descrição dos resultados e à preparação da demonstração em vídeo.

A documentação de Diassis também registra apoio de IA na elaboração de seus artefatos. O detalhamento da ferramenta, das solicitações e da validação deverá ser complementado pelo próprio integrante.

O uso de IA por Nayra deverá ser descrito por ela, conforme as atividades efetivamente realizadas.

---

## Referências

[1] GAMMA, Erich; HELM, Richard; JOHNSON, Ralph; VLISSIDES, John. *Design Patterns: Elements of Reusable Object-Oriented Software*. Addison-Wesley, 1994.

[2] OPENAI. *ChatGPT*. Disponível em: https://chatgpt.com/. Acesso em: 18 out. 2026.

[3] SUBEQUIPE 02. [GoF Estrutural — Adapter](/Base/Relatorios/subequipe02/gof_estrutural.md).

[4] SUBEQUIPE 02. [Modelagem UML — Adapter de Catálogo](/Base/Relatorios/subequipe02/modelagem.md).

[5] SUBEQUIPE 02. [Implementação Java — Adapter de Catálogo](/Base/Relatorios/subequipe02/implementacao.md).

[6] SUBEQUIPE 02. [Execução e Verificação — Adapter de Catálogo](/Base/Relatorios/subequipe02/execucao.md).

---

## Histórico de Versões

| Versão | Data | Descrição | Autor(a) | Revisor(a) |
| :---: | :---: | :--- | :--- | :--- |
| 1.0 | 18/10/2026 | Adaptação da página para a Subequipe 02 e registro das experiências de Uires no uso de IA, na revisão dos artefatos e na elaboração e execução dos testes complementares, com apoio de IA generativa. | [Uires Carlos de Oliveira](https://github.com/uires2023) | Pendente. |
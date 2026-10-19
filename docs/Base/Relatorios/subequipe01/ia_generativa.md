# IA Generativa & Lições Aprendidas

## 1. Visão Geral do Foco

A Inteligência Artificial Generativa (IAG) tem ampliado as possibilidades de aplicação na Engenharia de Software, auxiliando atividades como escrita de código, documentação, modelagem e testes. Este documento apresenta a experiência da Subequipe 01 durante o desenvolvimento do Fluxo A — Gestão de Usuário, Autenticação e Perfil — do sistema DECATHLON, com foco na utilização de Modelos de Linguagem de Grande Escala (*Large Language Models* — LLMs).

Durante o projeto, a equipe utilizou ferramentas de IA como apoio à implementação e à documentação dos padrões de projeto criacionais Factory Method e Singleton. O primeiro foi aplicado à organização dos diferentes métodos de autenticação, enquanto o segundo foi utilizado para gerenciar as sessões dos usuários. A IA também auxiliou na elaboração de estruturas de classes e na criação de diagramas UML.

Apesar dos benefícios, as respostas geradas apresentaram limitações. Algumas sugestões não contemplavam adequadamente requisitos de segurança, controle de concorrência e organização dos relacionamentos entre classes. Esses problemas demonstraram que um código sintaticamente correto nem sempre atende a todos os requisitos de um sistema.

Diante disso, a equipe adotou uma abordagem baseada na revisão crítica das respostas, verificando sua compatibilidade com os requisitos e os princípios de Engenharia de Software. A IA foi utilizada como ferramenta de apoio, sem substituir as decisões técnicas dos integrantes.

Este relatório apresenta as contribuições individuais, os prompts utilizados, os resultados obtidos, as limitações identificadas e as principais lições aprendidas durante o desenvolvimento do projeto.

## 2. Quadro Integrado de Contribuições Individuais

**Entrega mínima obrigatória:** Todos os membros da subequipe registraram individualmente suas experiências, considerando os conhecimentos teóricos e as atividades realizadas durante o desenvolvimento dos artefatos.

O quadro a seguir apresenta as principais lições aprendidas por cada integrante e como a IA foi utilizada durante a implementação dos padrões Factory Method e Singleton e a elaboração dos diagramas UML.

| Nome do membro | Lições aprendidas | Uso da IA Generativa (senso crítico e embasamento) |
|---|---|---|
| **Dylan Cavalcante** | A implementação do Factory Method mostrou como esse padrão facilita a inclusão de diferentes métodos de autenticação sem exigir alterações em toda a lógica do sistema. Também ficou evidente que os padrões de projeto não garantem, por si só, a segurança da aplicação. O uso de PBKDF2-HMAC-SHA256, salt individual e bloqueio temporário após tentativas inválidas exigiu cuidados específicos. | A IA ajudou a criar a estrutura inicial das classes e interfaces utilizando o módulo `abc` do Python. Entretanto, algumas sugestões apresentavam problemas de segurança, como armazenamento de senhas em texto puro ou uso de algoritmos inadequados, como MD5. O código precisou ser revisado, e a injeção de dependência de `BancoUsuarios` e as regras de bloqueio temporário foram adaptadas com revisão humana. |
| **Mariana Ribeiro** | A implementação do Singleton demonstrou que garantir uma única instância não é suficiente para proteger dados compartilhados entre diferentes threads. Foi necessário distinguir a proteção da criação da instância do controle de acesso às sessões. O uso de `threading.Lock` e `threading.RLock` ajudou a compreender a importância da sincronização em operações concorrentes. | A IA foi utilizada para sugerir a estrutura do Singleton e apoiar a criação de testes com `unittest`. Embora tenha proposto uma proteção para a criação da instância, não protegeu adequadamente as operações no dicionário compartilhado de sessões. A implementação precisou ser revisada para controlar o acesso aos dados e reduzir o risco de inconsistências. |
| **Samuel Felipe** | A elaboração dos diagramas UML mostrou a importância de representar corretamente as classes, seus métodos e seus relacionamentos. A modelagem ajudou a compreender como o Factory Method organiza a criação dos autenticadores e como o Singleton gerencia as sessões. Também foi possível perceber que os diagramas devem corresponder à implementação real do código. | A IA foi utilizada para converter a estrutura das classes Python em código PlantUML. Apesar de gerar uma estrutura válida, o modelo adicionou relacionamentos desnecessários entre classes. Foi necessário remover conexões redundantes e ajustar os relacionamentos para representar a arquitetura de maneira mais clara e fiel ao código. |

**Observação:** As contribuições individuais devem ser revisadas pelos respectivos integrantes, preservando a autoria de cada reflexão e garantindo que os relatos correspondam às atividades efetivamente realizadas.

As experiências apresentadas demonstram que o conhecimento dos fundamentos da programação e dos padrões de projeto é essencial para avaliar as respostas geradas pela IA. Embora a ferramenta tenha auxiliado na criação das estruturas iniciais e dos diagramas, foi necessário verificar os resultados, corrigir problemas e adaptar as soluções aos requisitos do sistema. Assim, a IA atuou como ferramenta de apoio, enquanto as decisões técnicas e a validação final permaneceram sob responsabilidade da equipe.

## 3. Prompts Relevantes e Validação Crítica

A IA Generativa foi utilizada para explorar alternativas de implementação e apoiar a elaboração dos diagramas. Os prompts foram construídos com informações sobre a linguagem Python, os padrões de projeto e os requisitos do sistema DECATHLON.

Os exemplos a seguir apresentam os comandos utilizados, os resultados obtidos e as decisões tomadas pela equipe após avaliar as respostas.

### 3.1. Implementação do Singleton para Gerenciamento de Sessões

**Prompt utilizado:**

> Atuando como Arquiteto de Software Sênior especialista em Python 3.10, forneça uma estrutura robusta para a classe GerenciadorSessao utilizando o Padrão de Projeto Singleton. O sistema será acessado simultaneamente por múltiplas partes da aplicação de forma assíncrona/multithreaded. Exijo que a criação da instância única utilize o método mágico `__new__` protegido contra condições de corrida (*race conditions*). Inclua também um dicionário interno mapeando o e-mail do usuário aos dados da sessão (token e tempo de expiração).

**Resultado da IA:**

O modelo gerou uma classe Singleton utilizando o método `__new__` e `threading.Lock` para controlar a criação da instância. Também criou o dicionário `_sessoes` e métodos básicos para criar e validar sessões. Entretanto, as operações realizadas no dicionário não possuíam proteção contra acessos simultâneos.

**Decisão do grupo:**

A equipe aproveitou a estrutura inicial da classe, mas identificou que o bloqueio da criação da instância não protegia automaticamente os dados compartilhados. Por isso, a implementação interna sugerida pela IA foi descartada e revisada.

**Validação:**

Foi utilizada uma segunda camada de sincronização, por meio de `threading.RLock`, para proteger as operações realizadas sobre as sessões. A equipe revisou os acessos aos dados para manter sua consistência durante operações concorrentes, conforme documentado no arquivo `singleton_sessao.py`.

A conversa pode ser acessada [clicando aqui](INSERIR_LINK_DA_CONVERSA_1).

### 3.2. Desacoplamento da Autenticação com Factory Method

**Prompt utilizado:**

> Desenvolva a estrutura basilar de classes para um sistema de autenticação utilizando o Padrão Criacional Factory Method. Precisamos lidar com cinco tipos de acesso no ecossistema DECATHLON: Login Local, Google, Facebook, Apple e um Código via E-mail. Defina uma classe abstrata `ServicoLogin` que contenha o método fábrica `criar_autenticador()` e o método orquestrador `realizar_login(credenciais)`. Este último deverá invocar a fábrica, executar a autenticação e, em caso de sucesso, registrar a criação de uma sessão vinculada. Retorne apenas os esqueletos estruturais sem implementações de bibliotecas externas.

**Resultado da IA:**

A IA gerou uma estrutura baseada no módulo `abc`, com a interface abstrata `Autenticador` e classes criadoras para os cinco métodos de autenticação. Também apresentou o método `realizar_login`, responsável por coordenar a autenticação e simular a criação da sessão.

**Decisão do grupo:**

A estrutura inicial estava de acordo com a proposta do Factory Method, mas não contemplava adequadamente o acesso aos dados dos usuários. A equipe decidiu manter as abstrações principais e adaptar as classes para permitir a injeção de dependência do repositório `BancoUsuarios`.

**Validação:**

A implementação foi ajustada para atender aos requisitos do sistema. No fluxo de login local, foram incorporados mecanismos de segurança, incluindo hashing com PBKDF2 e bloqueio temporário após tentativas inválidas. Essas medidas exigiram análise e implementação adicionais, pois não foram contempladas adequadamente pela resposta inicial da IA.

A conversa pode ser acessada [clicando aqui](INSERIR_LINK_DA_CONVERSA_2).

### 3.3. Geração de Diagramas UML com PlantUML

**Prompt utilizado:**

> Com base na implementação das classes `ServicoLogin` e `Autenticador` detalhadas anteriormente (código Python fornecido abaixo), gere a representação formal para um Diagrama de Classes UML utilizando estritamente a linguagem declarativa do PlantUML. Mapeie rigorosamente as associações, as implementações (herança com seta vazada) e garanta que os métodos abstratos apareçam em itálico. Evite redundâncias na representação da injeção de dependência.

**Resultado da IA:**

O modelo gerou um código PlantUML válido, incluindo classes, métodos, visibilidade e relacionamentos de herança. Entretanto, adicionou setas de dependência redundantes entre os criadores e os produtos, tornando o diagrama mais complexo e menos claro.

**Decisão do grupo:**

A equipe aproveitou a estrutura gerada como ponto de partida para a elaboração do diagrama. Em seguida, revisou os relacionamentos e removeu as conexões desnecessárias.

**Validação:**

O diagrama final foi ajustado para representar os relacionamentos de forma consistente com a implementação do sistema. A experiência demonstrou que a IA pode facilitar a geração da sintaxe do PlantUML, mas a revisão humana continua necessária para garantir a clareza e a correção da modelagem.

A conversa pode ser acessada [clicando aqui](INSERIR_LINK_DA_CONVERSA_3).

## 4. Análise Crítica do Uso da IA

A utilização da Inteligência Artificial Generativa durante o desenvolvimento do Fluxo A trouxe benefícios para a criação de estruturas de classes, a exploração de soluções e a documentação do sistema. Entretanto, também revelou limitações que exigiram revisão técnica.

Uma das principais vantagens foi a rapidez na geração de estruturas iniciais. A IA conseguiu sugerir interfaces abstratas, classes concretas e código PlantUML, reduzindo o trabalho repetitivo. Por outro lado, nem todas as respostas consideraram adequadamente os requisitos de segurança, concorrência e organização arquitetural.

A análise das experiências da equipe pode ser resumida em três aspectos:

| Aspecto analisado | Limitação identificada | Experiência no projeto |
|---|---|---|
| **Estrutura do código** | A IA consegue gerar classes e interfaces, mas pode deixar de considerar requisitos específicos do sistema. | A estrutura inicial do Factory Method precisou ser adaptada para permitir a injeção de dependência de `BancoUsuarios`. |
| **Concorrência** | A proteção de uma operação não garante a segurança de todas as operações realizadas sobre dados compartilhados. | No Singleton, foi necessário proteger também o acesso ao dicionário de sessões utilizando `threading.RLock`. |
| **Segurança e modelagem** | O código pode conter soluções inseguras, enquanto os diagramas podem apresentar relacionamentos desnecessários. | A equipe revisou os mecanismos de autenticação e ajustou as conexões geradas pelo PlantUML. |

Esses resultados demonstram que a correção sintática não garante a correção funcional ou arquitetural de uma solução. Um código pode executar sem erros e, ainda assim, apresentar problemas de segurança ou comportamento inesperado em situações específicas.

Outro ponto importante é a necessidade de fornecer contexto adequado nos prompts. Informar a linguagem utilizada, os requisitos do sistema e as restrições de implementação ajuda a direcionar as respostas. Entretanto, mesmo prompts detalhados não eliminam a possibilidade de erros.

A revisão humana foi, portanto, fundamental em todas as etapas. A equipe precisou comparar as sugestões com os requisitos do projeto, verificar os relacionamentos entre as classes e avaliar as implicações das decisões técnicas.

Conclui-se que a IA Generativa pode aumentar a produtividade e facilitar tarefas repetitivas, mas não substitui o conhecimento necessário para analisar requisitos, identificar vulnerabilidades e tomar decisões arquiteturais. Seu uso exige senso crítico, testes e validação contínua.

## 5. Lições Aprendidas

O desenvolvimento dos padrões Factory Method e Singleton, associado ao uso da IA Generativa, proporcionou aprendizados sobre organização do código, segurança, concorrência e documentação de software.

### 5.1. Desacoplamento com Factory Method

A implementação do Factory Method demonstrou como separar a criação dos autenticadores da lógica principal de login facilita a manutenção e a inclusão de novos métodos de autenticação.

Essa separação reduz a necessidade de modificar o fluxo principal sempre que um novo provedor é adicionado. Entretanto, a equipe também aprendeu que o desacoplamento precisa ser acompanhado de mecanismos adequados de segurança, como validação de credenciais, hashing de senhas e bloqueio temporário após falhas consecutivas.

### 5.2. Controle de Estado com Singleton

A implementação do Singleton mostrou que manter uma única instância não resolve, por si só, os problemas de concorrência. Quando várias threads acessam os mesmos dados, é necessário controlar as operações para evitar inconsistências.

O uso de `threading.Lock` e `threading.RLock` permitiu compreender a diferença entre proteger a criação de uma instância e proteger o estado compartilhado. Também ficou evidente que um Singleton em memória possui limitações em sistemas distribuídos, nos quais diferentes processos ou servidores podem precisar compartilhar as mesmas sessões.

### 5.3. Uso Crítico da Inteligência Artificial

A experiência com a IA mostrou que as respostas devem ser avaliadas antes de serem incorporadas ao projeto. Embora a ferramenta tenha ajudado na geração de código e diagramas, algumas sugestões precisaram ser corrigidas por não atenderem integralmente aos requisitos.

A equipe compreendeu que a responsabilidade pela qualidade do software continua sendo dos desenvolvedores. Isso inclui revisar o código, realizar testes, identificar casos de falha e verificar os requisitos de segurança.

### 5.4. Importância da Documentação

A criação dos diagramas UML demonstrou que a documentação precisa representar fielmente o sistema implementado. Diagramas com relacionamentos incorretos ou redundantes podem dificultar a compreensão da arquitetura.

A IA facilitou a elaboração inicial da modelagem, mas a revisão dos relacionamentos foi necessária para garantir sua clareza e consistência.

Em conjunto, essas experiências reforçaram que o uso responsável da IA depende do conhecimento técnico, da revisão crítica e da validação dos resultados. A ferramenta pode acelerar o trabalho, mas a qualidade final depende das decisões e verificações realizadas pela equipe.

## 6. Considerações Finais

O desenvolvimento do Fluxo A permitiu aplicar os padrões Factory Method e Singleton e avaliar, na prática, as possibilidades e limitações da Inteligência Artificial Generativa na Engenharia de Software.

O Factory Method contribuiu para organizar os diferentes métodos de autenticação, reduzindo o acoplamento entre a criação dos autenticadores e o fluxo principal de login. O Singleton, por sua vez, permitiu centralizar o gerenciamento das sessões, exigindo cuidados adicionais com o acesso concorrente aos dados compartilhados.

A utilização da IA auxiliou na criação das estruturas iniciais de código e na elaboração dos diagramas UML. Entretanto, foram identificados problemas relacionados à segurança, à concorrência e à representação dos relacionamentos entre classes. Essas limitações reforçaram a importância de revisar e testar os resultados antes de incorporá-los ao sistema.

A principal conclusão é que a IA Generativa deve ser utilizada como ferramenta de apoio, e não como substituta do conhecimento técnico. A compreensão dos padrões de projeto, dos requisitos de segurança e dos princípios de arquitetura foi essencial para avaliar as sugestões e realizar as correções necessárias.

Assim, a experiência demonstrou que a produtividade proporcionada pela IA precisa estar associada à responsabilidade técnica, à documentação adequada e à validação contínua. Esses fatores são fundamentais para desenvolver sistemas seguros, compreensíveis e de fácil manutenção.

## 7. Uso de Inteligência Artificial Generativa

A utilização da IA Generativa foi organizada de modo a preservar a participação dos integrantes nas decisões técnicas e no desenvolvimento dos artefatos. O processo foi dividido em quatro etapas.

1. **Definição da arquitetura:** a equipe selecionou os padrões Factory Method e Singleton com base nos requisitos do sistema DECATHLON e nos conhecimentos de Engenharia de Software.

2. **Elaboração dos prompts:** após definir a arquitetura, os integrantes formularam comandos com informações sobre Python 3.10, os padrões de projeto e as restrições necessárias para orientar a geração de código e diagramas.

3. **Análise das respostas:** os resultados foram avaliados criticamente para identificar problemas de segurança, concorrência, dependência entre classes e clareza dos diagramas.

4. **Revisão e validação:** a equipe corrigiu as estruturas geradas, adaptou o código aos requisitos e revisou os diagramas antes de incorporá-los à documentação do projeto.

A IA foi utilizada principalmente como apoio à geração de estruturas iniciais e à exploração de alternativas. As decisões sobre a arquitetura, a avaliação dos resultados e a validação final permaneceram sob responsabilidade dos integrantes.

Essa abordagem permitiu aproveitar os benefícios da ferramenta sem dispensar a análise técnica. O processo também reforçou a importância de conhecer os fundamentos da Engenharia de Software para identificar erros e avaliar as soluções sugeridas por modelos generativos.

## Referências

[1] OPENAI. **ChatGPT**. Disponível em: [https://chatgpt.com/](https://chatgpt.com/). Acesso em: 17 set. 2026.

## Histórico de Versões

| Versão | Data | Descrição | Autores | Revisor |
|---|---|---|---|---|
| 0.1 | 17/10/2026 | Criação e estruturação da página de IA Generativa & Lições Aprendidas. | [Dylan Cavalcante](https://github.com/dylancavalcante)  | [Samuel Felipe](https://github.com/TerminaKng05) |
| 0.2 | 17/10/2026 | Adição de conteúdo sobre o uso de IA e as lições aprendidas. | [Samuel Felipe](https://github.com/TerminaKng05) |  |
| 0.3 | 18/10/2026 | Adição de conteúdo sobre o uso de IA e as lições aprendidas. | [Dylan Cavalcante](https://github.com/dylancavalcante)  | — |
| 0.4 | 18/10/2026 | Adição de conteúdo sobre o uso de IA e as lições aprendidas. | [Mariana Ribeiro](https://github.com/marianagonzaga0) | — |
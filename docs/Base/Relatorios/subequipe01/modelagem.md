# Modelagem UML dos Padrões GoF: Factory Method e Singleton

**Subequipe 01 — Entrega 03**

- **Domínio:** Fluxo A — Gestão de Usuário, Autenticação e Perfil
- **Sistema de referência:** DECATHLON

## Divisão de Responsabilidades e Revisão em Pares

A tabela a seguir detalha a divisão de responsabilidades, a revisão em pares (*peer review*) e as evidências de contribuição referentes à elaboração deste artefato.

| Etapa / Tópico do Relatório | Autor(a) Principal | Revisor(a) em Par | Evidência / Commit |
| :--- | :--- | :--- | :---: |
| 1. Introdução | [Samuel Felipe](https://github.com/TerminaKng05) | [Mariana Ribeiro](https://github.com/marianagonzaga0) | A definir |
| 2. Objetivos (geral e específicos) | [Samuel Felipe](https://github.com/TerminaKng05) | [Dylan Cavalcante](https://github.com/dylancavalcante)  | A definir |
| 3. Visão Geral dos Padrões | [Dylan Cavalcante](https://github.com/dylancavalcante)  | [Samuel Felipe](https://github.com/TerminaKng05)| A definir |
| 4. Metodologia | [Samuel Felipe](https://github.com/TerminaKng05) | [Mariana Ribeiro](https://github.com/marianagonzaga0) | A definir |
| 5. Modelagem UML: Factory Method e descrição do modelo | [Dylan Cavalcante](https://github.com/dylancavalcante), [Samuel Felipe](https://github.com/TerminaKng05), [Mariana Ribeiro](https://github.com/marianagonzaga0) | [Mariana Ribeiro](https://github.com/marianagonzaga0)| A definir |
| 6. Modelagem UML: Singleton e descrição do modelo | [Dylan Cavalcante](https://github.com/dylancavalcante), [Samuel Felipe](https://github.com/TerminaKng05), [Mariana Ribeiro](https://github.com/marianagonzaga0) | [Dylan Cavalcante](https://github.com/dylancavalcante)  | A definir |
| 7. Integração entre os Modelos | [Dylan Cavalcante](https://github.com/dylancavalcante), [Samuel Felipe](https://github.com/TerminaKng05), [Mariana Ribeiro](https://github.com/marianagonzaga0)| [Dylan Cavalcante](https://github.com/dylancavalcante)  | A definir |
| 8. Conclusão | [Mariana Ribeiro](https://github.com/marianagonzaga0)| [Samuel Felipe](https://github.com/TerminaKng05) | A definir |
| Referências bibliográficas | [Mariana Ribeiro](https://github.com/marianagonzaga0) |[Samuel Felipe](https://github.com/TerminaKng05)| A definir |
| Histórico de versão e revisão documental | [Dylan Cavalcante](https://github.com/dylancavalcante)  | [Mariana Ribeiro](https://github.com/marianagonzaga0) | A definir |

## 1. Introdução

A modelagem de software é uma etapa importante do desenvolvimento, pois permite representar a estrutura de um sistema antes ou durante sua implementação. Por meio de diagramas UML (*Unified Modeling Language*), é possível identificar classes, responsabilidades, relacionamentos e mecanismos de interação, facilitando a compreensão da arquitetura proposta.

No contexto do Fluxo A do projeto, voltado à gestão de usuários, autenticação e perfil, foram selecionados dois Padrões de Projeto Criacionais (*Gang of Four — GoF*): **Factory Method** e **Singleton**.

O Factory Method organiza a criação dos diferentes autenticadores, separando o processo de autenticação das implementações específicas de cada modalidade de login. Já o Singleton centraliza o gerenciamento das sessões em uma única instância por processo Python.

A Subequipe 01 desenvolveu os diagramas UML correspondentes a esses padrões, considerando as responsabilidades das classes e sua relação com a implementação em Python. A modelagem busca demonstrar como os padrões se complementam no fluxo de autenticação e gerenciamento de sessões do projeto.

## 2. Objetivos

### 2.1. Objetivo Geral

Modelar, por meio de diagramas UML, a aplicação dos padrões criacionais Factory Method e Singleton no Fluxo A do projeto, representando suas classes, responsabilidades e relacionamentos de acordo com a implementação proposta para autenticação e gerenciamento de sessões.

### 2.2. Objetivos Específicos

- Representar a estrutura do Factory Method, identificando os criadores, os autenticadores e suas relações.
- Modelar a estrutura do Singleton e seu mecanismo de controle da instância do gerenciador de sessões.
- Demonstrar como os dois padrões se integram ao fluxo de autenticação e criação de sessões.
- Manter a correspondência entre os diagramas UML e as classes implementadas em Python.
- Facilitar a compreensão, a manutenção e a evolução da arquitetura do projeto.

## 3. Visão Geral dos Padrões

Os dois padrões possuem responsabilidades distintas e complementares no sistema.

| Padrão | Finalidade no projeto | Elemento principal |
| :--- | :--- | :--- |
| **Factory Method** | Delegar a criação dos autenticadores específicos de cada modalidade de login. | `ServicoLogin` |
| **Singleton** | Centralizar o gerenciamento das sessões em uma única instância por processo Python. | `GerenciadorSessao` |

O Factory Method determina qual autenticador será utilizado para validar as credenciais. Quando a autenticação é bem-sucedida, o serviço solicita ao `GerenciadorSessao` a criação de uma sessão, estabelecendo a integração entre os dois padrões.

## 4. Metodologia

A modelagem foi elaborada a partir dos requisitos do Fluxo A, das responsabilidades definidas para os padrões GoF e da implementação demonstrativa em Python.

O processo foi organizado nas seguintes etapas:

1. **Análise do domínio:** identificação das necessidades relacionadas à autenticação, ao gerenciamento de usuários e ao controle das sessões.
2. **Identificação dos padrões:** definição dos papéis e das responsabilidades de Factory Method e Singleton no sistema.
3. **Mapeamento das classes:** identificação das classes abstratas, das implementações concretas, dos métodos e dos relacionamentos relevantes.
4. **Construção dos diagramas:** representação da estrutura dos padrões por meio de diagramas de classes UML, utilizando como apoio o material didático disponibilizado e as referências do Refactoring.Guru.
5. **Verificação de correspondência:** comparação dos elementos modelados com os arquivos de implementação, buscando manter a consistência entre o projeto arquitetural e o código.

Os diagramas foram elaborados com o auxílio do [Draw.io](https://www.drawio.com/), ferramenta utilizada para a criação e edição de diagramas.

## 5. Modelagem UML: Factory Method

O diagrama UML a seguir representa a estrutura do padrão Factory Method aplicada ao fluxo de autenticação do projeto.

![Diagrama UML do Factory Method](../subequipe01/img%20diagramas/Diagrama_FactoryMethod.png)

*Figura 1 — Diagrama de classes UML do padrão Factory Method. Fonte: Elaborado por Samuel Felipe, Subequipe 01 (2026).*

### 5.1. Descrição do Modelo

A estrutura modelada separa a responsabilidade de criar os autenticadores daquela de executar a autenticação.

A classe `ServicoLogin` representa o criador abstrato (*Creator*), responsável por declarar o método fábrica `criar_autenticador()` e organizar o fluxo comum por meio de `realizar_login()`.

As subclasses `LoginLocal`, `LoginGoogle`, `LoginFacebook`, `LoginApple` e `LoginCodigoEmail` representam os criadores concretos (*ConcreteCreators*). Cada uma implementa o método fábrica para retornar o autenticador correspondente.

A classe abstrata `Autenticador` define o contrato comum por meio do método `autenticar()`. Suas implementações concretas executam as regras específicas de cada modalidade de acesso.

| Papel GoF | Classe no projeto | Responsabilidade |
| :--- | :--- | :--- |
| **Creator** | `ServicoLogin` | Define o método fábrica e coordena o fluxo de autenticação. |
| **ConcreteCreator** | `LoginLocal`, `LoginGoogle`, `LoginFacebook`, `LoginApple` e `LoginCodigoEmail` | Selecionam e criam os autenticadores concretos. |
| **Product** | `Autenticador` | Define o contrato comum de autenticação. |
| **ConcreteProduct** | `AutenticadorLocal`, `AutenticadorGoogle`, `AutenticadorFacebook`, `AutenticadorApple` e `AutenticadorCodigoEmail` | Implementam as regras de autenticação específicas. |

A modelagem evidencia o desacoplamento entre o fluxo principal de login e as implementações concretas. Dessa maneira, novas modalidades podem ser acrescentadas por meio de novos criadores e autenticadores, sem concentrar todas as regras em uma única classe.

## 6. Modelagem UML: Singleton

O diagrama UML a seguir representa a estrutura do padrão Singleton aplicada ao gerenciamento das sessões dos usuários.

![Diagrama UML do Singleton](../subequipe01/img%20diagramas/Diagrama_Singleton.png)

*Figura 2 — Diagrama de classes UML do padrão Singleton. Fonte: Elaborado por Samuel Felipe, Subequipe 01 (2026).*

### 6.1. Descrição do Modelo

A classe `GerenciadorSessao` concentra as operações de gerenciamento das sessões. O padrão Singleton controla sua instanciação por meio do método `__new__()`, assegurando que as chamadas ao construtor retornem a mesma instância dentro do processo Python.

A criação da instância é protegida por uma trava de sincronização. As operações de gerenciamento também utilizam mecanismos de sincronização para proteger o estado compartilhado durante acessos concorrentes.

O gerenciador mantém os dados das sessões e disponibiliza operações para sua criação, validação, consulta e encerramento.

| Elemento | Responsabilidade |
| :--- | :--- |
| `GerenciadorSessao` | Centralizar o gerenciamento das sessões. |
| `__new__()` | Controlar a criação e a reutilização da instância. |
| `criar_sessao()` | Gerar e registrar uma nova sessão. |
| `validar_sessao()` | Verificar a validade do token e, quando apropriado, renovar o período de inatividade. |
| `dispositivos_conectados()` / `sessoes_ativas()` | Consultar as sessões e seus metadados. |
| `encerrar_sessao()` | Invalidar uma sessão específica. |
| `encerrar_todas()` | Encerrar as sessões registradas para um usuário. |

A modelagem evidencia a centralização do estado das sessões e das operações necessárias ao controle de acesso.

É importante destacar que a instância única é compartilhada dentro de um processo Python. A implementação em memória não garante o compartilhamento automático das sessões entre processos ou servidores diferentes.

## 7. Integração entre os Modelos

Os diagramas representam componentes que trabalham de forma complementar no fluxo de autenticação.

A sequência lógica de interação é:

1. O usuário seleciona uma modalidade de login.
2. O criador concreto do Factory Method fornece o autenticador correspondente.
3. O autenticador executa a validação das credenciais.
4. Se a autenticação for bem-sucedida, `ServicoLogin` solicita a criação de uma sessão.
5. `GerenciadorSessao`, por meio do Singleton, registra a sessão e administra sua validade.
6. Nas solicitações posteriores, a aplicação deve validar a sessão antes de permitir o acesso aos recursos protegidos.

O Factory Method é responsável pela criação e seleção dos autenticadores, enquanto o Singleton centraliza o controle das sessões em memória. Essa divisão permite representar as responsabilidades dos componentes sem concentrar a lógica de autenticação e o gerenciamento de sessões em uma única classe.

## 8. Conclusão

A modelagem UML dos padrões Factory Method e Singleton permitiu representar de forma estruturada as responsabilidades e os relacionamentos envolvidos na autenticação e no gerenciamento das sessões do Fluxo A.

O Factory Method demonstrou como a criação dos autenticadores pode ser delegada a classes especializadas, favorecendo o desacoplamento e a extensibilidade do fluxo de login. O Singleton representou a centralização das operações de sessão em uma única instância por processo Python, facilitando o controle de criação, validação e encerramento dos acessos.

A elaboração dos diagramas também contribui para verificar a correspondência entre a arquitetura planejada e a implementação em código, facilitando a comunicação entre os integrantes da equipe e a manutenção futura.

Conclui-se que os dois padrões possuem responsabilidades complementares no projeto. Sua aplicação organiza a estrutura do fluxo de autenticação, desde que sejam consideradas as limitações da implementação em memória e a necessidade de mecanismos adicionais para uma aplicação distribuída ou de produção.

## Referências

REFACTORING.GURU. [Design Patterns: Factory Method](https://refactoring.guru/design-patterns/factory-method).

REFACTORING.GURU. [Design Patterns: Singleton](https://refactoring.guru/design-patterns/singleton).

DRAW.IO. [Ferramenta para criação de diagramas](https://www.drawio.com/).

SERRANO, Milene. *Arquitetura e Desenho de Software: Aula GoFs Criacionais*. Universidade de Brasília (UnB Gama).

SUBEQUIPE 01. *Documentação de Implementação* (`implementacao.md`).

## Histórico de Versão

| Versão | Data | Descrição | Autor | Revisor |
| :---: | :---: | :--- | :--- |:----|
| 1.0 | 17/10/2026 | Estruturação inicial da página de modelagem dos padrões Factory Method e Singleton. | Samuel Felipe ||
| 1.2 | 17/10/2026 | Inclusão da visão geral e da metodologia. | Samuel Felipe ||
| 1.3 | 17/10/2026 | Inclusão dos diagramas UML e das respectivas descrições. | Samuel Felipe ||
| 1.4 | 18/10/2026 | Inclusão dos objetivos geral e específicos, detalhamento das responsabilidades dos padrões, integração entre os modelos e conclusão. | Samuel Felipe ||
| 1.5 | 18/10/2026 | Refatoração da página de acordo com os padrões de projeto. | [Dylan Cavalcante](https://github.com/dylancavalcante) ||

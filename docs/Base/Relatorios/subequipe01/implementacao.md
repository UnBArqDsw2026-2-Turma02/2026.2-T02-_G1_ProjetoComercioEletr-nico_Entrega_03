# Implementação dos Padrões GoF: Factory Method e Singleton

**Subequipe 01 — Entrega 03**

- **Domínio:** Fluxo A — Gestão de Usuário, Autenticação e Perfil
- **Sistema de referência:** DECATHLON
- **Tecnologia:** Python 3.10+
- **Arquivos principais:** `factory_method_autenticacao.py` e `singleton_sessao.py`

## Divisão de Responsabilidades e Revisão em Pares

A tabela a seguir detalha a divisão de responsabilidades, a revisão em pares (*peer review*) e as evidências de contribuição referentes à elaboração deste artefato.

| Etapa / Tópico do Relatório | Autor(a) Principal | Revisor(a) em Par | Evidência / Commit |
| :--- | :--- | :--- | :---: |
| 1. Introdução | A definir | A definir | A definir |
| 2. Objetivos (geral e específicos) | A definir | A definir | A definir |
| 3. Organização da Implementação | A definir | A definir | A definir |
| 4. Implementação do Factory Method: estrutura, código e segurança | A definir | A definir | A definir |
| 5. Implementação do Singleton: estrutura, código e gerenciamento de sessões | A definir | A definir | A definir |
| 6. Integração entre os Padrões | A definir | A definir | A definir |
| 7. Testes e Resultados Observados | A definir | A definir | A definir |
| 8. Instruções de Execução | A definir | A definir | A definir |
| 9. Identificação e Acesso aos Códigos-Fonte | A definir | A definir | A definir |
| 10. Limitações e Considerações de Segurança | A definir | A definir | A definir |
| 11. Histórico de Versão e Revisão Documental | A definir | A definir | A definir |

---

## 1. Introdução

O desenvolvimento de sistemas orientados a objetos exige que a criação de objetos e o gerenciamento de seus estados sejam organizados de maneira consistente. No Fluxo A do projeto, dedicado à gestão de usuários, autenticação e perfil, esses desafios estão relacionados à existência de diferentes métodos de login e à necessidade de controlar as sessões dos usuários.

Para atender a essas necessidades, a Subequipe 01 implementou dois Padrões de Projeto Criacionais (*Gang of Four — GoF*): **Factory Method** e **Singleton**.

O Factory Method organiza a criação dos autenticadores, permitindo que cada modalidade de acesso utilize sua própria implementação sem concentrar todas as regras em uma única classe. O Singleton centraliza o gerenciamento das sessões em uma única instância por processo Python.

Este documento apresenta a estrutura das implementações, suas decisões de projeto, os mecanismos de segurança demonstrados, os testes executados e as limitações da solução.

## 2. Objetivos

### 2.1. Objetivo geral

Documentar e demonstrar a implementação dos padrões Factory Method e Singleton no fluxo de autenticação e gerenciamento de sessões, explicando suas responsabilidades, funcionamento e contribuições para a organização arquitetural do projeto.

### 2.2. Objetivos específicos

- Demonstrar como o Factory Method desacopla o fluxo de autenticação da criação dos autenticadores.
- Apresentar como o Singleton controla a criação e o gerenciamento das sessões.
- Documentar os mecanismos de segurança implementados na simulação.
- Registrar os testes realizados e seus resultados observados.
- Identificar as limitações da implementação e os requisitos adicionais para uma aplicação em produção.

## 3. Organização da Implementação

A solução foi dividida em dois arquivos principais, cada um responsável por um padrão de projeto.

| Padrão | Arquivo | Responsabilidade |
|---|---|---|
| Factory Method | `factory_method_autenticacao.py` | Criar os autenticadores e coordenar o fluxo de autenticação. |
| Singleton | `singleton_sessao.py` | Centralizar a criação, validação, consulta e revogação das sessões. |

O arquivo do Factory Method importa `GerenciadorSessao` para registrar as sessões após autenticações bem-sucedidas. Dessa forma, os dois padrões atuam de maneira complementar, mantendo separadas as responsabilidades de autenticação e gerenciamento de sessões.

## 4. Implementação do Factory Method

### 4.1. Problema identificado

O fluxo de autenticação contempla cinco modalidades: e-mail e senha, Google, Facebook, Apple e código de uso único enviado por e-mail.

Cada modalidade possui regras de validação próprias. Sem uma estrutura de criação organizada, a classe responsável pelo login precisaria conhecer os detalhes de cada autenticador, aumentando o acoplamento e dificultando futuras extensões.

### 4.2. Estrutura implementada

O Factory Method foi implementado por meio de uma classe abstrata que define o método fábrica e de subclasses responsáveis por retornar os autenticadores concretos.

| Papel GoF | Classe ou implementação | Responsabilidade |
|---|---|---|
| **Product** | `Autenticador` | Define a interface comum por meio do método `autenticar()`. |
| **ConcreteProduct** | `AutenticadorLocal`, `AutenticadorGoogle`, `AutenticadorFacebook`, `AutenticadorApple` e `AutenticadorCodigoEmail` | Implementam as regras específicas de cada modalidade de autenticação. |
| **Creator** | `ServicoLogin` | Declara `criar_autenticador()` e organiza o fluxo comum em `realizar_login()`. |
| **ConcreteCreator** | `LoginLocal`, `LoginGoogle`, `LoginFacebook`, `LoginApple` e `LoginCodigoEmail` | Sobrescrevem o método fábrica para fornecer o autenticador correspondente. |

### 4.3. Trecho de código: criação dos autenticadores

O código a seguir demonstra como `ServicoLogin` utiliza o autenticador por meio de sua interface comum e como as subclasses definem qual produto será criado.

```python
class ServicoLogin(ABC):
    def __init__(self, banco: BancoUsuarios):
        self.banco = banco

    @abstractmethod
    def criar_autenticador(self) -> Autenticador:
        """Factory Method."""
        raise NotImplementedError

    def realizar_login(self, credenciais: dict) -> ResultadoAutenticacao:
        autenticador = self.criar_autenticador()
        resultado = autenticador.autenticar(credenciais)

        if resultado.sucesso and resultado.email:
            resultado.token_sessao = self._iniciar_sessao(
                resultado.email
            )

        return resultado


class LoginLocal(ServicoLogin):
    def criar_autenticador(self) -> Autenticador:
        return AutenticadorLocal(self.banco)


class LoginGoogle(ServicoLogin):
    def criar_autenticador(self) -> Autenticador:
        return AutenticadorGoogle(self.banco)
```

*Trecho representativo de `factory_method_autenticacao.py`. O código completo contém também os criadores para Facebook, Apple e código de e-mail.*

O método `realizar_login()` executa três etapas principais: solicita a criação do autenticador, delega a validação das credenciais e, quando a autenticação é bem-sucedida e existe um e-mail associado, solicita a criação de uma sessão.

A escolha do autenticador fica sob responsabilidade da subclasse correspondente, reduzindo a necessidade de alterar o fluxo comum de login para cada nova modalidade.

### 4.4. Decisões de implementação e segurança

A implementação combina a estrutura do padrão com mecanismos demonstrativos de proteção da autenticação.

| Mecanismo | Implementação | Finalidade |
|---|---|---|
| Mensagem genérica de erro | O login local utiliza `"E-mail ou senha inválidos."` para credenciais incorretas e contas inexistentes. | Reduzir a possibilidade de enumeração de usuários pelas respostas do sistema. |
| Bloqueio temporário | Cinco tentativas consecutivas incorretas ativam um bloqueio de 15 minutos no fluxo de login local. | Dificultar tentativas repetidas de adivinhação de senhas. |
| Derivação de hash de senha | Utilização de PBKDF2-HMAC-SHA256 com *salt* aleatório individual e 600.000 iterações na implementação demonstrativa. | Evitar armazenar senhas em texto puro e elevar o custo de tentativas de descoberta das senhas. |
| Código de uso único | Código de seis dígitos, validade de 15 minutos e limite de tentativas. | Permitir autenticação por código temporário. |
| Consumo do código | A validação e a remoção do código são protegidas por uma trava compartilhada no repositório em memória. | Impedir que uma tentativa incorreta consuma o código correto e evitar sua reutilização bem-sucedida. |
| Provedores sociais | Validadores específicos consultam tokens de teste previamente cadastrados. | Demonstrar a separação entre os autenticadores sem depender de serviços externos. |

Os mecanismos foram projetados para demonstrar decisões de segurança no contexto acadêmico. A simulação, entretanto, não substitui uma avaliação de segurança de uma aplicação real.

O bloqueio em memória, por exemplo, não oferece limitação de tentativas compartilhada entre servidores diferentes. Da mesma forma, os autenticadores sociais não validam tokens reais emitidos por Google, Facebook ou Apple.

## 5. Implementação do Singleton

### 5.1. Problema identificado

O gerenciamento de sessões exige que as operações de criação, validação e encerramento utilizem um estado consistente.

Se módulos diferentes mantiverem gerenciadores independentes, um módulo poderá não reconhecer uma sessão encerrada por outro. Isso prejudica o controle de acesso e pode permitir que informações desatualizadas sobre sessões permaneçam na memória.

### 5.2. Estrutura implementada

A classe `GerenciadorSessao` implementa o Singleton por meio de `__new__()`, que controla a criação da instância.

A implementação utiliza duas travas com finalidades diferentes:

- `threading.Lock`: protege a inicialização da instância única.
- `threading.RLock`: protege as operações de consulta e modificação das sessões compartilhadas.

A unicidade garantida por essa implementação é limitada ao processo Python. Processos ou servidores independentes não compartilham automaticamente a instância ou seus dados em memória.

### 5.3. Trecho de código: controle da instância

O trecho abaixo apresenta o mecanismo responsável por garantir que chamadas sucessivas a `GerenciadorSessao()` retornem o mesmo objeto.

```python
class GerenciadorSessao:
    _instancia = None
    _lock_instancia = threading.Lock()

    def __new__(cls):
        with cls._lock_instancia:
            if cls._instancia is None:
                instancia = super().__new__(cls)
                instancia._sessoes = {}
                instancia._lock_sessoes = threading.RLock()
                cls._instancia = instancia

        return cls._instancia
```

A trava evita que duas threads criem simultaneamente instâncias diferentes durante a inicialização. Após a criação, as chamadas seguintes reutilizam o objeto existente.

### 5.4. Gerenciamento e validação das sessões

O gerenciador oferece métodos para criar sessões, validar sua validade, consultar metadados e encerrar acessos.

Na criação, o token é gerado utilizando uma fonte de aleatoriedade apropriada para segredos, e a sessão recebe um prazo inicial de inatividade de 30 minutos.

A validação verifica se o token existe e se a sessão expirou. Quando a sessão permanece válida e a renovação está habilitada, o período de inatividade é atualizado.

```python
def validar_sessao(
    self,
    email: str,
    token: str,
    renovar: bool = True
) -> bool:
    email_normalizado = self._normalizar_email(email)
    agora = time.time()

    with self._lock_sessoes:
        sessoes_usuario = self._sessoes.get(email_normalizado)

        if not sessoes_usuario:
            return False

        dados = sessoes_usuario.get(token)

        if dados is None:
            return False

        if dados["expira_em"] <= agora:
            del sessoes_usuario[token]

            if not sessoes_usuario:
                self._sessoes.pop(email_normalizado, None)

            return False

        if renovar:
            dados["ultima_atividade"] = agora
            dados["expira_em"] = agora + TEMPO_INATIVIDADE_SEGUNDOS

        return True
```

*Trecho representativo de `singleton_sessao.py`.*

Os principais métodos complementares são:

| Método | Responsabilidade |
|---|---|
| `criar_sessao()` | Gera o token e registra os metadados e o prazo de expiração. |
| `validar_sessao()` | Verifica a validade da sessão e pode renovar o timeout de inatividade. |
| `sessoes_ativas()` | Retorna os metadados das sessões ativas sem expor os tokens. |
| `dispositivos_conectados()` | Oferece uma consulta compatível com o exemplo de dispositivos conectados, retornando metadados das sessões. |
| `encerrar_sessao()` | Invalida uma sessão específica. |
| `encerrar_todas()` | Remove as sessões registradas para determinado usuário nessa instância. |

A consulta de dispositivos conectados não identifica automaticamente dispositivos físicos. Ela apresenta os metadados registrados durante a criação das sessões.

## 6. Integração entre os padrões

A integração ocorre depois que o autenticador retorna um resultado bem-sucedido.

O serviço de login chama seu método interno `_iniciar_sessao()`, que solicita ao Singleton a criação da sessão correspondente. Assim, o Factory Method permanece responsável pela escolha e execução do autenticador, enquanto o Singleton gerencia o estado das sessões.

O fluxo integrado pode ser resumido da seguinte maneira:

1. O usuário escolhe uma modalidade de autenticação.
2. O criador concreto fornece o autenticador correspondente.
3. O autenticador valida as credenciais.
4. Se a autenticação for bem-sucedida, o serviço solicita uma sessão.
5. O Singleton registra o token e os metadados da sessão.
6. Nas operações posteriores, a validade do token precisa ser verificada pelo sistema que protege os recursos.

Essa divisão evita concentrar a lógica de todas as modalidades de login na classe responsável pelo gerenciamento das sessões.

## 7. Testes e Resultados Observados

Os arquivos contêm blocos de execução direta que demonstram o comportamento dos padrões no terminal. Os testes apresentados são demonstrativos e não substituem uma suíte automatizada de testes unitários.

### 7.1. Testes do Singleton

A execução de `singleton_sessao.py` apresentou os seguintes resultados:

| Teste | Resultado observado | Interpretação |
|---|---|---|
| Comparação entre duas chamadas ao construtor | Ambas retornaram o mesmo identificador e a comparação com `is` foi verdadeira. | A instância única foi demonstrada dentro do processo. |
| Criação de duas sessões | Duas sessões ativas foram registradas. | O gerenciador aceita sessões distintas para o mesmo usuário. |
| Validação de sessão | A primeira sessão foi considerada válida. | O token registrado foi reconhecido pelo gerenciador. |
| Logout individual | A operação retornou `True` e restou uma sessão. | Uma sessão foi encerrada sem remover a outra. |
| Encerramento global | Nenhuma sessão permaneceu ativa. | O gerenciador removeu as sessões do usuário. |
| Validação após encerramento | A segunda sessão foi considerada inválida. | O acesso revogado deixou de ser reconhecido. |

### 7.2. Testes do Factory Method

A execução de `factory_method_autenticacao.py` demonstrou os seguintes comportamentos:

| Teste | Resultado observado | Interpretação |
|---|---|---|
| Login local com senha correta | Sucesso. | As credenciais de teste foram aceitas. |
| E-mail inexistente e senha incorreta | A mesma mensagem genérica foi apresentada. | A resposta não revelou diretamente a existência da conta. |
| Tentativas repetidas com senha incorreta | O acesso foi bloqueado temporariamente após o limite configurado. | A regra de bloqueio foi demonstrada. |
| Provedores sociais simulados | Tokens de teste válidos foram aceitos e um token inválido foi recusado. | Os autenticadores concretos executaram suas regras de demonstração. |
| Código de uso único correto | A autenticação foi concluída. | O fluxo de código temporário foi demonstrado. |
| Reutilização de código | A segunda tentativa foi recusada. | O código consumido não foi aceito novamente. |
| Registro de sessões | Foram consultadas as sessões ativas no gerenciador. | O Factory Method foi integrado ao Singleton. |

Os resultados confirmam os comportamentos exibidos nas execuções manuais realizadas pela equipe. Não constituem prova de resistência a ataques reais nem verificam automaticamente todos os cenários de concorrência ou falha.

## 8. Como Executar os Exemplos

Os exemplos podem ser executados localmente com Python 3.10 ou superior.

Os dois arquivos devem estar na mesma pasta, pois o módulo de autenticação importa `GerenciadorSessao` do arquivo `singleton_sessao.py`.

A partir da raiz do repositório, no terminal do VS Code, execute:

```powershell
cd docs/Base/Relatorios/subequipe01/codigo
python --version
python singleton_sessao.py
python factory_method_autenticacao.py
```

O primeiro comando de execução demonstra o comportamento do Singleton. O segundo demonstra os diferentes fluxos de autenticação e a integração com o gerenciamento de sessões.

O projeto de demonstração utiliza um repositório de usuários em memória e um canal de e-mail simulado. Os códigos de teste podem ser consultados apenas por mecanismos destinados à demonstração local. Esses componentes não devem ser expostos como serviços públicos em uma aplicação real.

O GitHub Pages hospeda a documentação estática, mas não executa automaticamente os programas Python.

## 9. Códigos-fonte

Os arquivos completos da implementação estão disponíveis no diretório `codigo`, localizado na pasta da subequipe.

### 9.1. Singleton — Gerenciador de Sessão

Arquivo responsável pela instância única e pelo gerenciamento das sessões.

**Caminho no repositório:** `docs/Base/Relatorios/subequipe01/codigo/singleton_sessao.py`

[Consultar singleton_sessao.py](./codigo/singleton_sessao.py)

### 9.2. Factory Method — Autenticação

Arquivo responsável pelos autenticadores, pelos criadores concretos e pela integração com o gerenciador de sessões.

**Caminho no repositório:** `docs/Base/Relatorios/subequipe01/codigo/factory_method_autenticacao.py`

[Consultar factory_method_autenticacao.py](./codigo/factory_method_autenticacao.py)

Os links relativos pressupõem que este documento esteja na pasta `docs/Base/Relatorios/subequipe01/`. Se o arquivo Markdown estiver em outro diretório, será necessário ajustar os caminhos.

## 10. Limitações e Considerações de Segurança

As implementações têm finalidade acadêmica e demonstrativa. Antes de sua utilização em uma aplicação real, seriam necessárias medidas adicionais.

- **Persistência:** os usuários, códigos e sessões são mantidos em memória. O estado é perdido ao encerrar o processo e não é compartilhado automaticamente entre servidores ou workers.
- **Provedores sociais:** Google, Facebook e Apple são simulados por tokens predefinidos. O código não valida tokens OAuth reais.
- **Envio de e-mails:** o canal utilizado é simulado e deve ser substituído por uma integração de envio de mensagens.
- **Sessões e navegador:** a revogação de um token impede seu uso quando o servidor valida a sessão. A prevenção de exibição de páginas em cache após o logout também depende das medidas aplicadas à camada web e aos cabeçalhos HTTP.
- **Proteção contra tentativas:** os limites de tentativa demonstrados são locais ao processo e não substituem controles distribuídos ou mecanismos adicionais contra abuso.
- **Testes:** as demonstrações de terminal devem ser complementadas por testes automatizados, incluindo cenários de expiração, concorrência, falha e reutilização de códigos.

Essas limitações não impedem a demonstração dos padrões GoF. Elas delimitam, porém, o que foi efetivamente implementado e o que ainda seria necessário para transformar o exemplo acadêmico em uma solução de produção.

## 11. Histórico de Versão

| Versão | Data | Descrição | Autor | Revisor |
|---|---|---|---|----|
| 1.0 | 16/10/2026 | Criação da página de implementação dos padrões Factory Method e Singleton. | Mariana Ribeiro ||
| 1.1 | 17/10/2026 | Inclusão de introdução, objetivos, integração entre os padrões, decisões de segurança e instruções de execução. | [Dylan Cavalcante](https://github.com/dylancavalcante)||
| 1.2 | 17/10/2026 | Reorganização da documentação, inclusão de trechos de código, registro dos testes e explicitação das limitações da implementação. | [Dylan Cavalcante](https://github.com/dylancavalcante) ||

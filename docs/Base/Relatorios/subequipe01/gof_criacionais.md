# Padrões GoF Criacionais: Factory Method e Singleton

**Subequipe 01 — Entrega 03**

- **Domínio:** Fluxo A (Gestão de Usuário, Autenticação e Perfil)
- **Sistema de referência:** DECATHLON
- **Tecnologia:** Python

## Divisão de Responsabilidades e Revisão em Pares

A tabela a seguir detalha a divisão de responsabilidades, o fluxo de co-criação síncrona e a revisão em pares (*peer review*) aplicados exclusivamente à construção deste artefato e de seu relatório.

| Etapa / Tópico do Relatório | Autor(a) Principal | Revisor(a) em Par | Evidência / Commit |
| :--- | :--- | :--- | :---: |
| 1. Introdução | A definir | A definir | A definir |
| 2. Objetivos (geral e específicos) | A definir | A definir | A definir |
| 3. Identificação dos Padrões GoF | A definir | A definir | A definir |
| 4. Padrão Factory Method: problema, solução, validação e segurança | A definir | A definir | A definir |
| 5. Padrão Singleton: problema, solução, validação e segurança | A definir | A definir | A definir |
| 6. Comparação e Integração entre os Padrões | A definir | A definir | A definir |
| 7. Limitações e *Trade-offs* | A definir | A definir | A definir |
| 8. Considerações Finais | A definir | A definir | A definir |
| Referências bibliográficas | A definir | A definir | A definir |

---

## 1. Introdução

A instanciação de objetos em sistemas orientados a objetos pode introduzir alto acoplamento quando não é adequadamente gerenciada. No contexto do Fluxo A do projeto, inspirado na plataforma DECATHLON, existem diferentes formas de autenticação e a necessidade de controlar de maneira consistente as sessões dos usuários.

Para lidar com esses desafios, esta documentação apresenta a aplicação de dois Padrões de Projeto Criacionais (*GoF*): **Factory Method** e **Singleton**.

O Factory Method organiza a criação dos diferentes autenticadores, reduzindo o acoplamento entre o fluxo de login e as implementações específicas. O Singleton centraliza o gerenciamento das sessões em uma única instância por processo Python, permitindo controlar sua criação, validação e encerramento.

## 2. Objetivos

### 2.1. Objetivo Geral

Demonstrar, documentar e validar a aplicação dos padrões criacionais Factory Method e Singleton no fluxo de autenticação e gestão de perfil, evidenciando suas contribuições para a organização arquitetural, a manutenção e a segurança do projeto.

### 2.2. Objetivos Específicos

- Explicar a finalidade de cada padrão no fluxo de autenticação e gerenciamento de sessões.
- Identificar os problemas arquiteturais e apresentar as classes envolvidas em suas soluções.
- Demonstrar o funcionamento dos padrões por meio de exemplos em Python e testes de execução.
- Analisar os mecanismos de segurança implementados e suas limitações.
- Avaliar as vantagens e os *trade-offs* das abordagens adotadas.

## 3. Identificação dos Padrões

A tabela a seguir sintetiza os padrões adotados, suas categorias, os problemas que resolvem e os exemplos de aplicação no projeto.

| Padrão | Categoria | Problema que resolve | Exemplo de aplicação |
| :--- | :--- | :--- | :--- |
| Factory Method | Criacional | Reduzir o acoplamento entre o fluxo principal de login e a criação de diferentes autenticadores. | `ServicoLogin`, `LoginLocal`, `LoginGoogle`, `LoginFacebook`, `LoginApple` e `LoginCodigoEmail`. |
| Singleton | Criacional | Evitar múltiplas instâncias independentes do gerenciador de sessões dentro do mesmo processo. | `GerenciadorSessao`, responsável pelo controle das sessões ativas. |

## 4. Padrão: Factory Method

### 4.1. Problema

O fluxo de autenticação do projeto contempla diferentes formas de acesso: e-mail e senha, provedores sociais (Google, Facebook e Apple) e código de uso único enviado por e-mail.

Se a classe principal de login instanciar diretamente cada autenticador, precisará conhecer os detalhes de implementação de todos eles. A inclusão de novos métodos de autenticação exigiria alterações frequentes nessa classe, aumentando o acoplamento e dificultando a manutenção.

Esse cenário contraria o **Princípio Aberto/Fechado (*Open/Closed Principle*)**, segundo o qual componentes devem permitir extensões sem exigir modificações constantes em sua estrutura existente.

### 4.2. Solução

O padrão Factory Method delega a criação dos autenticadores às subclasses de `ServicoLogin`. Cada classe concreta implementa `criar_autenticador()` e retorna o autenticador correspondente à modalidade de acesso. O método `realizar_login()` executa a autenticação sem precisar conhecer os detalhes internos de cada produto.

O trecho abaixo demonstra a estrutura central do padrão implementada em `factory_method_autenticacao.py`:

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

    def _iniciar_sessao(self, email: str) -> str:
        return GerenciadorSessao().criar_sessao(email)


class LoginLocal(ServicoLogin):
    def criar_autenticador(self) -> Autenticador:
        return AutenticadorLocal(self.banco)


class LoginGoogle(ServicoLogin):
    def criar_autenticador(self) -> Autenticador:
        return AutenticadorGoogle(self.banco)
```

**Funcionamento:** `ServicoLogin` define o fluxo comum, enquanto `LoginLocal` e `LoginGoogle` escolhem os produtos concretos. Após uma autenticação bem-sucedida, o serviço solicita ao Singleton a criação da sessão correspondente.

O mesmo mecanismo é utilizado pelas classes `LoginFacebook`, `LoginApple` e `LoginCodigoEmail`, permitindo adicionar outras modalidades sem concentrar todas as regras de criação em uma única classe.


**Fluxo resumido:**

1. O cliente seleciona uma modalidade de login.
2. O criador concreto instancia o autenticador correspondente.
3. O autenticador valida as credenciais.
4. Em caso de sucesso, o serviço solicita a criação da sessão.

**Principais componentes:**

- **Product:** `Autenticador`, interface comum dos autenticadores.
- **ConcreteProducts:** `AutenticadorLocal`, `AutenticadorGoogle`, `AutenticadorFacebook`, `AutenticadorApple` e `AutenticadorCodigoEmail`.
- **Creator:** `ServicoLogin`, responsável pelo fluxo comum de autenticação.
- **ConcreteCreators:** `LoginLocal`, `LoginGoogle`, `LoginFacebook`, `LoginApple` e `LoginCodigoEmail`.

A implementação está disponível no arquivo `factory_method_autenticacao.py`.

### 4.3. Validação e Segurança

A implementação contempla os seguintes mecanismos:

- **Enumeração de usuários:** o login local retorna a mensagem genérica `"E-mail ou senha inválidos."` tanto para e-mails inexistentes quanto para senhas incorretas.
- **Proteção contra tentativas repetidas:** após cinco tentativas incorretas, o login local bloqueia temporariamente novas tentativas por 15 minutos.
- **Armazenamento de senhas:** utiliza PBKDF2-HMAC-SHA256 com *salt* individual, evitando o armazenamento das senhas em texto puro.
- **Código de uso único:** o código enviado por e-mail possui validade de 15 minutos e não pode ser reutilizado após o consumo bem-sucedido.
- **Validação de credenciais sociais:** a estrutura possui autenticadores específicos para cada provedor. Na versão demonstrativa, a validação é simulada por meio de tokens previamente cadastrados.

Esses mecanismos demonstram decisões relevantes de segurança, embora a simulação dos provedores sociais e do envio de e-mails não substitua integrações reais.

## 5. Padrão: Singleton

### 5.1. Problema

Se diferentes módulos do sistema criarem gerenciadores de sessão independentes, cada instância poderá manter informações próprias sobre os acessos ativos. Isso pode provocar inconsistências no controle de sessões e dificultar a aplicação de operações como logout e revogação de acesso.

É necessário, portanto, centralizar o gerenciamento das sessões em uma única instância compartilhada dentro do processo da aplicação.

### 5.2. Solução

O padrão Singleton garante que as chamadas a `GerenciadorSessao()` retornem a mesma instância dentro do processo Python. A criação é controlada por `__new__()`, enquanto `threading.Lock` protege a inicialização contra condições de corrida.

A instância mantém as sessões em memória e oferece métodos para criar, validar e encerrar acessos, incluindo a expiração após 30 minutos de inatividade.

O trecho abaixo demonstra a estrutura central implementada em `singleton_sessao.py`:

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

    def criar_sessao(self, email: str) -> str:
        email_normalizado = self._normalizar_email(email)

        if not email_normalizado:
            raise ValueError("O e-mail é obrigatório.")

        agora = time.time()
        token = secrets.token_urlsafe(32)

        with self._lock_sessoes:
            self._remover_expiradas_usuario(email_normalizado, agora)

            self._sessoes.setdefault(email_normalizado, {})[token] = {
                "criada_em": agora,
                "ultima_atividade": agora,
                "expira_em": agora + TEMPO_INATIVIDADE_SEGUNDOS,
                "dispositivo": "dispositivo desconhecido",
            }

        return token
```

Para validar uma sessão, o gerenciador verifica se o token existe e se o prazo de expiração foi atingido. Quando a sessão ainda é válida, sua atividade pode renovar o período de inatividade:

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

**Funcionamento:** o método `__new__()` centraliza a criação da instância; `criar_sessao()` registra um token exclusivo com seu prazo de validade; e `validar_sessao()` verifica a validade do acesso, renovando o timeout quando apropriado. Os métodos `encerrar_sessao()` e `encerrar_todas()` complementam o controle, permitindo o logout individual e a revogação das sessões de um usuário.

**Fluxo resumido:**

1. Um módulo solicita o gerenciador de sessões.
2. O Singleton retorna a instância existente ou cria a primeira.
3. A sessão é registrada com um token e seu prazo de validade.
4. O sistema valida a sessão durante sua utilização e permite o encerramento do acesso.

A implementação está disponível no arquivo `singleton_sessao.py`.

### 5.3. Validação e Segurança

A implementação contempla os seguintes mecanismos:

- **Instância única:** duas chamadas a `GerenciadorSessao()` retornam o mesmo objeto.
- **Logout individual:** permite invalidar uma sessão específica sem encerrar as demais.
- **Revogação global:** permite encerrar todas as sessões de um usuário.
- **Expiração por inatividade:** a sessão deixa de ser válida após o período configurado sem atividade.
- **Concorrência:** a sincronização protege a criação da instância e as operações compartilhadas previstas na implementação.

Esses mecanismos permitem centralizar o controle das sessões. Entretanto, como o estado é mantido em memória, a instância não é automaticamente compartilhada entre processos ou servidores diferentes.

## 6. Comparação e Integração entre os Padrões

Os padrões atuam em etapas complementares do fluxo de autenticação. O Factory Method seleciona e cria o autenticador adequado, enquanto o Singleton gerencia a sessão após o sucesso da autenticação.

| Característica | Factory Method | Singleton |
| :--- | :--- | :--- |
| **Responsabilidade** | Delegar a criação dos diferentes autenticadores. | Centralizar o gerenciamento das sessões em uma instância por processo. |
| **Criação dos objetos** | Definida pelas subclasses concretas de `ServicoLogin`. | Controlada pela classe `GerenciadorSessao`, por meio de `__new__()`. |
| **Principal benefício** | Reduzir o acoplamento e facilitar a extensão dos métodos de login. | Evitar instâncias independentes do gerenciador de sessões dentro do processo. |
| **Aplicação no fluxo** | Criar o autenticador e executar a validação das credenciais. | Registrar, validar e encerrar as sessões após a autenticação. |

A integração ocorre quando `ServicoLogin.realizar_login()` recebe um resultado de autenticação bem-sucedido e solicita uma sessão ao `GerenciadorSessao`. Dessa forma, a lógica de autenticação permanece separada da lógica de gerenciamento de sessões.

## 7. Limitações

Apesar dos benefícios, os padrões escolhidos apresentam limitações que devem ser consideradas.

### 7.1. Factory Method

A criação de classes específicas para cada modalidade de autenticação aumenta a quantidade de código e de componentes do sistema. Embora essa separação favoreça a manutenção, pode representar complexidade adicional em projetos pequenos.

Além disso, os provedores sociais e o envio de códigos por e-mail são simulados na implementação demonstrativa. Para uso real, seriam necessárias integrações com serviços externos e validações apropriadas.

### 7.2. Singleton

O Singleton introduz um estado compartilhado, o que pode aumentar o acoplamento entre os módulos que dependem do gerenciador e dificultar testes isolados.

A implementação também mantém as sessões em memória. Assim, processos Python distintos não compartilham automaticamente o mesmo estado, e as sessões não persistem após o encerramento do processo. Sistemas distribuídos exigiriam uma solução de armazenamento compartilhado e mecanismos adicionais de sincronização.

## 8. Considerações Finais

A aplicação dos padrões Factory Method e Singleton no Fluxo A contribui para organizar a autenticação e o gerenciamento de sessões de maneira modular.

O Factory Method separa a criação dos autenticadores do fluxo principal de login, facilitando a inclusão de novas modalidades. O Singleton centraliza o controle das sessões dentro do processo, disponibilizando operações para criação, validação e encerramento dos acessos.

Os exemplos e testes demonstram o funcionamento das estruturas propostas e permitem observar seus benefícios e limitações. Entretanto, a implementação deve ser compreendida como uma demonstração acadêmica: integrações reais com provedores sociais, envio de e-mails e compartilhamento de sessões entre servidores exigiriam componentes adicionais.

Assim, os dois padrões contribuem para uma arquitetura mais organizada, desde que suas responsabilidades e limitações sejam consideradas no desenvolvimento do sistema.

---

## Referências

SERRANO, Milene. *Arquitetura e Desenho de Software: Aula GoFs Criacionais*. Universidade de Brasília (UnB Gama).

REFACTORING.GURU. [Design Patterns: Factory Method](https://refactoring.guru/design-patterns/factory-method).

REFACTORING.GURU. [Design Patterns: Singleton](https://refactoring.guru/design-patterns/singleton).

SUBEQUIPE 01. *Documentação de Implementação* (`implementacao.md`).

SUBEQUIPE 01. *Documentação de Modelagem* (`modelagem.md`).

---

## Histórico de Versão

| Versão | Data | Descrição | Autor | Revisor |
| :---: | :---: | :--- | :--- | :--- |
| 0.1 | 17/10/2026 | Estruturação inicial da documentação dos padrões Factory Method e Singleton. |  [Dylan Cavalcante](https://github.com/dylancavalcante) | A definir |
| 0.2 | 17/10/2026 | Revisão textual, síntese das seções de solução e alinhamento da documentação com a implementação demonstrativa. | [Dylan Cavalcante](https://github.com/dylancavalcante) | A definir |

# Implementação: Factory Method e Singleton

## Visão geral

A subequipe 01 aplicou dois padrões GoF criacionais ao Fluxo A (gestão de usuário, autenticação e perfil):

| Padrão | Onde foi aplicado | Arquivo |
|---|---|---|
| Factory Method | Criação do autenticador para cada forma de login | `factory_method_autenticacao.py` |
| Singleton | Gerenciador único das sessões ativas | `singleton_sessao.py` |

Os dois trabalham juntos: o Factory Method autentica o usuário e, quando o login é aprovado, a sessão é registrada no Singleton.

## Factory Method: autenticação

### Problema

O usuário pode entrar com e-mail e senha, Google, Facebook, Apple ou um código de uso único enviado por e-mail. Cada forma de login tem sua própria validação, mas o fluxo de autenticar e iniciar a sessão é igual para todas. Sem o padrão, esse fluxo precisaria de uma cadeia de `if/elif` que teria que ser editada a cada novo tipo de login.

### Papéis do padrão no código

| Papel no padrão | Classe no projeto | Responsabilidade |
|---|---|---|
| Creator | `ServicoLogin` | Declara o factory method `criar_autenticador()` e implementa o fluxo comum em `realizar_login()` |
| ConcreteCreator | `LoginLocal`, `LoginGoogle`, `LoginFacebook`, `LoginApple`, `LoginCodigoEmail` | Cada um sobrescreve `criar_autenticador()` e decide qual autenticador criar |
| Product | `Autenticador` | Interface comum com o método `autenticar()` |
| ConcreteProduct | `AutenticadorLocal`, `AutenticadorGoogle`, `AutenticadorFacebook`, `AutenticadorApple`, `AutenticadorCodigoEmail` | Implementam a validação específica de cada forma de login |

O método `realizar_login()` não sabe qual autenticador concreto está usando. Para adicionar uma nova forma de login, basta criar um novo par ConcreteCreator e ConcreteProduct, sem alterar as classes existentes (princípio aberto/fechado).

### Decisões de segurança

As regras abaixo vieram da análise do fluxo de login da Decathlon, usada como referência pelo grupo:

| Ponto observado na Decathlon | Decisão no nosso projeto |
|---|---|
| O login informa "Conta não encontrada", permitindo descobrir quais e-mails têm conta (enumeração de usuários) | Mensagem genérica "E-mail ou senha inválidos." tanto para e-mail inexistente quanto para senha errada |
| Cinco senhas erradas seguidas não geraram bloqueio | Bloqueio temporário de 15 minutos após 5 tentativas, contado também para e-mails inexistentes |
| Senha de uso único enviada por e-mail, válida por 15 minutos | Código de 6 dígitos, válido por 15 minutos e descartado após o primeiro uso |
| Login social com Google, Facebook e Apple | Um autenticador para cada provedor, com a classe base comum `AutenticadorSocial` |

Além disso, as senhas são armazenadas com hash PBKDF2 e salt, nunca em texto puro.

## Singleton: gestão de sessão

### Problema

As sessões ativas precisam ficar em um único registro compartilhado pelo sistema. Se cada parte do código criasse seu próprio gerenciador, um logout feito em um lugar não seria visto pelos outros, e a sessão continuaria válida.

### Papel do padrão no código

| Papel no padrão | Classe no projeto | Responsabilidade |
|---|---|---|
| Singleton | `GerenciadorSessao` | Garante uma única instância (controlada em `__new__`) e oferece acesso global às sessões ativas |

A criação da instância é protegida por uma trava (`threading.Lock`), para que duas requisições simultâneas não criem dois gerenciadores.

### Decisões de segurança

| Ponto observado na Decathlon | Decisão no nosso projeto |
|---|---|
| Teste de logout: após sair, o botão "Voltar" não deve reabrir o perfil | O logout apaga o token da sessão no servidor, então voltar no navegador não recupera o acesso |
| "Alterar sua palavra-passe irá desconectar todos os seus serviços" | `encerrar_todas()` desconecta o usuário de todos os dispositivos |
| Menu "Dispositivos conectados" | `dispositivos_conectados()` lista as sessões ativas do usuário |

As sessões também expiram após 30 minutos sem uso.

## Integração entre os padrões

Depois que qualquer autenticador aprova o login, o método `_iniciar_sessao()` do `ServicoLogin` chama `GerenciadorSessao().criar_sessao()`. Assim, todas as formas de login registram a sessão no mesmo lugar.


## Código-fonte

### Factory Method

Arquivo: `docs/Base/Relatorios/subequipe01/codigo/factory_method_autenticacao.py`

[factory_method_autenticacao.py](/Base/Relatorios/subequipe01/codigo/factory_method_autenticacao.py ':include :type=code python')

### Singleton

Arquivo: `docs/Base/Relatorios/subequipe01/codigo/singleton_sessao.py`

[singleton_sessao.py](/Base/Relatorios/subequipe01/codigo/singleton_sessao.py ':include :type=code python')

## Histórico de versão

| Versão | Data | Descrição | Autor |
|---|---|---|---|
| 1.0 | 16/10/2026 | Criação da página de implementação do Factory Method e do Singleton | Mariana Ribeiro |
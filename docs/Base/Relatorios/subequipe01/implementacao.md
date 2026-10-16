# Implementação: Factory Method

## Visão geral

O Factory Method foi aplicado ao fluxo de autenticação do e-commerce. O usuário pode entrar com e-mail e senha, Google, Facebook, Apple ou com um código de uso único enviado por e-mail. Cada forma de login tem sua própria validação, mas o fluxo de autenticar e iniciar a sessão é o mesmo para todas.

## Papéis do padrão no código

| Papel no padrão | Classe no projeto | Responsabilidade |
|---|---|---|
| Creator | `ServicoLogin` | Declara o factory method `criar_autenticador()` e implementa o fluxo comum em `realizar_login()` |
| ConcreteCreator | `LoginLocal`, `LoginGoogle`, `LoginFacebook`, `LoginApple`, `LoginCodigoEmail` | Cada um sobrescreve `criar_autenticador()` e decide qual autenticador criar |
| Product | `Autenticador` | Interface comum com o método `autenticar()` |
| ConcreteProduct | `AutenticadorLocal`, `AutenticadorGoogle`, `AutenticadorFacebook`, `AutenticadorApple`, `AutenticadorCodigoEmail` | Implementam a validação específica de cada forma de login |

O método `realizar_login()` não sabe qual autenticador concreto está usando. Para adicionar uma nova forma de login, basta criar um novo par ConcreteCreator e ConcreteProduct, sem alterar as classes existentes (princípio aberto/fechado).

## Decisões de segurança

As regras abaixo vieram da análise do fluxo de login da Decathlon, usada como referência pelo grupo:

| Ponto observado na Decathlon | Decisão no nosso projeto |
|---|---|
| O login informa "Conta não encontrada", permitindo descobrir quais e-mails têm conta (enumeração de usuários) | Mensagem genérica "E-mail ou senha inválidos." tanto para e-mail inexistente quanto para senha errada |
| Cinco senhas erradas seguidas não geraram bloqueio | Bloqueio temporário de 15 minutos após 5 tentativas, contado também para e-mails inexistentes |
| Senha de uso único enviada por e-mail, válida por 15 minutos | Código de 6 dígitos, válido por 15 minutos e descartado após o primeiro uso |
| Login social com Google, Facebook e Apple | Um autenticador para cada provedor, com a classe base comum `AutenticadorSocial` |

Além disso, as senhas são armazenadas com hash PBKDF2 e salt, nunca em texto puro.

## Código-fonte

Arquivo: `docs/Base/Relatorios/subequipe01/codigo/factory_method_autenticacao.py`

[factory_method_autenticacao.py](/Base/Relatorios/subequipe01/codigo/factory_method_autenticacao.py ':include :type=code python')

## Histórico de versão

| Versão | Data | Descrição | Autor |
|---|---|---|---|
| 1.0 | 16/10/2026 | Criação da página de implementação do Factory Method | Mariana Ribeiro |
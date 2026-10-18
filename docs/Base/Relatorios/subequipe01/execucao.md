# Execução e Testes: Factory Method e Singleton

**Subequipe 01 — Entrega 03**

- **Domínio:** Fluxo A — Gestão de Usuário, Autenticação e Perfil
- **Sistema de referência:** DECATHLON
- **Tecnologia:** Python 3.10+
- **Arquivos:** `factory_method_autenticacao.py`, `singleton_sessao.py` e `test_gof.py`

## Divisão de Responsabilidades e Revisão em Pares

A tabela a seguir apresenta a divisão de responsabilidades, a revisão em pares (*peer review*) e as evidências de contribuição relacionadas à documentação e à validação dos padrões Factory Method e Singleton.

| Etapa / Tópico do Relatório | Autor(a) Principal | Revisor(a) em Par | Evidência / Commit |
| :--- | :--- | :--- | :---: |
| 1. Introdução | A definir | A definir | A definir |
| 2. Pré-requisitos e estrutura dos arquivos | A definir | A definir | A definir |
| 3.1. Execução manual do Singleton | A definir | A definir | A definir |
| 3.2. Execução manual do Factory Method | A definir | A definir | A definir |
| 4. Testes automatizados com `unittest` | A definir | A definir | A definir |
| 5. Resultado da suíte de testes | A definir | A definir | A definir |
| 6. Critérios de Validação | A definir | A definir | A definir |
| 7. Limitações da Demonstração | A definir | A definir | A definir |
| 8. Evidências de execução e capturas do terminal | A definir | A definir | A definir |
| 9. Considerações Finais | A definir | A definir | A definir |
| 10. Referências bibliográficas | A definir | A definir | A definir |
| 11. Histórico de versões e revisão documental | A definir | A definir | A definir |

## 1. Introdução

Esta página documenta como executar a demonstração dos padrões criacionais Factory Method e Singleton e como verificar seus comportamentos por meio de testes automatizados.

O projeto utiliza apenas bibliotecas da biblioteca padrão do Python. O arquivo `factory_method_autenticacao.py` contém os autenticadores e os criadores concretos; `singleton_sessao.py` contém o gerenciador de sessões; e `test_gof.py` reúne testes automatizados usando `unittest`.

A implementação tem finalidade acadêmica. Usuários e sessões são mantidos em memória, o canal de e-mail é simulado e os provedores sociais utilizam tokens predefinidos. Os testes verificam o comportamento do código demonstrativo, mas não equivalem a uma auditoria de segurança de um sistema em produção.

## 2. Pré-requisitos e estrutura dos arquivos

É necessário ter Python 3.10 ou superior instalado. Não é necessário instalar dependências com `pip`.

Mantenha os três arquivos na mesma pasta:

```text
codigo/
├── factory_method_autenticacao.py
├── singleton_sessao.py
└── test_gof.py
```

Verifique a versão do Python:

```powershell
python --version
```

Em alguns ambientes, o comando pode ser `python3` ou `py -3`.

## 3. Executar as demonstrações manuais

Os scripts também possuem blocos `if __name__ == "__main__":` para exibir exemplos de uso.

### 3.1. Singleton

```powershell
python singleton_sessao.py
```

A demonstração apresenta a comparação entre duas chamadas ao construtor, a criação de sessões e os procedimentos de logout individual e global. Os identificadores de memória variam a cada execução; o comportamento relevante é que os dois objetos sejam a mesma instância.

### 3.2. Factory Method

```powershell
python factory_method_autenticacao.py
```

A demonstração percorre login local, falhas de autenticação, bloqueio temporário, provedores sociais simulados e código de uso único por e-mail. Os tokens sociais e o envio de e-mail são simulações locais, não integrações externas reais.

## 4. Executar os testes automatizados

Com os três arquivos na mesma pasta, execute:

```powershell
python -m unittest -v test_gof.py
```

O módulo `unittest` faz parte da biblioteca padrão do Python, portanto não é necessário instalar `pytest` ou outra dependência.

A suíte verifica:

- se os criadores do Factory Method fornecem os autenticadores concretos esperados;
- se um login local válido cria uma sessão utilizável;
- se e-mail inexistente e senha incorreta recebem a mesma mensagem genérica;
- se cinco falhas consecutivas ativam o bloqueio temporário;
- se tokens sociais de teste válidos e inválidos são tratados de maneira diferente;
- se o código de e-mail não é consumido por uma tentativa incorreta, expira, respeita o limite de tentativas e não pode ser reutilizado;
- se apenas uma de várias tentativas concorrentes consegue consumir um mesmo código;
- se o Singleton mantém uma única instância e permite criar, validar, consultar e encerrar sessões;
- se as sessões expiram conforme o tempo configurado e se a criação concorrente mantém tokens distintos.

Para manter os testes rápidos, `test_gof.py` reduz temporariamente o número de iterações do PBKDF2 durante a suíte. Essa configuração existe exclusivamente nos testes e não altera o valor utilizado pela execução normal de `factory_method_autenticacao.py`.

## 5. Resultado da suíte de referência

Na execução de referência realizada com os arquivos desta entrega, o comando de testes apresentou:

```text
Ran 18 tests
OK
```

Esse resultado registra a execução realizada durante a preparação destes arquivos. A equipe deve repetir o comando no repositório local e conferir a saída no próprio ambiente antes de registrar a validação final do projeto.

Os testes demonstram comportamentos específicos do código. Não comprovam, isoladamente, resistência a ataques reais, integração com OAuth, entrega real de e-mails ou compartilhamento de sessões entre processos distintos.

## 6. Critérios de Validação

A validação considera os comportamentos observáveis dos padrões, as regras de autenticação e o gerenciamento das sessões. A tabela relaciona cada critério à forma de verificação correspondente no arquivo `test_gof.py` ou à inspeção do código-fonte.

| Identificador | Critério | Forma de verificação | Evidência no projeto |
| :---: |---|---|---|
| CV01 | Delegação do Factory Method. | Verificar se cada criador concreto fornece o autenticador correspondente. | `test_factory_method_cria_autenticadores_corretos` |
| CV02 | Unicidade do Singleton. | Confirmar que chamadas sucessivas ao construtor retornam a mesma instância. | `test_duas_chamadas_retornam_mesma_instancia` |
| CV03 | Tratamento de credenciais inválidas e bloqueio temporário. | Comparar as respostas para e-mail inexistente e senha incorreta; verificar o bloqueio após cinco falhas. | `test_email_inexistente_e_senha_errada_usam_mensagem_generica`; `test_bloqueia_apos_cinco_falhas_e_recusa_senha_correta` |
| CV04 | Autenticação social demonstrativa. | Verificar a aceitação de tokens de teste válidos e a recusa de tokens inválidos. | `test_login_social_simulado_aceita_token_valido`; `test_login_social_simulado_recusa_token_invalido` |
| CV05 | Código de e-mail de uso único. | Verificar resposta neutra, consumo após sucesso, rejeição de código expirado, limite de tentativas e concorrência na utilização. | Testes de código de e-mail em `test_gof.py` |
| CV06 | Criação, validação e encerramento de sessões. | Verificar a criação e validação de uma sessão, logout individual e revogação de todas as sessões do usuário. | `test_criar_validar_e_encerrar_sessao`; `test_encerrar_todas_remove_sessoes_do_usuario` |
| CV07 | Expiração por inatividade. | Verificar se uma sessão deixa de ser válida após o prazo configurado. | `test_sessao_expira_apos_timeout_configurado` |
| CV08 | Operações concorrentes. | Verificar a consistência da criação de sessões e o consumo de um código por apenas uma tentativa concorrente. | `test_criacao_concorrente_gera_tokens_distintos_e_consistentes`; `test_apenas_uma_de_varias_tentativas_concorrentes_consume_codigo` |
| CV09 | Proteção dos dados de sessão. | Conferir que a consulta de sessões ativas não expõe tokens e que os métodos de logout invalidam os tokens correspondentes. | `test_sessoes_ativas_nao_expoe_tokens` e testes de sessão |
| CV10 | Derivação de hash de senha. | Inspecionar a implementação para confirmar PBKDF2-HMAC-SHA256 e *salt* individual, sem armazenar a senha em texto puro. | `gerar_hash()` e `BancoUsuarios.cadastrar()` em `factory_method_autenticacao.py` |

Os testes automatizados fornecem evidências reproduzíveis dos comportamentos cobertos. A inspeção do código é utilizada quando o critério diz respeito à estrutura da implementação, e não apenas a uma saída observável. A execução da suíte deve ser repetida no ambiente da equipe antes de registrar os resultados finais da entrega.

## 7. Limitações da Demonstração

| Aspecto | Limitação |
|---|---|
| Persistência | Usuários, códigos e sessões permanecem em memória e são perdidos quando o processo termina. |
| Escopo do Singleton | A instância é compartilhada dentro de um processo Python, não automaticamente entre processos ou servidores diferentes. |
| Provedores sociais | Google, Facebook e Apple são representados por tokens de teste; não há validação de tokens OAuth reais. |
| Envio de e-mails | O canal utilizado é simulado e não envia mensagens por SMTP ou por outro serviço real. |
| Bloqueio de tentativas | O contador de falhas é local ao processo e não substitui controles distribuídos de limitação de taxa. |
| Segurança em produção | Os testes automatizados não equivalem a uma auditoria de segurança nem comprovam resistência a ataques reais. |

Essas limitações não impedem a demonstração dos padrões GoF. Elas delimitam o alcance da implementação acadêmica e indicam os componentes adicionais que seriam necessários em um ambiente de produção.

## 8. Evidências de execução

Após executar os comandos no computador da equipe, acrescentar capturas reais do terminal.

### 8.1. Demonstração do Singleton

**Figura 1 — Execução de `singleton_sessao.py`.**

[Inserir captura real do terminal.]

*Fonte: elaborada pela equipe, 2026.*

### 8.2. Demonstração do Factory Method

**Figura 2 — Execução de `factory_method_autenticacao.py`.**

[Inserir captura real do terminal.]

*Fonte: elaborada pela equipe, 2026.*

### 8.3. Testes automatizados

**Figura 3 — Resultado da execução de `python -m unittest -v test_gof.py`.**

[Inserir captura real do terminal com os testes e o resumo final.]

*Fonte: elaborada pela equipe, 2026.*

## 9. Conclusão

A inclusão de testes automatizados complementa as demonstrações manuais e permite verificar comportamentos de maneira repetível. A suíte cobre os principais caminhos implementados para os dois padrões: criação descentralizada de autenticadores, validação das credenciais, uso único de códigos e gerenciamento de sessões.

Os resultados devem ser interpretados dentro do escopo da solução acadêmica. A implementação não substitui integrações reais com provedores sociais e serviços de e-mail, nem oferece compartilhamento de estado entre servidores. Para produção, seriam necessários armazenamento persistente e compartilhado, controles de segurança adicionais e testes de integração específicos.

## 10. Referências

GAMMA, Erich et al. *Design Patterns: Elements of Reusable Object-Oriented Software*. Boston: Addison-Wesley, 1994.

REFACTORING.GURU. [Design Patterns: Factory Method](https://refactoring.guru/design-patterns/factory-method).

REFACTORING.GURU. [Design Patterns: Singleton](https://refactoring.guru/design-patterns/singleton).

SUBEQUIPE 01. *Documentação de Implementação* (`implementacao.md`).

SUBEQUIPE 01. *Documentação de Modelagem* (`modelagem.md`).

## 11. Histórico de versões

| Versão | Data | Descrição | Autor(es) | Revisor(es) |
| :---: | :---: |---|---|---|
| 1.0 | 18/10/2026 | Estruturação inicial dos procedimentos de execução. |[Dylan Cavalcante](https://github.com/dylancavalcante) | A definir |
| 1.1 | 18/10/2026 | Inclusão de testes automatizados com `unittest`, critérios de validação e documentação explícita das limitações. | [Dylan Cavalcante](https://github.com/dylancavalcante) | A definir |

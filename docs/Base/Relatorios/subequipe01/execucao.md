# Execução e Testes: Factory Method e Singleton

**Subequipe 01 — Entrega 03**

- **Domínio:** Fluxo A — Gestão de Usuário, Autenticação e Perfil
- **Sistema de referência:** DECATHLON
- **Tecnologia:** Python 3.10 ou superior
- **Arquivos:** `factory_method_autenticacao.py`, `singleton_sessao.py` e `test_gof.py`

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

## 6. Critérios e limitações

| Critério | Como verificar | Limitação conhecida |
|---|---|---|
| Factory Method | Confirmar por teste que cada criador retorna o autenticador correto. | Os provedores externos são simulados. |
| Singleton | Confirmar que chamadas ao construtor retornam a mesma instância no processo. | Processos Python diferentes não compartilham a memória. |
| Login local | Verificar a senha correta, as respostas genéricas e o bloqueio após falhas. | O repositório e o contador de falhas são mantidos em memória. |
| Código de e-mail | Verificar expiração, limite de tentativas, uso único e concorrência. | O canal de envio é simulado; códigos não são enviados por SMTP real. |
| Sessões | Verificar criação, renovação, expiração, logout individual e logout global. | A validade só é aplicada a recursos que chamem `validar_sessao()` no servidor. |
| Concorrência | Executar os cenários concorrentes da suíte. | A sincronização local não fornece coordenação distribuída entre servidores. |

O Factory Method e o Singleton estão efetivamente representados na implementação: `ServicoLogin` define o método fábrica abstrato `criar_autenticador()`, e os criadores concretos o sobrescrevem; `GerenciadorSessao.__new__()` reutiliza uma única instância por processo, protegendo sua inicialização com `threading.Lock`. As operações de sessão usam `threading.RLock` para sincronizar o estado compartilhado.

## 7. Evidências de execução

Após executar os comandos no computador da equipe, acrescentar capturas reais do terminal.

### 7.1. Demonstração do Singleton

**Figura 1 — Execução de `singleton_sessao.py`.**

[Inserir captura real do terminal.]

*Fonte: elaborada pela equipe, 2026.*

### 7.2. Demonstração do Factory Method

**Figura 2 — Execução de `factory_method_autenticacao.py`.**

[Inserir captura real do terminal.]

*Fonte: elaborada pela equipe, 2026.*

### 7.3. Testes automatizados

**Figura 3 — Resultado da execução de `python -m unittest -v test_gof.py`.**

[Inserir captura real do terminal com os testes e o resumo final.]

*Fonte: elaborada pela equipe, 2026.*

## 8. Conclusão

A inclusão de testes automatizados complementa as demonstrações manuais e permite verificar comportamentos de maneira repetível. A suíte cobre os principais caminhos implementados para os dois padrões: criação descentralizada de autenticadores, validação das credenciais, uso único de códigos e gerenciamento de sessões.

Os resultados devem ser interpretados dentro do escopo da solução acadêmica. A implementação não substitui integrações reais com provedores sociais e serviços de e-mail, nem oferece compartilhamento de estado entre servidores. Para produção, seriam necessários armazenamento persistente e compartilhado, controles de segurança adicionais e testes de integração específicos.

## 9. Referências

GAMMA, Erich et al. *Design Patterns: Elements of Reusable Object-Oriented Software*. Boston: Addison-Wesley, 1994.

REFACTORING.GURU. [Design Patterns: Factory Method](https://refactoring.guru/design-patterns/factory-method).

REFACTORING.GURU. [Design Patterns: Singleton](https://refactoring.guru/design-patterns/singleton).

SUBEQUIPE 01. *Documentação de Implementação* (`implementacao.md`).

SUBEQUIPE 01. *Documentação de Modelagem* (`modelagem.md`).

## 10. Histórico de versões

| Versão | Data | Descrição | Autor(es) | Revisor(es) |
| :---: | :---: |---|---|---|
| 1.0 | 18/10/2026 | Estruturação inicial dos procedimentos de execução. | A definir | A definir |
| 1.1 | 18/10/2026 | Inclusão de testes automatizados com `unittest`, critérios de validação e documentação explícita das limitações. | A definir | A definir |

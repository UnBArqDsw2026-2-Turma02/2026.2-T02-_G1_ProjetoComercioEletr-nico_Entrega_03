# Contribuindo

Obrigado por contribuir com o projeto **Alltletic** (e-commerce com ênfase em Design Centrado no Usuário e Usabilidade/UX), desenvolvido pelo Grupo 01 da Turma 02 de Arquitetura e Desenho de Software (UnB, 2026.2).

Este repositório reúne a documentação (publicada via Docsify no GitHub Pages), o código-fonte e os testes das três subequipes. Leia também o [Código de Conduta](CODE_OF_CONDUCT.md).

## Estrutura do repositório

| Pasta | Conteúdo |
| ----- | -------- |
| `docs/` | Documentação publicada no GitHub Pages. O menu lateral fica em `docs/_sidebar.md`. |
| `src/` | Código-fonte do projeto. |
| `tests/` | Testes, organizados por subequipe (ex.: `tests/subequipe02`). |
| `.github/` | Configurações do GitHub (templates, workflows). |

## Fluxo de trabalho

1. **Escolha uma issue.** Cada subequipe tem seus épicos e tarefas, identificados pelas labels `subequipe-01`, `subequipe-02` e `subequipe-03`. Se for trabalhar em algo sem issue, abra uma antes e descreva o que pretende fazer.
2. **Avise o grupo.** Comente na issue que você vai assumi-la, para evitar duas pessoas fazendo a mesma coisa.
3. **Crie uma branch a partir da `main` atualizada**, com um nome que diga o que ela faz:
   - `docs/subequipe03-execucao`
   - `feat/checkout-strategy`
   - `fix/sidebar-link-ia-equipe03`
4. **Faça commits pequenos e claros** (veja o padrão abaixo).
5. **Abra um Pull Request para a `main`**, vinculando a issue (`Closes #número`) e marcando o que foi feito na lista de tarefas do PR.
6. **Peça revisão** a pelo menos uma pessoa da sua subequipe. Resolva os comentários na própria branch.
7. **Faça o merge somente após a aprovação.** Quem abriu o PR não aprova o próprio trabalho.

Não faça commits diretamente na `main`.

## Padrão de commits

Use o formato `tipo: descrição curta no imperativo`.

| Tipo | Quando usar |
| ---- | ----------- |
| `feat` | Nova funcionalidade ou novo documento/seção |
| `fix` | Correção de erro (código, link quebrado, texto) |
| `docs` | Mudanças apenas em documentação |
| `test` | Criação ou ajuste de testes |
| `refactor` | Reorganização sem mudar comportamento |
| `chore` | Configuração, `.gitignore`, ajustes de rotina |

Exemplos:

```
docs: adiciona relatório de execução da subequipe 03
fix: corrige link de IA generativa no menu lateral
test: adiciona testes do padrão Strategy no checkout
```

## Documentação (Docsify)

- Cada página nova deve ser adicionada ao `docs/_sidebar.md`.
- Use caminhos relativos à pasta `docs/`, **sem** o prefixo `/docs` (ex.: `/Base/Relatorios/...`).
- Siga a organização existente: `Base/Relatorios/<subequipe>/`.
- Imagens e diagramas ficam junto da documentação, com nomes descritivos e sem espaços.
- Antes de abrir o PR, confira que os links do menu abrem e que a página renderiza corretamente.

## Código e testes

- Mantenha o código dentro da pasta indicada pela sua subequipe e do fluxo em que ela atua.
- Todo padrão de projeto implementado deve ter evidência de execução ou teste correspondente, registrada na documentação de execução da subequipe.
- Não versione arquivos de editor ou de ferramentas (`.vscode/`, `.metals/`, dependências, builds). Eles devem estar no `.gitignore`.

## Pull Requests

Um bom PR:

- tem título claro e descreve o que mudou e por quê;
- referencia a issue relacionada;
- é pequeno o bastante para ser revisado em poucos minutos;
- não mistura assuntos diferentes (documentação, código e configuração em PRs separados, sempre que possível).

## Gestão

- Use as **milestones** (M1 a M4) para indicar a fase da entrega.
- Use as labels `epico`, `geral`, `documentacao` e `subequipe-0X` para organizar o trabalho.
- Dúvidas ou bloqueios devem ser registrados como comentário na issue correspondente, para que todo o grupo acompanhe.
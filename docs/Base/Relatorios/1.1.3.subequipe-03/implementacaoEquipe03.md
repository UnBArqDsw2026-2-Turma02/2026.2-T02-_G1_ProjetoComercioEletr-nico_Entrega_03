# Implementação do Padrão Strategy

## 1. Introdução
Esta página documenta a aplicação prática do padrão comportamental **Strategy** no ecossistema do e-commerce da Sub-equipe 03. O foco do modelo é o fluxo de checkout e finalização de compras, permitindo a seleção flexível e dinâmica de diferentes meios de pagamento (Pix, Cartão de Crédito e Boleto Bancário).

## 2. Padrão de Projeto Utilizado
O **Strategy** permite definir uma família de algoritmos, encapsular cada um deles e torná-los intercambiáveis em tempo de execução.

### Benefícios no Projeto:
- **Baixo Acoplamento:** A classe `Pedido` depende apenas da interface abstrata `FormaPagamento`.
- **Princípio do Aberto/Fechado:** Para adicionar um novo meio de pagamento no futuro, basta criar uma nova classe sem alterar a classe `Pedido`.

---

## 3. Estrutura e Links do Código-Fonte

Os componentes estão organizados no repositório oficial da seguinte forma:

### 3.1. Interface Strategy (O Contrato)
Define o contrato padrão para todas as estratégias de pagamento.
* [`FormaPagamento.java`](https://github.com/UnBArqDsw2026-2-Turma02/2026.2-T02-_G1_ProjetoComercioEletr-nico_Entrega_03/blob/main/src/subequipe03/ecommerce/FormaPagamento.java)

### 3.2. Classes Concretas (As Estratégias)
Implementações específicas de cada meio de pagamento:
* [`PagamentoPix.java`](https://github.com/UnBArqDsw2026-2-Turma02/2026.2-T02-_G1_ProjetoComercioEletr-nico_Entrega_03/blob/main/src/subequipe03/ecommerce/PagamentoPix.java) — Lógica de cobrança e validação via Pix.
* [`PagamentoBoleto.java`](https://github.com/UnBArqDsw2026-2-Turma02/2026.2-T02-_G1_ProjetoComercioEletr-nico_Entrega_03/blob/main/src/subequipe03/ecommerce/PagamentoBoleto.java) — Gerador de boleto bancário e linha digitável.
* [`PagamentoCartaoCredito.java`](https://github.com/UnBArqDsw2026-2-Turma02/2026.2-T02-_G1_ProjetoComercioEletr-nico_Entrega_03/blob/main/src/subequipe03/ecommerce/PagamentoCartaoCredito.java) — Processamento de compras parceladas no cartão.

### 3.3. Classes de Contexto e Execução
* [`Pedido.java`](https://github.com/UnBArqDsw2026-2-Turma02/2026.2-T02-_G1_ProjetoComercioEletr-nico_Entrega_03/blob/main/src/subequipe03/ecommerce/Pedido.java) — Classe de contexto que consome o Strategy sem acoplamento.
* [`Main.java`](https://github.com/UnBArqDsw2026-2-Turma02/2026.2-T02-_G1_ProjetoComercioEletr-nico_Entrega_03/blob/main/src/subequipe03/ecommerce/Main.java) — Ponto de entrada que simula o fluxo completo de compra e execução no terminal.

---

## 4. Repositório Oficial
* [Acessar a pasta com todos os arquivos no GitHub](https://github.com/UnBArqDsw2026-2-Turma02/2026.2-T02-_G1_ProjetoComercioEletr-nico_Entrega_03/tree/main/src/subequipe03/ecommerce)

---

## 5. Vídeo de Demonstração e Execução

O vídeo a seguir apresenta a execução prática do projeto no fluxo de checkout de compras.

---

<div align="center">
  <iframe width="560" height="315" src="https://youtu.be/iyV-XuSEjN4" title="Execução do Checkout com Padrão de Projeto Strategy, com explicações de Letícia e Rafaela." frameborder="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture" allowfullscreen></iframe>
</div>

---

> **Não consegue visualizar o player?** [Clique aqui para assistir ao vídeo diretamente no YouTube](https://youtu.be/iyV-XuSEjN4).

---
> **Histórico de Versões**
> 
> | Versão | Data | Descrição | Autores |
> | :---: | :---: | :--- | :--- |
> | `0.1` | 19/10/2026 | Criação da página e postagem do conteúdo | [Rafaela Andrea](https://github.com/radamesGuerra) |


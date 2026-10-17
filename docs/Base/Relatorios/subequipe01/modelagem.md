# Modelagem: Factory Method e Singlethon

## Introdução/Visão Geral

Após escolher os métodos de implementação, o subgrupo 1 desenvolveu a modelagem UML sobre o Factory Method e o Singlethon baseado no Fluxo A (Gestão de usuário, autenticação e perfil),

| Padrão | Onde foi aplicado |
|---|---|
| Factory Method | Criação do autenticador para cada forma de login |
| Singleton | Gerenciador |

Os dois trabalham juntos: o Factory Method autentica o usuário e, quando o login é aprovado, a sessão é registrada no Singleton.

A tabela a seguir detalha a divisão de responsabilidades, o fluxo de co-criação síncrona e a revisão em pares (*peer review*) aplicados exclusivamente para a construção deste artefato e seu relatório.

| Etapa / Tópico do Relatório | Autor(a) Principal | Revisor(a) em Par | Evidência / Commit |
| :--- | :--- | :--- | :---: |
| **Introdução e metodologia** | Samuel Felipe |  |  |
| **Modelagem UML: Factory Method** | Samuel Felipe |  |  |
| **Modelagem UML: Singlethon** | Samuel Felipe |  |  |

---

## Metodologia

Utilizando os papéis de código e as decisções de segurança determinados na página de implementação, os modelos de exemplo do site "Refacotirng Guru" e os modelos de exemplo do material disponibilzado, foi possível montar os UML respectivos para corresponder com a execução do código que será reproduzido envolvendo o Fluxo A.

---

## Modelagem UML: Factory Method

O diagrama UML a seguir segue a estrutura de código referente ao Fluxo A utilizando o Factory Method:

![Diagrama UML - Factory Method](../subequipe01/img%20diagramas/Diagrama_FactoryMethod.png)
<p align="center"<sub>Fonte: Elaborado pelos autores da Subequipe 1: Samuel Felipe, 2026.</sub></p>

---

## Modelagem UML: Singlethon

O diagrama UML a seguir segue a estrutura de código referente ao Fluxo A utilizando o Singlethon:

![Diagrama UML - Singlethon](../subequipe01/img%20diagramas/Diagrama_Singleton.png)
<p align="center"<sub>Fonte: Elaborado pelos autores da Subequipe 1: Samuel Felipe, 2026.</sub></p>

---

## Referências

 - [Refactoring Guru (Factory Method)](https://refactoring.guru/factory-method)
- [Refactoring Guru (Singleton)](https://refactoring.guru/singleton)
- [Draw.IO](https://www.drawio.com/)

---

> **Histórico de versão**
>
>| Versão | Data | Descrição | Autor | Revisor |
>| :----: | :--: |:--------: | :---: | :-----: |
>| 0.1 | 17/10/2026 | Estruturação da página de modelagem do Factory Method e Singlethon | Samuel Felipe |  |
>| 0.2 | 17/10/2026 | Adição da Introdução/Visão Geral e metodologia | Samuel Felipe |  |
>| 0.3 | 17/10/2026 | Adição de informações e diagrama UML do Factory Method e Singlethon | Samuel Felipe |  |
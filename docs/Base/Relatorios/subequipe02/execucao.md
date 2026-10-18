# Execução e Verificação — Adapter de Catálogo

## 1. Pré-requisitos

- JDK 17 ou superior instalado, com `javac` e `java` no `PATH`.
- Terminal aberto na raiz deste repositório. Não são necessários Maven, Gradle, bibliotecas externas ou acesso à internet para executar o exemplo.

O código está em `src/subequipe02` e a verificação em `tests/subequipe02`. Os arquivos compilados são colocados em `build/subequipe02`, diretório ignorado pelo Git.

## 2. Compilar e executar

No **PowerShell**:

```powershell
New-Item -ItemType Directory -Force -Path build/subequipe02 | Out-Null
$fontes = @((Get-ChildItem -LiteralPath src/subequipe02 -Filter '*.java').FullName) + @((Get-ChildItem -LiteralPath tests/subequipe02 -Filter '*.java').FullName)
javac --release 17 -encoding UTF-8 -d build/subequipe02 @fontes
if ($LASTEXITCODE -ne 0) { throw "Falha na compilação" }
java -cp build/subequipe02 subequipe02.AdapterCheck
if ($LASTEXITCODE -ne 0) { throw "Falha na verificação" }
java -cp build/subequipe02 subequipe02.Main
```

Em **Bash**:

```bash
mkdir -p build/subequipe02 &&
javac --release 17 -encoding UTF-8 -d build/subequipe02 src/subequipe02/*.java tests/subequipe02/*.java &&
java -cp build/subequipe02 subequipe02.AdapterCheck &&
java -cp build/subequipe02 subequipe02.Main
```

Para pesquisar outro termo, acrescente-o após `subequipe02.Main`, por exemplo `java -cp build/subequipe02 subequipe02.Main "Sapato"`.

As verificações de erro interrompem a sequência se a compilação ou o teste falhar; uma execução anterior em `build/subequipe02` não será apresentada como evidência de uma compilação bem-sucedida.

## 3. Resultado observado

A compilação com `javac --release 17` e a execução com Eclipse Temurin JDK 21.0.12.1 produziram, em 18/10/2026:

```text
Verificacoes do Adapter: OK
Busca: Bola
BOLA-01 | Bola de futebol | R$ 129,90
BOLA-02 | Bola de basquete | R$ 89,90
Busca: sem-correspondencia
Nenhum produto encontrado.
```

A consulta `"Bola"` confirma a tradução de `BOLA-01 / Bola de futebol / 12990` para `Produto` com `precoAtual = 129.90`. O segundo item confirma que a quantidade e a ordem são preservadas. A consulta `"sem-correspondencia"` confirma o retorno de uma lista vazia. A classe `AdapterCheck` também confere esses valores programaticamente e verifica a busca com letras em caixas diferentes.

## 4. Histórico de versões

| Versão | Data | Descrição | Autor(a) | Revisor(a) |
| --- | --- | --- | --- | --- |
| 1.0 | 18/10/2026 | Instruções de compilação, cenários e saída observada da implementação Java, com apoio de IA. | [Diassis Bezerra Nascimento (@Diaxiz)](https://github.com/Diaxiz) | [Uires Carlos de Oliveira](https://github.com/uires2023) |
| 1.1 | 18/10/2026 | Revisão dos comandos de reprodução para interromper a sequência em caso de erro. | [Diassis Bezerra Nascimento (@Diaxiz)](https://github.com/Diaxiz) | [Uires Carlos de Oliveira](https://github.com/uires2023) |
| 1.2 | 18/10/2026 | Atualização da página para manter apenas os comandos e resultados verificáveis. | [Diassis Bezerra Nascimento (@Diaxiz)](https://github.com/Diaxiz) | [Uires Carlos de Oliveira](https://github.com/uires2023) |
| 1.3 | 18/10/2026 | Inclusão e execução de seis novos cenários em AdapterCheckComplementar.java, com apoio de IA generativa, preservando o teste original e registrando os resultados na documentação. | [Uires Carlos de Oliveira](https://github.com/uires2023) | [Nayra Silva Nery](https://github.com/NayraNery127) |

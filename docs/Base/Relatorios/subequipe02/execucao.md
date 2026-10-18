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

## 4. Vídeo de execução

<video controls preload="metadata" width="800" aria-label="Vídeo da compilação, verificação e execução do Adapter da Subequipe 02">
  <source src="assets/videos/subequipe02-adapter-execucao.webm" type="video/webm">
  Seu navegador não oferece reprodução de vídeo WebM.
</video>

*Vídeo 1 — Reprodução visual automatizada de comandos reais executados neste checkout em 18/10/2026 (12 segundos, sem narração). Mostra a compilação, a verificação automatizada, os dois cenários de busca e os quatro participantes do padrão. A lista de arquivos fonte aparece abreviada no vídeo; os comandos completos estão na seção 2.*

O vídeo reproduz visualmente saídas obtidas em execuções reais, mas não é uma captura contínua de um terminal. A equipe ainda precisa revisá-lo; uma gravação direta e narrada da execução pode oferecer uma evidência mais clara na apresentação à professora.

## 5. Histórico de versões

| Versão | Data | Descrição | Revisão |
| --- | --- | --- | --- |
| 1.0 | 18/10/2026 | Instruções de compilação, cenários e saída observada da implementação Java, com apoio de IA. | Pendente de revisão pela subequipe. |
| 1.1 | 18/10/2026 | Inclusão do vídeo curto gerado a partir da compilação e execução reais, sem narração. | Pendente de revisão pela subequipe. |
| 1.2 | 18/10/2026 | Comandos de reprodução interrompem a sequência em caso de erro; limite da evidência em vídeo explicitado. | Pendente de revisão pela subequipe. |

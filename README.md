# Projeto eCommerce - Testes de Software I

**Instituição**: UFRN  
**Disciplina**: Teste de Software I  
**Professor**: Eiji Adachi  
**Alunos**: Antonio Higino Bisneto Leite Medeiros, Nathan Medeiros Clemente

---

## Sumário

- [Como Executar o Projeto](#como-executar-o-projeto)
- [Como Executar os Testes](#como-executar-os-testes)
- [Como Verificar a Cobertura dos Testes](#como-verificar-a-cobertura-dos-testes)
- [Documentação](#documentação)
- [Licença](#licença)

---

## Como Executar o Projeto

### Pré-requisitos

- **Java 17** ou superior
- **Maven** (ou usar o wrapper incluído)

### Comandos Maven

- **Maven instalado**: Use `mvn`
- **Wrapper incluído**: Use `.\mvnw.cmd` (Windows) ou `./mvnw` (Linux/Mac)

### Passos para Execução

1. **Navegue até o diretório do projeto**:
   ```powershell
   cd projetoUni2
   ```

2. **Compile o projeto**:
   ```powershell
   .\mvnw.cmd clean compile
   ```

3. **Execute a aplicação**:
   ```powershell
   .\mvnw.cmd spring-boot:run
   ```

---

## Como Executar os Testes

### Executar Todos os Testes

```powershell
.\mvnw.cmd test
```

### Executar Classe de Teste Específica

```powershell
# Testes do CompraService - Casos gerais
.\mvnw.cmd test -Dtest=CompraServiceTest

# Testes do CompraService - Partições
.\mvnw.cmd test -Dtest=CompraServiceParticoesTest

# Testes do CompraService - Valores limites
.\mvnw.cmd test -Dtest=CompraServiceLimitesTest

# Testes do finalizarCompra - Cenário 1 (Fakes para externos + Mocks para serviços)
.\mvnw.cmd test -Dtest=CompraServiceFinalizarCompraCenario1Test

# Testes do finalizarCompra - Cenário 2 (Mocks para externos + Fakes para serviços)
.\mvnw.cmd test -Dtest=CompraServiceFinalizarCompraCenario2Test

# Testes de ItemCompra
.\mvnw.cmd test -Dtest=ItemCompraTest

# Testes de Produto
.\mvnw.cmd test -Dtest=ProdutoTest
```

### Executar Teste Específico

```powershell
.\mvnw.cmd test -Dtest=CompraServiceTest#nomeDoMetodoDeTeste
```

### Saída Esperada

Após executar os testes, você verá um resumo como:

```
[INFO] Tests run: XX, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

---

## Como Verificar a Cobertura dos Testes

### Gerar Relatório de Cobertura

```powershell
.\mvnw.cmd clean test jacoco:report
```

### Visualizar o Relatório

1. Após executar o comando acima, o relatório será gerado em:
   ```
   target/site/jacoco/index.html
   ```

2. Abra o arquivo `index.html` em um navegador

3. Navegue até a classe `CompraService` para ver a cobertura detalhada:
   - Clique em `ecommerce.service`
   - Clique em `CompraService`
   - Clique no método: `calcularCustoTotal`

### Métricas de Cobertura Esperadas

**Para o método `calcularCustoTotal`:**
- **Cobertura de Instruções**: 100%
- **Cobertura de Branches (Arestas)**: 100%
- **Mutation Coverage**: 100% (8 mutantes mortos de 8 gerados)

**Para o método `finalizarCompra`:**
- **Cobertura de Instruções**: 100% (103/103)
- **Cobertura de Branches (Decisões)**: 100% (6/6)
- **Cobertura de Linhas**: 100% (18/18)
- **Test Doubles**: 2 cenários com estratégias invertidas
- **Total de Testes**: 8 (4 por cenário)

### Interpretar o Relatório JaCoCo

- **Verde**: Linha/branch coberto
- **Amarelo**: Branch parcialmente coberto
- **Vermelho**: Linha/branch não coberto

---

## Testes de Mutação

### Gerar Relatório de Mutação

```powershell
.\mvnw.cmd test-compile org.pitest:pitest-maven:mutationCoverage
```

### Visualizar o Relatório

1. Após executar o comando acima, o relatório será gerado em:
   ```
   target/pit-reports/index.html
   ```

2. Abra o arquivo `index.html` em um navegador

3. Navegue até a classe `CompraService` para ver a cobertura detalhada:
   - Clique em `ecommerce.service`
   - Clique em `CompraService.java`

### Métricas Esperadas

**Para a classe `CompraService`:**
- **Line Coverage**: 71% (44/62)
- **Mutation Coverage**: 53% (8/15)
- **Test Strength**: 100% (8/8)

### Interpretar o Relatório PIT

- **Linha Verde**: Testes cobrem essa linha
- **Linha Verde Mais Escura**: Mutação foi realizada naquela linha e mutante foi morto
- **Vermelho**: Nenhum teste cobre essa linha
- **Vermelho Mais Escuro**: Mutação foi realizada naquela linha e mutante não foi morto

### Resultados e Estratégia

- Como é possível observar no relatório seguindo as instruções descritas acima, **todos os mutantes** (8 no total) 
gerados a partir de mutações no método `calcularCustoTotal` foram mortos, **atingindo a meta de 100%**

- Não foram necessárias alterações no código para atingir a meta

---

## Testes do Método finalizarCompra()

### Estratégia de Test Doubles

Para testar o método `finalizarCompra()`, foram implementados **dois cenários** com estratégias **invertidas** de test doubles, garantindo cobertura completa de decisões (100% branch coverage).

### Cenário 1: Fakes para Externos + Mocks para Internos

**Arquivos:**
- Classe de Teste: `CompraServiceFinalizarCompraCenario1Test.java`
- Fakes implementados:
  - `EstoqueSimulado` (implementa `IEstoqueExternal`)
  - `PagamentoSimulado` (implementa `IPagamentoExternal`)

**Test Doubles utilizados:**
- **Fakes** para dependências **externas**:
  - `EstoqueSimulado`: Simula API externa de estoque com estado interno (HashMap)
  - `PagamentoSimulado`: Simula API externa de pagamento com rastreamento de transações
- **Mocks** (Mockito) para serviços **internos**:
  - `ClienteService`
  - `CarrinhoDeComprasService`

**Executar:**
```powershell
.\mvnw.cmd test -Dtest=CompraServiceFinalizarCompraCenario1Test
```

### Cenário 2: Mocks para Externos + Fakes para Internos

**Arquivos:**
- Classe de Teste: `CompraServiceFinalizarCompraCenario2Test.java`
- Fakes implementados:
  - `ClienteServiceFake` (estende `ClienteService`)
  - `CarrinhoDeComprasServiceFake` (estende `CarrinhoDeComprasService`)

**Test Doubles utilizados:**
- **Mocks** (Mockito) para dependências **externas**:
  - `IEstoqueExternal`
  - `IPagamentoExternal`
- **Fakes** para serviços **internos**:
  - `ClienteServiceFake`: Implementação fake com armazenamento em memória (HashMap)
  - `CarrinhoDeComprasServiceFake`: Implementação fake com validação de propriedade

**Executar:**
```powershell
.\mvnw.cmd test -Dtest=CompraServiceFinalizarCompraCenario2Test
```

### Verificações nos Testes

Todos os testes verificam **duas coisas**:

1. **Comportamento esperado** (usando `assertThat()`):
   - Resultado retornado correto
   - Exceções lançadas com mensagens apropriadas
   - Estado dos objetos após operação

2. **Métodos invocados** (usando `verify()`):
   - Invocações dos mocks foram realizadas
   - Sequência de chamadas está correta
   - Rollback executado quando necessário

### Cobertura de Código

**Método `finalizarCompra()`:**
- **Cobertura de Branches (Decisões)**: **6/6 → 100%** ✅
- **Cobertura de Instruções**: **103/103 → 100%** ✅
- **Cobertura de Linhas**: **18/18 → 100%** ✅

**Importante:** Cada cenário **individualmente** já atinge 100% de cobertura de branches!

### Verificar Cobertura

Para verificar a cobertura de branches do `finalizarCompra()`:

1. Execute os testes com relatório:
   ```powershell
   .\mvnw.cmd clean test jacoco:report
   ```

2. Abra o relatório:
   ```
   target/site/jacoco/index.html
   ```

3. Navegue até:
   - `ecommerce.service` → `CompraService` → `finalizarCompra(Long, Long)`

4. Confirme:
   - **Missed Branches**: 0
   - **Cov.**: 100%

---

## Documentação  

 ### Acerca dos testes de validação e robustez, cobrindo entradas inválidas

Nas classes `ItemCompra` e `Produto` foram feitas alterações nos métodos **`setAtributo`** descritos abaixo,  
e os respectivos valores limites foram utilizados como entradas inválidas para testar o lançamento de exceções:
- ItemCompra:  
   - setId (0,-1)
   - setQuantidade (0,-1)
- Produto:
   - setId (0,-1)
   - setPreco (-0.99, -1)
   - setPesoFisico (0, -0.99)
   - setComprimento (0, -0.99)
   - setLargura (0, -0.99)
   - setAltura (0, -0.99)

 ### Planilha e Grafo

Para os testes relacionados a `calcularCustoTotal`, o projeto de casos de teste, valores limites, partições  
encontradas, além de outras informações relevantes estão presentes no arquivo **`Planilha.xlsx`**.

O grafo de fluxo de controle (CFG) está presente no arquivo **`Grafo.png`**.

### Complexidade Ciclomática

**Fórmula:** V(G) = E - N + 2P

Onde:
- **M** (ou V(G)) = Complexidade ciclomática
- **E** = Quantidade de arestas (setas) = 19
- **N** = Quantidade de nós = 9
- **P** = Quantidade de componentes conectados = 1

**Cálculo:**  
V(G) = 19 - 9 + 2(1) = **12**

**Interpretação:**
- Complexidade moderada
- Requer no mínimo **12 casos de teste independentes** para cobertura completa de caminhos

---

## Licença

Este projeto foi desenvolvido para fins acadêmicos na disciplina de Teste de Software I - UFRN.
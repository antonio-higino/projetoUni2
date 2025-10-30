# Projeto Unidade 2 - Testes de Software I

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

### Interpretar o Relatório JaCoCo

- **Verde**: Linha/branch coberto
- **Amarelo**: Branch parcialmente coberto
- **Vermelho**: Linha/branch não coberto

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
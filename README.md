# Projeto Unidade 2 - Testes de Software I

**Instituição**: UFRN  
**Disciplina**: Teste de Software I    
**Professor**: Eiji Adachi  
**Alunos**: Antonio Higino Bisneto Leite Medeiros, Nathan Medeiros Clemente  

---

## Como Executar o Projeto

### Pré-requisitos

- **Java 17**
- **Maven** (ou usar o wrapper incluído)

### Passos para Execução

- Para usar:
    - Maven instalado: **mvn**
    - Wrapper incluso: **.\mvnw.cmd**

1. **Clone o repositório** (ou extraia o arquivo .zip):
   ```powrshell (ou terminal equivalente)
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
# Testes do CompraService (100% branch coverage)
.\mvnw.cmd test -Dtest=CompraServiceTest

# Testes do CompraService (Valores limites)
.\mvnw.cmd test -Dtest=CompraServiceTestLimites

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
[INFO] Tests run: 51, Failures: 0, Errors: 0, Skipped: 0
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

3. Navegue até a classe `CompraService` para ver a cobertura detalhada
    1. ecommerce.service
    2. compraService
    3. calcularCustoTotal(CarrinhoDeCompras, Regiao, TipoCliente)  

### Métricas de Cobertura Esperadas para o método `calcularCustoTotal`

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
encontradas, além de outras informações relevantes estão presentes no arquivo **`Planilha.xlsl`**.

O grafo de fluxo de fluxo de controle (GFC) está presente no arquivo **`Grafo.png`**.

---

## Licença

Este projeto foi desenvolvido para fins acadêmicos na disciplina de Teste de Software I - UFRN.
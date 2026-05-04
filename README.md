# Controle de Estoque — Programação II
**Professor:** Flávio Vilela | **Turma:** 2026-1

---

## Estrutura do projeto

```
controle-estoque/
├── pom.xml
└── src/main/java/br/edu/ifg/estoque/
    ├── Main.java                        ← testes via método main
    ├── model/                           ← entidades que representam o banco
    │   ├── Categoria.java
    │   ├── Produto.java
    │   ├── Cliente.java
    │   ├── Fornecedor.java
    │   ├── Venda.java
    │   ├── ItemVenda.java               ← tabela associativa venda ↔ produto
    │   ├── Compra.java
    │   └── ItemCompra.java              ← tabela associativa compra ↔ produto
    ├── dao/                             ← acesso ao banco de dados (JDBC)
    │   ├── CategoriaDAO.java
    │   ├── ProdutoDAO.java
    │   ├── ClienteDAO.java
    │   ├── FornecedorDAO.java
    │   ├── VendaDAO.java
    │   └── CompraDAO.java
    ├── controller/                      ← regras de negócio
    │   ├── CategoriaController.java
    │   ├── ProdutoController.java
    │   ├── ClienteController.java
    │   ├── FornecedorController.java
    │   ├── VendaController.java         ← aplica RNF001, RNF003, RNF004, RNF005
    │   └── CompraController.java        ← aplica RNF002, RNF006, RNF007
    └── util/
        └── Conexao.java                 ← fábrica de conexão MySQL
src/main/resources/
    └── banco.sql                        ← script SQL completo
```

---

## Banco de dados

Execute o script `banco.sql` no MySQL para criar o banco e as tabelas:

```sql
-- No MySQL Workbench ou linha de comando:
source caminho/para/banco.sql
```

**Tabelas criadas:**

| Tabela        | Descrição                                         |
|---------------|---------------------------------------------------|
| `categoria`   | Categorias de produtos                            |
| `produto`     | Produtos com estoque e preços                     |
| `cliente`     | Clientes com CPF único                            |
| `fornecedor`  | Fornecedores com CNPJ único                       |
| `venda`       | Cabeçalho da venda (cliente + data + total)       |
| `item_venda`  | Itens da venda (produto + qtd + valor_unit)       |
| `compra`      | Cabeçalho da compra (fornecedor + data + total)   |
| `item_compra` | Itens da compra (produto + qtd + valor_unit)      |

---

## Configuração da conexão

Edite `Conexao.java` com suas credenciais MySQL:

```java
private static final String URL     = "jdbc:mysql://localhost:3306/controle_estoque_prog2_seuNome?...";
private static final String USUARIO = "root";
private static final String SENHA   = "root"; // sua senha aqui
```

---

## Como executar

```bash
# Compilar
mvn compile

# Executar
mvn exec:java -Dexec.mainClass="br.edu.ifg.estoque.Main"

# Ou gerar o jar e rodar
mvn package
java -jar target/controle-estoque-1.0-SNAPSHOT.jar
```

---

## Requisitos implementados

### Funcionais
| RF    | Descrição                                              | Status |
|-------|--------------------------------------------------------|--------|
| RF001 | Cadastrar produtos                                     | ✅     |
| RF002 | Cadastrar clientes                                     | ✅     |
| RF003 | Cadastrar fornecedores                                 | ✅     |
| RF004 | Cadastrar categorias                                   | ✅     |
| RF005 | Produto deve ter categoria                             | ✅     |
| RF006 | Venda com um cliente e vários produtos                 | ✅     |
| RF007 | Compra com um fornecedor e vários produtos             | ✅     |

### Não Funcionais
| RNF    | Descrição                                             | Onde é aplicado          |
|--------|-------------------------------------------------------|--------------------------|
| RNF001 | Venda subtrai estoque                                 | `VendaController`        |
| RNF002 | Compra acrescenta estoque                             | `CompraController`       |
| RNF003 | Bloqueia venda se estoque < 1                         | `VendaController`        |
| RNF004 | Bloqueia venda se CPF já tem 3 vendas no mês          | `VendaController`        |
| RNF005 | Atualiza `valor_ultima_venda` ao vender               | `VendaController`        |
| RNF006 | Atualiza `valor_ultima_compra` ao comprar             | `CompraController`       |
| RNF007 | Atualiza `preco_medio` (média das compras) ao comprar | `CompraController`       |

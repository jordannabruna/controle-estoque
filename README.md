# Controle de Estoque

## Como executar

### 1️⃣ Iniciar o PostgreSQL

```bash
sudo service postgresql start
```

### 2️⃣ Criar o banco de dados

```bash
sudo -u postgres psql -c "CREATE DATABASE controle_estoque_prog2_jordanna ENCODING 'UTF8';"
```

### 3️⃣ Configurar a senha do PostgreSQL

```bash
sudo -u postgres psql -c "ALTER USER postgres PASSWORD '123456789';"
```

### 4️⃣ Criar as tabelas

```bash
cat /home/jordanna/Downloads/controle-estoque/controle-estoque/src/main/resources/banco.sql | sudo -u postgres psql -d controle_estoque_prog2_jordanna
```

### 5️⃣ Compilar e executar o programa

```bash
cd /home/jordanna/Downloads/controle-estoque/controle-estoque
mvn clean compile exec:java -Dexec.mainClass="br.edu.ifg.estoque.Main"
```

---

## Versão rápida (um comando)

Após a primeira execução, use:

```bash
cd /home/jordanna/Downloads/controle-estoque/controle-estoque && \
sudo service postgresql start && \
mvn exec:java -Dexec.mainClass="br.edu.ifg.estoque.Main"
```

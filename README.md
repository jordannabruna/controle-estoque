# Controle de Estoque

## Como executar

### 1️⃣ Subir o PostgreSQL com Docker

```bash
docker compose up -d
```

O container já cria o banco `controle_estoque_prog2_jordanna` com o usuário `postgres` e a senha `123456789`.

### 2️⃣ Compilar e executar o programa

```bash
mvn clean compile exec:java -Dexec.mainClass="br.edu.ifg.estoque.Main"
```

---

## Versão rápida

Após subir o container uma vez, basta executar:

```bash
mvn exec:java -Dexec.mainClass="br.edu.ifg.estoque.Main"
```

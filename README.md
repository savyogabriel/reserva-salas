# Sistema de Reserva de Salas de Estudo

> Trabalho Final — Engenharia de Software II | IFPI  
> Tecnologias: Java 21 · Spring Boot 4.1 · Thymeleaf · Spring Security · PostgreSQL · Bootstrap 5

---

## Sumário

1. [Descrição do Sistema](#1-descrição-do-sistema)
2. [Requisitos](#2-requisitos)
3. [Instalação](#3-instalação)
4. [Configuração do Banco de Dados](#4-configuração-do-banco-de-dados)
5. [Configuração do Projeto](#5-configuração-do-projeto)
6. [Execução](#6-execução)
7. [Execução dos Testes](#7-execução-dos-testes)
8. [Estrutura de Pastas](#8-estrutura-de-pastas)
9. [Usuários de Acesso](#9-usuários-de-acesso)
10. [Funcionalidades e Exemplos de Uso](#10-funcionalidades-e-exemplos-de-uso)
11. [Limitações Conhecidas](#11-limitações-conhecidas)

---

## 1. Descrição do Sistema

Sistema web para gerenciamento e reserva de salas de estudo, desenvolvido com arquitetura **MVC em camadas** (Controller → Service → Repository). Permite que usuários solicitem reservas, e administradores as aprovem ou rejeitem.

**Funcionalidades principais:**
- Cadastro, edição e inativação de salas (ADMIN)
- Solicitação de reserva de sala (USER)
- Aprovação e rejeição de reservas (ADMIN)
- Cancelamento de reservas (USER)
- Dashboard com estatísticas em tempo real

---

## 2. Requisitos

| Ferramenta | Versão mínima |
|---|---|
| Java JDK | 21 |
| Maven | 3.9+ (ou usar `mvnw` incluído) |
| PostgreSQL | 14+ |
| Navegador moderno | — |

---

## 3. Instalação

### Java 21 (Ubuntu/Debian)

```bash
sudo apt update
sudo apt install -y openjdk-21-jdk
java -version
```

### Java 21 (Windows)

Baixe o instalador em: https://adoptium.net/temurin/releases/?version=21  
Adicione `JAVA_HOME` às variáveis de ambiente.

### PostgreSQL (Ubuntu/Debian)

```bash
sudo apt install -y postgresql postgresql-contrib
sudo systemctl start postgresql
sudo systemctl enable postgresql
```

### PostgreSQL (Windows)

Baixe o instalador em: https://www.postgresql.org/download/windows/

---

## 4. Configuração do Banco de Dados

```bash
# Acesse o PostgreSQL como superusuário
sudo -u postgres psql

# Crie o banco de dados
CREATE DATABASE reserva_salas_db;

# Saia do psql
\q

# Execute o script de criação das tabelas
psql -U postgres -d reserva_salas_db -f script.sql
```

---

## 5. Configuração do Projeto

Edite o arquivo `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/reserva_salas_db
spring.datasource.username=postgres
spring.datasource.password=SUA_SENHA_AQUI
```

> **Primeira execução**: altere `spring.jpa.hibernate.ddl-auto=validate` para `create`  
> e restaure para `validate` nas execuções seguintes.

---

## 6. Execução

```bash
# Clone ou navegue até o diretório do projeto
cd reserva-salas

# Execute com o Maven Wrapper
./mvnw spring-boot:run

# Windows
mvnw.cmd spring-boot:run
```

Acesse: **http://localhost:8080**

---

## 7. Execução dos Testes

```bash
# Executa todos os testes unitários (usa H2 em memória, sem PostgreSQL)
./mvnw test

# Executa apenas os testes de serviço
./mvnw test -Dtest="SalaServiceTest,ReservaServiceTest"

# Gera relatório de cobertura (se configurado)
./mvnw verify
```

---

## 8. Estrutura de Pastas

```
reserva-salas/
├── src/
│   ├── main/
│   │   ├── java/br/edu/ifpi/reserva_salas/
│   │   │   ├── config/           # SecurityConfig, WebMvcConfig
│   │   │   ├── controller/       # DashboardController, SalaController, ReservaController
│   │   │   ├── dto/              # SalaDTO, ReservaDTO
│   │   │   ├── exception/        # Exceções customizadas + GlobalExceptionHandler
│   │   │   ├── model/            # Sala, Reserva, StatusReserva
│   │   │   ├── repository/       # SalaRepository, ReservaRepository
│   │   │   ├── service/          # SalaService, ReservaService
│   │   │   └── validation/       # Strategy Pattern: ReservaValidationStrategy + implementações
│   │   └── resources/
│   │       ├── static/css/       # custom.css
│   │       └── templates/        # Thymeleaf HTML (layout, sala, reserva, login, error)
│   └── test/
│       └── java/.../service/     # SalaServiceTest, ReservaServiceTest
├── script.sql                    # DDL + dados de exemplo
├── documentacao-es2.md           # Documentação acadêmica completa
├── pom.xml
└── README.md
```

---

## 9. Usuários de Acesso

| Usuário | Senha      | Papel |
|---------|------------|-------|
| admin   | admin123   | ADMIN |
| joao    | user123    | USER  |
| maria   | user123    | USER  |
| pedro   | user123    | USER  |

> Credenciais definidas em `SecurityConfig.java` (usuários em memória).

---

## 10. Funcionalidades e Exemplos de Uso

### Solicitar uma reserva (USER)

1. Faça login com `joao / user123`
2. Clique em **"Solicitar Reserva"**
3. Selecione a sala, data e horário
4. Clique em **"Solicitar Reserva"**
5. A reserva aparecerá com status **PENDENTE**

### Aprovar uma reserva (ADMIN)

1. Faça login com `admin / admin123`
2. Clique em **"Aprovações"** no menu ou no botão do Dashboard
3. Clique em **"Aprovar"** na reserva desejada

### Cadastrar uma sala (ADMIN)

1. Faça login com `admin / admin123`
2. Clique em **"Nova Sala"** no menu
3. Preencha nome e capacidade
4. Clique em **"Cadastrar Sala"**

---

## 11. Limitações Conhecidas

- **Autenticação em memória**: os usuários são pré-configurados em `SecurityConfig.java`. Em produção, deve ser substituído por autenticação com banco de dados.
- **Sem paginação**: a listagem de reservas não é paginada. Com grande volume, pode ser lenta.
- **Sem e-mail de notificação**: não há envio de e-mail ao aprovar/rejeitar reservas.
- **Sem recuperação de senha**: não há fluxo de redefinição de senha.
- **Sem perfil de usuário**: não é possível alterar nome ou senha pelo sistema.

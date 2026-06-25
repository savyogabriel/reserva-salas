# Documentação de Engenharia de Software II
## Sistema de Reserva de Salas de Estudo — IFPI

---

## 1. Visão do Produto

### 1.1 Declaração de Visão

**Para** a comunidade acadêmica do IFPI (alunos e professores)  
**que** necessita gerenciar o uso de salas de estudo de forma organizada,  
**o Sistema de Reserva de Salas** é uma aplicação web  
**que** permite o agendamento e controle de reservas de forma centralizada e transparente.  
**Diferente de** sistemas de planilhas e agendamento manual,  
**nosso produto** oferece um fluxo completo de solicitação, aprovação e consulta com validação automática de conflitos de horário.

### 1.2 Objetivo

Desenvolver um sistema web para gerenciamento de salas de estudo, permitindo:
- Administradores cadastrarem e gerirem salas;
- Usuários solicitarem e acompanharem reservas;
- Validação automática de conflitos de horário;
- Fluxo de aprovação com rastreabilidade completa.

---

## 2. Atores do Sistema

| Ator | Descrição | Permissões |
|------|-----------|-----------|
| **Administrador** | Responsável pela gestão do sistema | Cadastrar/editar/inativar salas; aprovar/rejeitar reservas; visualizar todas as reservas |
| **Usuário** | Membro da comunidade acadêmica | Solicitar reservas; cancelar suas reservas; visualizar salas disponíveis |

---

## 3. Histórias de Usuário e Critérios de Aceitação

### HU01 — Cadastro de Salas
> *Como administrador, desejo cadastrar salas para disponibilizá-las para reserva.*

**Critérios de Aceitação:**
- [x] O formulário exige Nome (obrigatório, 2–100 caracteres) e Capacidade (mínimo 1)
- [x] O sistema não permite duas salas com o mesmo nome (RN01)
- [x] A sala é cadastrada com status ATIVA (RN02)
- [x] Uma mensagem de sucesso é exibida após o cadastro
- [x] Apenas usuários com role ADMIN acessam a funcionalidade

### HU02 — Visualização de Salas
> *Como usuário, desejo visualizar salas disponíveis para escolher uma sala adequada.*

**Critérios de Aceitação:**
- [x] O usuário autenticado visualiza a lista de salas
- [x] No formulário de reserva, somente salas ATIVAS aparecem no dropdown
- [x] A lista exibe nome, capacidade e status de cada sala
- [x] Salas inativas aparecem distinguidas visualmente na lista do admin

### HU03 — Solicitar Reserva
> *Como usuário, desejo solicitar uma reserva de sala para determinada data e horário.*

**Critérios de Aceitação:**
- [x] O usuário seleciona sala, data (futura), hora início e hora fim
- [x] A reserva é criada com status PENDENTE (RN03)
- [x] O sistema rejeita horários com conflito com reservas já APROVADAS (RN04)
- [x] O sistema rejeita reservas em salas INATIVAS
- [x] Hora de fim deve ser posterior à hora de início
- [x] O nome do solicitante é registrado automaticamente (usuário logado)
- [x] Uma mensagem confirma a solicitação

### HU04 — Aprovar ou Rejeitar Reservas
> *Como administrador, desejo aprovar ou rejeitar reservas para controlar a utilização das salas.*

**Critérios de Aceitação:**
- [x] O admin visualiza a lista de reservas PENDENTES
- [x] Pode aprovar ou rejeitar individualmente cada reserva
- [x] Apenas reservas PENDENTES podem ser aprovadas (RN05)
- [x] Apenas reservas PENDENTES podem ser rejeitadas
- [x] Apenas usuários com role ADMIN acessam a funcionalidade de aprovação
- [x] Mensagem de feedback é exibida após cada ação

### HU05 — Cancelar Reserva
> *Como usuário, desejo cancelar uma reserva para liberar o horário reservado.*

**Critérios de Aceitação:**
- [x] O usuário pode cancelar suas reservas PENDENTES ou APROVADAS
- [x] A data e hora do cancelamento são registradas automaticamente (RN06)
- [x] Reservas CANCELADAS não bloqueiam novas reservas no mesmo horário (RN07)
- [x] Reservas já CANCELADAS ou REJEITADAS não podem ser canceladas novamente
- [x] Uma mensagem confirma o cancelamento

---

## 4. Backlog Priorizado

| Prioridade | ID | História | Complexidade | Sprint |
|---|---|---|---|---|
| 🔴 Alta | HU01 | Cadastro de Salas | Média | Sprint 1 |
| 🔴 Alta | HU02 | Visualização de Salas | Baixa | Sprint 1 |
| 🔴 Alta | HU03 | Solicitar Reserva | Alta | Sprint 2 |
| 🔴 Alta | HU04 | Aprovar/Rejeitar Reservas | Alta | Sprint 2 |
| 🟡 Média | HU05 | Cancelar Reserva | Baixa | Sprint 3 |
| 🟢 Baixa | —   | Dashboard com estatísticas | Baixa | Sprint 3 |
| 🟢 Baixa | —   | Tratamento global de erros | Baixa | Sprint 3 |

---

## 5. Planejamento das Sprints

### Sprint 1 — Infraestrutura e Gerenciamento de Salas

**Duração:** 2 semanas  
**Objetivo:** Ter a fundação técnica pronta e o módulo de salas funcionando.

**Tarefas:**
- Configuração do projeto (pom.xml, application.properties, PostgreSQL)
- Implementação das entidades Sala e Reserva
- Implementação de SalaRepository e ReservaRepository
- Implementação de SalaService (HU01 + RN01 + RN02)
- Implementação de SalaController e templates Thymeleaf para salas
- Configuração de Spring Security com usuários em memória

**Entregável:** Sistema com CRUD completo de salas funcionando.

---

### Sprint 2 — Módulo de Reservas

**Duração:** 2 semanas  
**Objetivo:** Implementar o fluxo completo de reservas com validações.

**Tarefas:**
- Implementação do Strategy Pattern para validações (RN04, RN07)
- Implementação de ReservaService (HU03 + HU04 + RN03–RN05)
- Implementação de ReservaController
- Templates de solicitação e aprovação de reservas
- GlobalExceptionHandler para tratamento de erros

**Entregável:** Fluxo de reserva funcionando do início ao fim.

---

### Sprint 3 — Cancelamento, Dashboard e Qualidade

**Duração:** 1 semana  
**Objetivo:** Finalizar funcionalidades, testes e documentação.

**Tarefas:**
- Implementação do cancelamento de reservas (HU05 + RN06)
- Dashboard com estatísticas
- Testes unitários (SalaServiceTest, ReservaServiceTest)
- Revisão de CSS e UX
- README.md e documentação acadêmica

**Entregável:** Sistema completo, testado e documentado.

---

## 6. Modelo de Domínio

```
┌──────────────────────────────────────────────────────────────────┐
│                        MODELO DE DOMÍNIO                         │
└──────────────────────────────────────────────────────────────────┘

  ┌─────────────────┐            ┌────────────────────────────────┐
  │      Sala       │            │            Reserva             │
  ├─────────────────┤            ├────────────────────────────────┤
  │ id: Long        │ 1      N   │ id: Long                       │
  │ nome: String    │────────────│ sala: Sala (FK)                │
  │ capacidade: Int │            │ nomeUsuario: String            │
  │ ativa: Boolean  │            │ data: LocalDate                │
  └─────────────────┘            │ horaInicio: LocalTime          │
                                 │ horaFim: LocalTime             │
                                 │ status: StatusReserva          │
                                 │ dataCriacao: LocalDateTime     │
                                 │ dataCancelamento: LocalDateTime│
                                 └────────────────────────────────┘
                                           │ uses
                                           ▼
                                 ┌────────────────────┐
                                 │   StatusReserva    │
                                 ├────────────────────┤
                                 │ PENDENTE           │
                                 │ APROVADA           │
                                 │ REJEITADA          │
                                 │ CANCELADA          │
                                 └────────────────────┘

  Ciclo de Vida de uma Reserva:
  
  [PENDENTE] ──aprovar──► [APROVADA]
  [PENDENTE] ──rejeitar──► [REJEITADA]
  [PENDENTE] ──cancelar──► [CANCELADA]
  [APROVADA] ──cancelar──► [CANCELADA]
```

---

## 7. Justificativa da Arquitetura

### 7.1 Arquitetura MVC em Camadas

O sistema adota a arquitetura **MVC (Model-View-Controller)** com separação em camadas, que é o padrão de facto para aplicações web Java corporativas.

**Camadas e responsabilidades:**

| Camada | Localização | Responsabilidade |
|--------|-------------|-----------------|
| **Model** | `model/` | Representar os conceitos do domínio |
| **Repository** | `repository/` | Acesso e persistência de dados |
| **Service** | `service/` | Lógica e regras de negócio |
| **Controller** | `controller/` | Receber requisições HTTP e retornar views |
| **View** | `templates/` | Renderização da interface (Thymeleaf) |
| **DTO** | `dto/` | Transferência de dados entre camadas |
| **Exception** | `exception/` | Tratamento centralizado de erros |
| **Config** | `config/` | Configurações de segurança e MVC |

**Benefícios:**
- **Separação de preocupações**: cada camada tem uma responsabilidade específica
- **Testabilidade**: services podem ser testados isoladamente com mocks
- **Manutenibilidade**: mudanças em uma camada não afetam as demais
- **Escalabilidade**: fácil adição de novos módulos

### 7.2 Justificativa do Spring Boot + Thymeleaf

| Tecnologia | Justificativa |
|---|---|
| **Spring Boot** | Autoconfiguração, ecossistema maduro, ampla documentação, padrão da indústria |
| **Spring MVC** | Implementação robusta do padrão MVC com suporte completo a Thymeleaf |
| **Thymeleaf** | Integração nativa com Spring, templates válidos em HTML, sem necessidade de servidor separado |
| **Spring Data JPA** | Abstração sobre JDBC, consultas derivadas, Repository Pattern nativo |
| **Spring Security** | Autenticação e autorização declarativa por anotação e configuração |
| **Bean Validation** | Validação declarativa com anotações, integrada ao Spring MVC |
| **Lombok** | Elimina código boilerplate (getters, setters, construtores), melhora legibilidade |
| **PostgreSQL** | SGBD relacional robusto, gratuito e amplamente utilizado em produção |
| **Bootstrap 5** | Framework CSS responsivo com componentes prontos, ampla comunidade |

---

## 8. Princípios SOLID Aplicados

### 8.1 SRP — Single Responsibility Principle

> *"Uma classe deve ter apenas um motivo para mudar."*

**Aplicação no projeto:**

| Classe | Responsabilidade única |
|--------|------------------------|
| `Sala.java` | Representar o domínio de Sala. Sem lógica de negócio. |
| `Reserva.java` | Representar o domínio de Reserva. Apenas dados e @PrePersist para defaults. |
| `SalaService.java` | Apenas regras de negócio referentes a Salas (RN01, RN02). |
| `ReservaService.java` | Apenas regras de negócio referentes a Reservas (RN03–RN07). |
| `SalaController.java` | Apenas receber requisições HTTP de Salas e delegar ao SalaService. |
| `GlobalExceptionHandler.java` | Apenas tratar e centralizar erros/exceções da aplicação. |
| `ConflitoDePeriodoStrategy.java` | Apenas validar conflito de horários entre reservas. |

**Exemplo de violação que evitamos:**  
Não colocamos lógica de validação de conflito diretamente na entidade `Reserva` ou no `ReservaController`. Isso seria uma violação do SRP.

---

### 8.2 OCP — Open/Closed Principle

> *"Classes devem ser abertas para extensão, mas fechadas para modificação."*

**Aplicação via Strategy Pattern:**

A interface `ReservaValidationStrategy` permite adicionar novas regras de validação sem modificar o `ReservaService`:

```
ReservaValidationStrategy (interface)
    ├── ConflitoDePeriodoStrategy  ← RN04: conflito de horários
    └── SalaAtivaStrategy          ← verifica se sala está ativa

// Para adicionar RN nova (ex: limite de reservas por usuário):
class LimiteReservasStrategy implements ReservaValidationStrategy { ... }
// ReservaService NÃO precisa ser modificado!
```

O `ReservaService` recebe `List<ReservaValidationStrategy>` via injeção de dependência.  
O Spring automaticamente injeta todos os beans que implementam a interface.

---

### 8.3 DIP — Dependency Inversion Principle

> *"Módulos de alto nível não devem depender de módulos de baixo nível. Ambos devem depender de abstrações."*

**Aplicação no projeto:**

| Módulo de alto nível | Depende de (abstração) | Não de (implementação) |
|---|---|---|
| `SalaService` | `SalaRepository` (interface) | `SimpleJpaRepository` (impl. concreta) |
| `ReservaService` | `ReservaRepository` (interface) | implementação JPA concreta |
| `ReservaService` | `List<ReservaValidationStrategy>` | `ConflitoDePeriodoStrategy` diretamente |
| `SalaController` | `SalaService` (injetado via @RequiredArgsConstructor) | `SalaService` instanciado com `new` |

**Benefício prático para testes:**  
Nos testes unitários (`SalaServiceTest`, `ReservaServiceTest`), substituímos as implementações reais por mocks (`@Mock`) sem alterar nenhum código de produção.

---

## 9. Padrões de Projeto Aplicados

### 9.1 Repository Pattern

**O que é:** Encapsula a lógica de acesso a dados em uma camada separada, fornecendo uma interface similar a uma coleção para os objetos de domínio.

**Onde:** `SalaRepository` e `ReservaRepository` estendem `JpaRepository<T, ID>`.

**Benefícios:**
- Services desconhecem detalhes de SQL ou JPA
- Facilita a substituição da tecnologia de persistência
- Centraliza as consultas ao banco de dados

```java
// SalaRepository — consultas customizadas encapsuladas
boolean existsByNomeIgnoreCase(String nome);       // RN01
List<Sala> findByAtivaTrue();                      // HU02
```

---

### 9.2 Service Layer Pattern

**O que é:** Define uma camada de serviços que encapsula a lógica de negócio da aplicação, funcionando como ponto de entrada para operações do domínio.

**Onde:** `SalaService` e `ReservaService`.

**Benefícios:**
- Lógica de negócio não fica espalhada nos controllers
- Reusabilidade: controllers e outros serviços podem chamar o mesmo service
- Transações gerenciadas com `@Transactional`

---

### 9.3 DTO Pattern (Data Transfer Object)

**O que é:** Objetos simples usados para transferir dados entre camadas, sem expor diretamente as entidades de domínio.

**Onde:** `SalaDTO` e `ReservaDTO`.

**Benefícios:**
- Desacopla a camada de apresentação da camada de domínio
- Permite validações específicas do contexto de entrada (Bean Validation)
- Evita que alterações nas entidades impactem diretamente os formulários

---

### 9.4 Strategy Pattern

**O que é:** Define uma família de algoritmos, encapsula cada um deles e os torna intercambiáveis.

**Onde:** `ReservaValidationStrategy` (interface) com implementações `ConflitoDePeriodoStrategy` e `SalaAtivaStrategy`.

**Benefícios:**
- Novas regras de validação são adicionadas sem modificar o `ReservaService` (OCP)
- Cada validação é testável de forma independente
- O Spring injeta automaticamente todas as implementações via lista

```
┌─────────────────────────────────────────────────┐
│              ReservaService                     │
│  validationStrategies.forEach(s -> s.validate)  │
└───────────────────────┬─────────────────────────┘
                        │ usa
           ┌────────────┴────────────┐
           ▼                         ▼
┌─────────────────────┐   ┌──────────────────────┐
│ConflitoDePeriodo    │   │  SalaAtiva           │
│Strategy             │   │  Strategy            │
│ (RN04 + RN07)       │   │                      │
└─────────────────────┘   └──────────────────────┘
```

---

## 10. Evidências de Testes

### 10.1 Testes Unitários — SalaServiceTest

| Método de Teste | Regra Validada | Resultado Esperado |
|---|---|---|
| `deveLancarExcecaoQuandoNomeDuplicado` | RN01 | `RegraDeNegocioException` |
| `devePermitirEdicaoComMesmoNome` | RN01 | Sem exceção |
| `deveIniciarSalaComoAtiva` | RN02 | `sala.ativa == true` |
| `deveCadastrarSalaComSucesso` | RN01 + RN02 | Sala salva com sucesso |
| `deveLancarExcecaoQuandoSalaInexistente` | — | `SalaNotFoundException` |
| `deveInativarSala` | — | `sala.ativa == false` |
| `deveListarSalasAtivas` | — | Apenas salas ativas |

### 10.2 Testes Unitários — ReservaServiceTest

| Método de Teste | Regra Validada | Resultado Esperado |
|---|---|---|
| `deveSolicitarReservaComStatusPendente` | RN03 | `status == PENDENTE` |
| `deveLancarExcecaoQuandoHouverConflito` | RN04 | `RegraDeNegocioException` |
| `deveAprovarReservaPendente` | RN05 | `status == APROVADA` |
| `deveLancarExcecaoAoAprovarReservaJaAprovada` | RN05 | `RegraDeNegocioException` |
| `deveLancarExcecaoAoAprovarReservaCancelada` | RN05 | `RegraDeNegocioException` |
| `deveCancelarReservaERegistrarDataCancelamento` | RN06 | `dataCancelamento != null` |
| `deveLancarExcecaoAoCancelarReservaJaCancelada` | — | `RegraDeNegocioException` |
| `deveLancarExcecaoQuandoReservaInexistente` | — | `ReservaNotFoundException` |
| `deveRejeitarReservaPendente` | RN05 (análogo) | `status == REJEITADA` |

### 10.3 Execução dos Testes

```bash
./mvnw test
```

**Saída esperada:**
```
[INFO] Tests run: 7, Failures: 0, Errors: 0, Skipped: 0 - SalaServiceTest
[INFO] Tests run: 9, Failures: 0, Errors: 0, Skipped: 0 - ReservaServiceTest
[INFO] BUILD SUCCESS
```

> Os testes utilizam **H2 (banco em memória)** e **Mockito**, sem necessidade de PostgreSQL.

---

## 11. Diagrama de Fluxo — Solicitar Reserva

```
Usuário                  ReservaController         ReservaService
   │                           │                         │
   │── POST /reservas/solicitar──►                       │
   │                           │── dto.nomeUsuario =     │
   │                           │   auth.getName() ──────►│
   │                           │                         │
   │                           │── solicitar(dto) ──────►│
   │                           │                         │── SalaAtivaStrategy.validate()
   │                           │                         │── ConflitoDePeriodoStrategy.validate()
   │                           │                         │── reservaRepository.save(reserva)
   │                           │◄── Reserva(PENDENTE) ───│
   │                           │                         │
   │◄── redirect:/reservas ────│
   │    (flash: "solicitada!") │
```

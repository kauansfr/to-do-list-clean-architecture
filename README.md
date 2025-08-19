---
title: To-Do List API (Java Technical Challenge)
description: API RESTful de To-Do List em Spring Boot com Clean Architecture
category: Negócio/Core, Infraestrutura
---

# To-Do List API

Aplicação Spring Boot 3.x (Java 21) seguindo Clean Architecture para gerenciar tarefas (To-Do List), com JDBC (SQL Server), documentação OpenAPI/Swagger e CI/CD (AWS - EC2, RDS).

## Visão geral

- Arquitetura em camadas (Core/Use Cases, Ports, Adapters/Infra e Delivery/Controller)
- Banco de dados SQL Server com schema inicial carregado em `schema.sql`
- Documentação interativa via Swagger UI
- Tratamento global de erros com payload consistente

### Stack

- Java 21, Spring Boot 3.x
- Spring Web, JDBC, Bean Validation, springdoc-openapi
- SQL Server (mssql-jdbc)

## Como executar (local)

1) Pré-requisitos
    - JDK 21
    - Maven (wrapper já incluso: `mvnw`)
    - SQL Server local acessível

2) Configuração de ambiente
    - Crie um arquivo `.env` na raiz (exemplo):

    ```properties
    DB_DRIVER=com.microsoft.sqlserver.jdbc.SQLServerDriver
    DB_URL=jdbc:sqlserver://localhost:1433;databaseName=NOME_DO_SEU_BANCO_DE_DADOS;encrypt=false;trustServerCertificate=true
    DB_USERNAME=<seu_usuario>
    DB_PASSWORD=<sua_senha>
    ```
    Observações:
    - `application.yaml` importa `optional:file:.env[.properties]` e usa essas variáveis para `spring.datasource.*`.
    - Não comite `.env` com credenciais reais.

3) Executar a aplicação

    ```bash
    ./mvnw spring-boot:run
    ```

4) Acessar remotamente documentação OpenAPI/Swagger
    - Swagger UI: *http://IP_PÚBLICO_DA_SUA_EC2:8080/swagger-ui/index.html*
    - Na seção "**Server Variables"**, adicione o IP público da sua EC2 no input **"host"**

## Deploy (CI/CD, EC2 e RDS)

Esta seção explica a execução da pipeline e infraestrutura de produção.

### CI/CD no GitHub Actions

- CI (`.github/workflows/ci.yml`):
	- Dispara em push/PR para `main`, `qa`, `dev`.
	- Passos: checkout → Oracle JDK 21 → cache Maven → build → test → upload reports em falha.
- CD (`.github/workflows/cd.yml`):
	- Executa quando o CI em `main` conclui com sucesso.
	- Passos: checkout → Oracle JDK 21 → build → SCP do JAR para EC2 (`/opt/todolist/releases`) → restart via SSH (`systemctl restart todolist`).
	- Secrets requeridos:
		- `EC2_HOST`: IP/DNS público
		- `EC2_USER`: usuário SSH (ex.: kauan)
		- `EC2_SSH_KEY`: chave privada com acesso à instância

Fluxo no servidor:
- Copia o último JAR para `/opt/todolist/app.jar` e reinicia `todolist.service`.
- Verifica status e imprime logs em falha.

### Setup da EC2 (Ubuntu + systemd)

1) Diretórios e service unit
    - Crie `/opt/todolist` e `/opt/todolist/releases`; ajuste permissões ao usuário do serviço.
    - Copie `deploy/systemd/todolist.service` para `/etc/systemd/system/todolist.service`.
    - Ajuste `User`/`Group` na service e confirme:
        - `WorkingDirectory=/opt/todolist`
        - `EnvironmentFile=-/opt/todolist/.env`
        - `ExecStart=/usr/bin/java $JAVA_OPTS -jar /opt/todolist/app.jar --spring.profiles.active=${SPRING_PROFILES_ACTIVE}`

2) Arquivo `.env` em `/opt/todolist/.env`
    - Use `deploy/.env.example` como base. Exemplo (sem segredos reais):

    ```bash
    # Database
    DB_DRIVER=com.microsoft.sqlserver.jdbc.SQLServerDriver
    DB_URL=jdbc:sqlserver://<your_rds_endpoint>:1433;databaseName=<your_database_name>;encrypt=false;trustServerCertificate=true
    DB_USERNAME=<your_username>
    DB_PASSWORD=<your_password>

    # Spring/Server
    SPRING_PROFILES_ACTIVE=default
    SERVER_PORT=8080

    # JVM (instâncias com pouca RAM)
    JAVA_OPTS=-XX:InitialRAMPercentage=10 -XX:MaxRAMPercentage=45 -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/opt/todolist/heapdump.hprof
    ```

3) Ativar serviço

    ```bash
    sudo systemctl daemon-reload
    sudo systemctl enable todolist
    sudo systemctl restart todolist
    sudo systemctl status todolist --no-pager -l
    ```

### RDS (SQL Server) e criação do banco

1) Na EC2, com `sqlcmd` instalado, conecte-se ao RDS e valide/crie o banco indicado na `DB_URL`:

    ```bash
    sqlcmd -S tcp:<endpoint>,1433 -U app_login -P 'SenhaForte!123' -d minhaaplicacao -Q "SELECT DB_NAME()" -N -C
    ```

    Se o banco não existir:

    ```bash
    sqlcmd -S tcp:<endpoint>,1433 -U app_login -P 'SenhaForte!123' -d master -Q "CREATE DATABASE minhaaplicacao;" -N -C
    ```

2) Atualize a `DB_URL` no `.env` para `databaseName=minhaaplicacao`.

### Resumo do fluxo de deploy
- Push na `main` → CI (build+test) → CD (build, SCP para EC2, restart service).
- EC2 executa via systemd lendo `/opt/todolist/.env` e aplicando limites de RAM via `JAVA_OPTS`.

## Arquitetura e pacotes

- Core (Domínio e Regras de Negócio)
	- `core/tasks/usecase/*UseCaseImpl.java` — Casos de uso (services de negócio)
	- `core/tasks/ports/TaskRepositoryPort.java` — Porta (abstração de persistência)
	- `core/tasks/*` — Entidades e enums (`Task`, `TaskStatus`)
- Infraestrutura
	- `infrastructure/persistence/impl/TaskServiceAdapter.java` (`@Repository`) — Adapter JDBC (JdbcTemplate)
	- `infrastructure/config/BeanConfig.java` (`@Configuration`) — Criação dos beans dos casos de uso
	- `infrastructure/config/OpenApiConfig.java` — Metadados do OpenAPI
- Delivery (Entrada/HTTP)
	- `infrastructure/delivery/impl/TaskControllerImpl.java` (`@RestController`) — Endpoints REST
	- `infrastructure/delivery/rest/TaskRest.java` — DTO API
	- `infrastructure/delivery/mappers/TaskRestMapper.java` — Mapper REST<->Domínio (conversão de timezone)
	- `infrastructure/delivery/exception/*` — Handler global e payload de erro

### Diagrama de sequência
```
Cliente -> Controller -> Mapper(REST->Domínio) -> UseCase -> Port -> Adapter(JdbcTemplate) -> DB
DB -> Adapter -> UseCase -> Mapper(Domínio->REST) -> Controller -> Cliente
```

## Modelo de dados

Tabela `dbo.Tasks` (criada se não existir):
- `TaskId` (IDENTITY, PK)
- `Title` (NVARCHAR(255), NOT NULL)
- `Description` (NVARCHAR(MAX), NULL)
- `CreatedAt` (DATETIME2(3), DEFAULT SYSUTCDATETIME())
- `UpdatedAt` (DATETIME2(3), NULL)
- `CompletedAt` (DATETIME2(3), NULL)
- `Status` (NVARCHAR(20), CHECK IN ["Não iniciado", "Em progresso", "Concluído"])

Enum `TaskStatus` (serializado como string):
- `NOT_STARTED` → "Não iniciado"
- `IN_PROGRESS` → "Em progresso"
- `COMPLETED` → "Concluído"

## Endpoints

Base path: `/v1/tasks`

- POST `/v1/tasks` — Criar tarefa (201)
- GET `/v1/tasks` — Listar tarefas (200)
- PUT `/v1/tasks/{id}` — Atualizar tarefa (200)
- DELETE `/v1/tasks/{id}` — Excluir tarefa (204)

Erros: 400 (validação), 404 (não encontrado), 500 (erro interno). Ver seção "Tratamento de erros".

## Services (Casos de Uso) — Métodos e Complexidade

| Método | Tipo | Complexidade | Responsabilidade | Anotações Spring |
|--------|------|--------------|------------------|------------------|
| `CreateTaskUseCaseImpl#execute(Task)` | Público | Baixa | Salva e retorna a tarefa persistida; se status = Concluído, marca `CompletedAt` e refaz leitura | N/A |
| `ListTasksUseCaseImpl#execute()` | Público | Baixa | Retorna todas as tarefas | N/A |
| `UpdateTaskUseCaseImpl#execute(Long id, Task task)` | Público | Média | Valida existência; se status = Concluído e ainda não concluída, marca `CompletedAt`; se deixar de ser Concluída e já tinha `CompletedAt`, limpa o campo; atualiza e retorna a tarefa | N/A |
| `DeleteTaskUseCaseImpl#execute(Long id)` | Público | Baixa | Valida existência e remove por id | N/A |

Notas:
- Os casos de uso são registrados via `@Bean` em `BeanConfig` (injeção por construtor via Port `TaskRepositoryPort`).

### Lógicas e Regras de Negócio

- Create: `saved = repository.save(task)` (retorna a entidade persistida com ID). Se `saved.status == COMPLETED`, `repository.updateCompletedAt(saved.id)` e `saved = repository.findById(saved.id)` para refletir `CompletedAt`.
- List: `repository.findAll()`.
- Update: valida `id != null && existsById(id)`; carrega `oldTask`. Se novo status = COMPLETED e `oldTask.completedAt == null`, `updateCompletedAt(id)`; se novo status ≠ COMPLETED e `oldTask.completedAt != null`, `clearCompletedAt(id)`; seta `task.id = id` e `updated = repository.update(task)`; retorna `updated`.
- Delete: valida `id != null && existsById(id)`; `repository.deleteById(id)`.

## Parâmetros, Retornos e Validações

DTO `TaskRest` (entrada/saída HTTP):
- `id` (Long) — somente leitura
- `title` (String) — `@NotBlank`
- `description` (String) — opcional
- `createdAt`, `updatedAt`, `completedAt` (LocalDateTime) — somente leitura
- `status` (TaskStatus) — `@NotNull` — valores: "Não iniciado", "Em progresso", "Concluído"

Validações e Exceções:
- Validação Bean Validation: `@Valid` no controller aplica `@NotBlank/@NotNull`.
- `TaskWithoutAllowedStatusException` quando status inválido é fornecido.
- `TaskNotFoundException` quando `id` ausente/inexistente em update/delete.

Tratamento de erros (`GlobalExceptionHandler`):
- 404 Not Found → `TaskNotFoundException`
- 400 Bad Request → `TaskWithoutAllowedStatusException`, `IllegalArgumentException`, validações (`MethodArgumentNotValidException`, `ConstraintViolationException`)
- 500 Internal Server Error → exceções genéricas

Payload de erro (`ErrorResponse`):
```json
{
	"timestamp": "2025-08-19T12:34:56Z",
	"status": 400,
	"error": "Bad request",
	"message": "Detalhes opcionais"
}
```

## Exemplos de uso (HTTP)

1) Criar tarefa (201):
    ```http
    POST /v1/tasks
    Content-Type: application/json

    {
        "title": "Estudar CI/CD",
        "description": "Ler docs do GitHub Actions",
        "status": "Não iniciado"
    }
    ```

2) Listar tarefas (200):
    ```http
    GET /v1/tasks
    ```

3) Atualizar tarefa (200):
    ```http
    PUT /v1/tasks/1
    Content-Type: application/json

    {
        "title": "Estudar CI/CD",
        "description": "Pipelines básicos",
        "status": "Concluído"
    }
    ```

4) Excluir tarefa (204):
    ```http
    DELETE /v1/tasks/1
    ```

Notas de datas/Timezone:
- O mapper converte datas do domínio (UTC) para America/Sao_Paulo ao expor via REST e vice-versa ao receber. No DTO os campos são read-only; a aplicação gera e mantém estes valores.

## Troubleshooting

Erros comuns:
- 400 Validation failed: título em branco ou status ausente/inválido.
- 404 Resource not found: `id` inexistente em update/delete.
- Conexão DB: verifique `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` no `.env` e se o SQL Server está ativo.

## OpenAPI

- Metadados configurados em `OpenApiConfig` com `@OpenAPIDefinition` e servidor parametrizado (`scheme`, `host`, `port`).
- UI padrão disponível em `/swagger-ui/index.html` (se executado localmente, defina a variável de servidor "host" como `localhost`; se executado em um servidor e está acessando remotamente, defina "host" com o IP público do servidor).
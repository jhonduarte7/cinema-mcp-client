# AGENTS.md - Workspace & Project Governance

This document describes the agent skills, architecture rules, project structure, and operational instructions for the **Cinema MCP Client of Spring AI**.

---

## 1. Project Overview

- **Project Name**: Cinema MCP Client
- **Group ID**: `com.venefast.springboot`
- **Artifact ID**: `cinema-mcp-client`
- **Base Package**: `com.venefast.springboot.cinemamcpclient.app`
- **Java Version**: Java 25 (Amazon Corretto 25 LTS)
- **Spring Boot Version**: 4.1.0
- **Spring AI BOM Version**: 2.0.1
- **LLM Provider**: Ollama (model: `llama3.2`, endpoint: `http://localhost:11434`)
- **MCP Client Transport**: Streamable HTTP (`spring-ai-starter-mcp-client`) targeting Movie MCP Server (`http://localhost:8090/mcp`)
- **Client REST Base**: `http://localhost:8091/api/cinema`

---

## 2. Active Skills & Responsibilities

| Skill Name | Location | Primary Role & Governance |
| :--- | :--- | :--- |
| **`spring-boot-best-practices-workspaces`** | `.agents/skills/spring-boot-best-practices-workspaces/SKILL.md` | Governs workspace-wide rules: Code in English, Communication in Spanish, Java 25 baseline, and skill orchestration. |
| **`spring-boot-best-practices`** | `.agents/skills/spring-boot-best-practices/SKILL.md` | Defines layered architecture patterns, Single DTO policy, record-based DTOs, dedicated mappers, Maven wrapper usage, and `application.properties` standard. |
| **`skill-creator`** | `.agents/skills/skill-creator/SKILL.md` | Guidelines for creating, evaluating, benchmarking, and maintaining agent skills. |

---

## 3. Strict Layered Architecture

The application enforces separation of concerns across the following layers:

```
src/main/java/com/venefast/springboot/cinemamcpclient/app/
├── CinemaMcpClientApplication.java          # Spring Boot bootstrap & entry point
├── config/
│   └── CinemaAssistantConfig.java           # Spring AI ChatClient setup with MCP ToolCallbackProvider
├── controllers/
│   └── CinemaAssistantController.java       # REST endpoints at /api/cinema returning ResponseEntity<DTO>
├── models/
│   └── dtos/
│       ├── CinemaPromptRequestDto.java      # Validated request payload contract
│       ├── CinemaPromptResponseDto.java     # AI inference response payload contract
│       └── CinemaStatusDto.java             # System & connectivity status contract
└── services/
    ├── CinemaAssistantService.java          # Business contract operating strictly on DTO records
    └── impl/
        └── CinemaAssistantServiceImpl.java  # Orchestration service invoking ChatClient and MCP tools
```

---

## 4. Spring AI & MCP Client Integration

### 4.1 Model Context Protocol (MCP) Setup
- **Starter**: `spring-ai-starter-mcp-client`
- **Protocol**: Streamable HTTP client (`spring.ai.mcp.client.streamable-http`)
- **Target Server**: Movie MCP Server at `http://localhost:8090/mcp`
- **Tool Resolution**: `SyncMcpToolCallbackProvider` dynamically registers remote tools (`searchMoviesByTitle`, `getMovieById`, `getMoviesByGenre`, `getTopRatedMovies`, `getAllMovies`, `getMovieScheduleByTitle`, etc.) directly into `ChatClient`.

### 4.2 Ollama Integration
- **Starter**: `spring-ai-starter-model-ollama`
- **Default Model**: `llama3.2`
- **Temperature**: `0.7`
- **Endpoint**: `http://localhost:11434`

---

## 5. REST API Endpoints

- `POST /api/cinema/ask`: Natural language query processed by Ollama with tool calls against Movie MCP Server.
  - **Payload**:
    ```json
    {
      "prompt": "What sci-fi movies are currently showing with rating above 8.5?",
      "userPreference": "Sci-Fi"
    }
    ```
- `GET /api/cinema/recommend?genre={genre}`: High-level recommendations tailored to a requested genre.
- `GET /api/cinema/status`: Checks readiness, Ollama model configuration, and MCP server connectivity.

---

## 6. How to Run & Verify

### Run the Application Locally
```cmd
mvnw.cmd -DskipTests spring-boot:run
```

### Run All Automated Tests
```cmd
mvnw.cmd test
```

### Access Points
- **REST API Base**: `http://localhost:8091/api/cinema`
- **Status Endpoint**: `http://localhost:8091/api/cinema/status`
- **Actuator Health**: `http://localhost:8091/actuator/health`

---

## 7. Project Health & Evaluation Scorecard

- **Automated Tests**: 8 tests passing (100% pass rate)
  - `CinemaMcpClientApplicationTests` (1 test): Spring Boot 4.1.0 context bootstrapping and Spring AI auto-configuration.
  - `CinemaAssistantControllerTest` (4 tests): MockMvc validation of request payloads, prompt queries, recommendations, and status endpoints.
  - `CinemaAssistantServiceTest` (3 tests): ChatClient orchestration, exception resilience, and status reporting.

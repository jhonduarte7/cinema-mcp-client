# Cinema MCP Client of Spring AI

An intelligent Cinema Assistant and Concierge built with **Spring Boot 4.1.0**, **Spring AI 2.0.1**, **Ollama**, and **Model Context Protocol (MCP) Client**.

---

## 🌟 Highlights

- **Spring Boot 4.1.0 & Java 25**: Built on modern Java 25 LTS and Spring Boot 4.
- **Spring AI MCP Client**: Automatically discovers and invokes remote tools exposed by the **Movie MCP Server** over Streamable HTTP (`http://localhost:8090/mcp`).
- **Ollama LLM Integration**: Powered by local LLM models (e.g. `llama3.2`) via `spring-ai-starter-model-ollama`.
- **Clean Configuration (`AIConfig`)**: Spring AI `ChatClient` is cleanly configured using `ObjectProvider<ToolCallbackProvider>` with `builder::defaultTools`, without hardcoding system prompts into infrastructure configuration.
- **Service-Layer Persona (`CineBot`)**: The conversational concierge persona and system prompt reside in the service layer (`CinemaAssistantServiceImpl`), ensuring proper separation of concerns.
- **Intelligent Tool Calling**: Natural language prompts are processed by Ollama, which autonomously invokes MCP tools (`searchMoviesByTitle`, `getMoviesByGenre`, `getTopRatedMovies`, `getMovieScheduleByTitle`, etc.) to answer user cinema queries.
- **Strict Layered Architecture**: Clear separation of concerns across controllers, services, configuration, and immutable record DTOs.
- **Automated Test Coverage**: 8 comprehensive automated unit and integration tests with a 100% pass rate.

---

## 🛠️ Architecture

```
src/main/java/com/venefast/springboot/cinemamcpclient/app/
├── CinemaMcpClientApplication.java          # Spring Boot application bootstrap
├── config/
│   └── AIConfig.java                        # Spring AI ChatClient setup with MCP ToolCallbackProvider
├── controllers/
│   └── CinemaAssistantController.java       # REST endpoints at /api/cinema returning ResponseEntity<DTO>
├── models/
│   └── dtos/
│       ├── CinemaPromptRequestDto.java      # Validated request payload contract
│       ├── CinemaPromptResponseDto.java     # AI inference response payload contract
│       └── CinemaStatusDto.java             # System readiness & connectivity status contract
└── services/
    ├── CinemaAssistantService.java          # Business contract operating strictly on DTO records
    └── impl/
        └── CinemaAssistantServiceImpl.java  # Orchestration service invoking ChatClient, MCP tools, and CineBot persona
```

---

## ⚙️ Configuration (`application.properties`)

```properties
# Server
server.port=8091
spring.application.name=cinema-mcp-client

# Ollama LLM
spring.ai.ollama.base-url=http://localhost:11434
spring.ai.ollama.chat.options.model=llama3.2
spring.ai.ollama.chat.options.temperature=0.7

# Spring AI MCP Client (Streamable HTTP)
spring.ai.mcp.client.enabled=true
spring.ai.mcp.client.name=cinema-mcp-client
spring.ai.mcp.client.version=1.0.0
spring.ai.mcp.client.type=SYNC
spring.ai.mcp.client.request-timeout=30s
spring.ai.mcp.client.toolcallback.enabled=true
spring.ai.mcp.client.streamable-http.connections.movie-server.url=http://localhost:8090
spring.ai.mcp.client.streamable-http.connections.movie-server.endpoint=/mcp

# Actuator
management.endpoints.web.exposure.include=health,info,metrics
management.endpoint.health.show-details=always
```

---

## 🚀 Getting Started

### Prerequisites
- **Java 25** (Amazon Corretto 25 LTS or compatible JDK)
- **Ollama** running locally on port 11434 with model installed:
  ```bash
  ollama run llama3.2
  ```
- **Movie MCP Server** running on port 8090:
  ```
  http://localhost:8090/mcp
  ```

### Run Application
```cmd
mvnw.cmd -DskipTests spring-boot:run
```

### Run Tests
```cmd
mvnw.cmd test
```

---

## 📡 REST API Endpoints

### 1. Ask Cinema Assistant
- **Endpoint**: `POST /api/cinema/ask`
- **Description**: Asks natural language questions. Ollama invokes remote MCP tools to retrieve movie information.
- **Request Body**:
  ```json
  {
    "prompt": "What sci-fi movies are currently showing with rating above 8.5?",
    "userPreference": "Sci-Fi"
  }
  ```
- **Example cURL**:
  ```bash
  curl -X POST http://localhost:8091/api/cinema/ask \
    -H "Content-Type: application/json" \
    -d "{\"prompt\": \"What sci-fi movies are currently showing?\", \"userPreference\": \"Sci-Fi\"}"
  ```

### 2. Movie Recommendations by Genre
- **Endpoint**: `GET /api/cinema/recommend?genre={genre}`
- **Description**: Returns curated recommendations for a given genre.
- **Example cURL**:
  ```bash
  curl http://localhost:8091/api/cinema/recommend?genre=Action
  ```

### 3. Service & MCP Connectivity Status
- **Endpoint**: `GET /api/cinema/status`
- **Description**: Checks readiness, Ollama model configuration, and MCP server connectivity.
- **Example cURL**:
  ```bash
  curl http://localhost:8091/api/cinema/status
  ```

### 4. Actuator Health
- **Endpoint**: `GET /actuator/health`
- **Description**: Standard Spring Boot health endpoint.

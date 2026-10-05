# Cinema MCP Client of Spring AI

An intelligent Cinema Assistant and Concierge built with **Spring Boot 4.1.0**, **Spring AI 2.0.1**, **Ollama**, and **Model Context Protocol (MCP) Client**.

---

## 🌟 Highlights

- **Spring Boot 4.1.0 & Java 25**: Utilizes the modern Java 25 runtime and Spring Boot 4 stack.
- **Spring AI MCP Client**: Automatically discovers and invokes remote tools exposed by **Movie MCP Server** over Streamable HTTP (`http://localhost:8090/mcp`).
- **Ollama LLM Integration**: Powered by local LLM models (such as `llama3.2`) via `spring-ai-starter-model-ollama`.
- **Intelligent Tool Calling**: Natural language prompts are reasoned by Ollama, which autonomously invokes MCP tools (`searchMoviesByTitle`, `getTopRatedMovies`, `getMovieScheduleByTitle`, etc.) to answer user cinema questions.
- **Strict Layered Architecture**: Clean separation between controllers, services, configuration, and immutable record DTOs.
- **Automated Test Coverage**: 8 comprehensive automated tests with a 100% pass rate.

---

## 🛠️ Architecture

```
src/main/java/com/venefast/springboot/cinemamcpclient/app/
├── CinemaMcpClientApplication.java
├── config/
│   └── CinemaAssistantConfig.java
├── controllers/
│   └── CinemaAssistantController.java
├── models/
│   └── dtos/
│       ├── CinemaPromptRequestDto.java
│       ├── CinemaPromptResponseDto.java
│       └── CinemaStatusDto.java
└── services/
    ├── CinemaAssistantService.java
    └── impl/
        └── CinemaAssistantServiceImpl.java
```

---

## 🚀 Getting Started

### Prerequisites
- **Java 25** (Amazon Corretto 25 LTS)
- **Ollama** running locally on port 11434 (`ollama run llama3.2`)
- **Movie MCP Server** running on port 8090 (`http://localhost:8090/mcp`)

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

- `POST /api/cinema/ask`: Query the AI assistant with natural language.
- `GET /api/cinema/recommend?genre={genre}`: Query tailored recommendations by genre.
- `GET /api/cinema/status`: Check client readiness and connection status.
- `GET /actuator/health`: Spring Boot Actuator health check.

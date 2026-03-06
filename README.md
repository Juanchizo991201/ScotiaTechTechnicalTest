z# Scotia Tech Technical Test - Student Management Microservice

## Project Overview

This project implements a reactive microservice using Spring Boot 3.4.1 and Spring WebFlux for managing students. The service exposes two REST endpoints that allow creating students and retrieving active students.

## Architecture

The project follows a **Layered Architecture** pattern with the following structure:

```
src/
├── main/java/com/projects/sbtechnicaltest/
│   ├── controller/       # REST endpoints (Controller layer)
│   ├── service/          # Business logic (Service layer)
│   │   └── impl/         # Service implementations
│   ├── repository/       # Data access layer
│   ├── model/            # Entity classes
│   ├── dto/              # Data Transfer Objects
│   ├── exception/        # Custom exceptions and error handling
│   └── SbTechnicalTestApplication.java
└── test/                 # Unit and integration tests
```

## Technology Stack

- **Java Version**: 17+
- **Spring Boot**: 3.4.1
- **Build Tool**: Gradle with Groovy
- **Database**: H2 (in-memory)
- **Reactive Framework**: Spring WebFlux, Project Reactor
- **Data Access**: Spring Data R2DBC
- **Validation**: Jakarta Validation API
- **Testing**: JUnit 5, Mockito, Spring Boot Test

## Key Features

### 1. Create Student Endpoint
- **URL**: `POST /api/students`
- **Request Body**: StudentDTO with id, nombre, apellido, estado, edad
- **Response**: 201 Created (empty body on success)
- **Error Handling**: 409 Conflict if student ID already exists

**Validations:**
- **ID**: Required, 1-50 characters, must be unique
- **Nombre (Name)**: Required, 1-100 characters
- **Apellido (Last Name)**: Required, 1-100 characters
- **Estado (Status)**: Required, must be "ACTIVO" or "INACTIVO"
- **Edad (Age)**: Required, must be between 1 and 120

### 2. Get Active Students Endpoint
- **URL**: `GET /api/students/active`
- **Response**: 200 OK with list of active students (JSON array)
- **Returns**: Only students with estado = "ACTIVO"

## Database Schema

```sql
CREATE TABLE IF NOT EXISTS student (
    id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    lastname VARCHAR(100) NOT NULL,
    status VARCHAR(10) NOT NULL,
    age INT NOT NULL
);
```

**Note**: Database columns (`name`, `lastname`, `status`, `age`) are mapped to Java fields (`nombre`, `apellido`, `estado`, `edad`) using `@Column` annotations.

## Building the Project

### Prerequisites
- JDK 17 or later
- Gradle 9.3.1 (included via gradle wrapper)

### Compile the Project

```bash
./gradlew build -x test
```

### Run Tests

```bash
./gradlew test
```

### Run the Application

```bash
./gradlew bootRun
```

The application will start on `http://localhost:8080`

## Testing

The project includes comprehensive tests across three layers:

### 1. Controller Layer Tests (StudentControllerTest)
- Tests REST endpoint behavior
- Validates HTTP status codes
- Tests request/response handling
- Uses WebTestClient with mocked services

### 2. Service Layer Tests (StudentServiceTest)
- Tests business logic
- Validates duplicate student handling
- Tests active students filtering
- Uses Mockito for repository mocking

### 3. Integration Tests (StudentIntegrationTest)
- End-to-end validation testing
- Tests input validation
- Tests error responses

### Run Specific Test Suite

```bash
# Run all tests
./gradlew test

# Run only controller tests
./gradlew test --tests StudentControllerTest

# Run only service tests
./gradlew test --tests StudentServiceTest

# Run only integration tests
./gradlew test --tests StudentIntegrationTest
```

## API Usage Examples

### Create a Student

```bash
curl -X POST http://localhost:8080/api/students \
  -H "Content-Type: application/json" \
  -d '{
    "id": "STU001",
    "nombre": "Juan",
    "apellido": "Pérez",
    "estado": "ACTIVO",
    "edad": 25
  }'
```

**Expected Response**: 201 Created (empty body)

### Get Active Students

```bash
curl http://localhost:8080/api/students/active
```

**Expected Response**:
```json
[
  {
    "id": "STU001",
    "nombre": "Juan",
    "apellido": "Pérez",
    "estado": "ACTIVO",
    "edad": 25
  }
]
```

### Error Example - Duplicate Student

```bash
# First request - succeeds
curl -X POST http://localhost:8080/api/students \
  -H "Content-Type: application/json" \
  -d '{"id": "STU002", "nombre": "Maria", "apellido": "González", "estado": "ACTIVO", "edad": 30}'

# Second request with same ID - fails with 409
curl -X POST http://localhost:8080/api/students \
  -H "Content-Type: application/json" \
  -d '{"id": "STU002", "nombre": "Maria", "apellido": "González", "estado": "ACTIVO", "edad": 30}'
```

**Expected Response**: 409 Conflict
```json
{
  "message": "No se pudo hacer la grabación. El estudiante con ID: STU002 ya existe.",
  "error": "STUDENT_ALREADY_EXISTS",
  "status": 409,
  "timestamp": "2024-03-03T18:00:00"
}
```

## Code Structure

### Model (Student.java)
- JPA entity with validation annotations
- Mapped to "student" table
- Contains all required fields with constraints

### DTOs
- **StudentDTO**: Input validation for API requests
- **ErrorResponse**: Standardized error responses

### Repository (StudentRepository.java)
- R2dbcRepository for reactive data access
- Custom queries for filtering by status

### Service Layer
- **StudentService**: Interface defining business operations
- **StudentServiceImpl**: Implementation with validation logic
- Handles duplicate checking and error cases

### Controller
- **StudentController**: REST endpoints
- Uses @Valid for request validation
- Handles HTTP status codes appropriately

### Exception Handling
- **GlobalExceptionHandler**: Centralized error handling
- Custom **StudentAlreadyExistsException** for business logic errors
- Validation error handling

## Configuration

### Application Properties

Located in `src/main/resources/application.properties`:

```properties
spring.application.name=SBTechnicalTest
spring.r2dbc.url=r2dbc:h2:mem:///testdb?options=DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
spring.r2dbc.username=sa
spring.r2dbc.password=
spring.r2dbc.initialization-mode=always
```

## Performance Considerations

- **Reactive Programming**: Uses Project Reactor for non-blocking I/O
- **In-Memory Database**: H2 provides fast, transient storage
- **Spring WebFlux**: Handles concurrent requests efficiently
- **R2DBC**: Reactive driver for database operations

## Testing Strategy

### Unit Tests
- Mock external dependencies
- Test individual units in isolation
- High code coverage for business logic

### Integration Tests
- Test API endpoints with real HTTP client
- Validate request/response serialization
- Test error handling

### Test Results
- Total Tests: 23
  - Controller Layer Tests: 9
  - Service Layer Tests: 5
  - Integration Tests: 8
  - Demo Script Tests: 1
- Pass Rate: 100%
- Coverage: Controller, Service, and API layers

## Troubleshooting

### Issue: Tests fail with database connection error
**Solution**: The tests use H2 in-memory database. Ensure no other instances are running.

### Issue: Port already in use
**Solution**: Change the port in `application.properties` or stop the process using port 8080.

### Issue: Gradle build fails
**Solution**: 
```bash
./gradlew clean build --refresh-dependencies
```

## Project Deliverables

✅ Reactive REST microservice with Spring WebFlux
✅ Student creation endpoint with validation and duplicate checking
✅ Get active students endpoint (filtered by status = "ACTIVO")
✅ H2 in-memory database persistence with R2DBC
✅ 23 comprehensive unit and integration tests
✅ Service layer tests with Mockito mocking
✅ Controller tests with WebTestClient
✅ Integration tests with in-memory database
✅ Global exception handling with custom error responses
✅ Complete documentation and quick start guide
✅ Gradle build configuration with Spring Boot 3.4.1
✅ Removed unused dependencies (R2dbcEntityTemplate)

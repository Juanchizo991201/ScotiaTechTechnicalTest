# Quick Start Guide - Scotia Tech Technical Test

## 1. Project Setup

### Clone/Open the Project
```bash
cd ScotiaTechTechnicalTest
```

### Build the Project
```bash
./gradlew clean build
```

## 2. Run Tests

### Execute All Tests
```bash
./gradlew test
```

### View Test Results
```bash
open build/reports/tests/test/index.html  # macOS
start build/reports/tests/test/index.html # Windows
xdg-open build/reports/tests/test/index.html # Linux
```

## 3. Start the Application

### Run the Microservice
```bash
./gradlew bootRun
```

The application will start on `http://localhost:8080`

## 4. Test the API

### Option A: Using cURL

#### Create a Student
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

#### Get Active Students
```bash
curl http://localhost:8080/api/students/active
```

### Option B: Using the Demo Script
```bash
chmod +x demo.sh
./demo.sh
```

## 5. Understanding the Architecture

### Layered Architecture Pattern

```
Request → Controller → Service → Repository → Database
  ↓          ↓          ↓          ↓            ↓
  HTTP      REST       Logic      R2DBC        H2
  API       Handler    Layer      Reactive    In-Memory
```

### Key Components

1. **Controller** (`StudentController.java`)
   - REST endpoints
   - Input validation
   - HTTP response mapping

2. **Service** (`StudentServiceImpl.java`)
   - Business logic
   - Duplicate checking
   - Data transformation

3. **Repository** (`StudentRepository.java`)
   - Database access
   - Reactive queries
   - Data persistence

4. **Exception Handler** (`GlobalExceptionHandler.java`)
   - Centralized error handling
   - Custom error responses

## 6. API Endpoints Summary

### POST /api/students
**Create a new student**

Request Body:
```json
{
  "id": "STU001",
  "nombre": "Juan",
  "apellido": "Pérez",
  "estado": "ACTIVO",
  "edad": 25
}
```

Responses:
- `201 Created` - Success
- `400 Bad Request` - Validation error
- `409 Conflict` - Duplicate ID

### GET /api/students/active
**Get all active students**

Response:
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

Response: `200 OK`

## 7. Validation Rules

| Field | Rules |
|-------|-------|
| id | Required, 1-50 chars, unique |
| nombre | Required, 1-100 chars |
| apellido | Required, 1-100 chars |
| estado | Required, "ACTIVO" or "INACTIVO" |
| edad | Required, 1-120 |

## 8. Test Results

Expected Results:
- ✅ 23 tests passing
- ✅ 100% pass rate
- ✅ All layers covered

## 9. Directory Structure

```
src/
├── main/
│   ├── java/com/projects/sbtechnicaltest/
│   │   ├── controller/     # REST API
│   │   ├── service/        # Business Logic
│   │   ├── repository/     # Data Access
│   │   ├── model/          # Entities
│   │   ├── dto/            # Transfer Objects
│   │   └── exception/      # Error Handling
│   └── resources/
│       ├── schema.sql      # Database
│       └── application.properties
└── test/
    ├── controller/         # Controller Tests
    ├── service/            # Service Tests
    └── integration/        # Integration Tests
```

## 10. Common Issues

### Issue: Port 8080 already in use
**Solution**: Change port in `application.properties` or kill process:
```bash
lsof -i :8080  # Find process
kill -9 <PID>  # Kill process
```

### Issue: Tests fail
**Solution**: Clean and rebuild:
```bash
./gradlew clean test
```

### Issue: Build fails
**Solution**: Refresh dependencies:
```bash
./gradlew clean build --refresh-dependencies
```

## 11. Key Technologies

- **Spring Boot 3.4.1** - Framework
- **Spring WebFlux** - Reactive Web
- **R2DBC** - Reactive Database
- **H2** - In-Memory Database
- **JUnit 5** - Testing
- **Mockito** - Mocking

## 12. Next Steps for Interview

1. **Understand the code**: Review each layer
2. **Run the tests**: Verify all tests pass
3. **Start the application**: Test endpoints manually
4. **Explain the architecture**: Be ready to discuss design decisions
5. **Show error handling**: Demonstrate validation and error responses

---

**Good luck with your presentation!**
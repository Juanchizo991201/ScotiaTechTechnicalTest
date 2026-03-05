package com.projects.sbtechnicaltest.integration;

import com.projects.sbtechnicaltest.dto.StudentDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@TestPropertySource(properties = {
    "spring.r2dbc.url=r2dbc:h2:mem:///testdb?options=DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
    "spring.r2dbc.username=sa",
    "spring.r2dbc.password="
})
@DisplayName("Student API Integration Tests")
class StudentIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    @DisplayName("Debe validar ID vacío")
    void testCreateStudentEmptyId() {
        StudentDTO studentDTO = StudentDTO.builder()
                .id("")
                .nombre("Luis")
                .apellido("Martínez")
                .estado("ACTIVO")
                .edad(35)
                .build();

        webTestClient.post()
                .uri("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(studentDTO)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("Debe validar nombre vacío")
    void testCreateStudentEmptyName() {
        StudentDTO studentDTO = StudentDTO.builder()
                .id("INT_EMPTY_NAME")
                .nombre("")
                .apellido("Pérez")
                .estado("ACTIVO")
                .edad(25)
                .build();

        webTestClient.post()
                .uri("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(studentDTO)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("Debe validar apellido vacío")
    void testCreateStudentEmptyLastName() {
        StudentDTO studentDTO = StudentDTO.builder()
                .id("INT_EMPTY_LAST")
                .nombre("Luis")
                .apellido("")
                .estado("ACTIVO")
                .edad(35)
                .build();

        webTestClient.post()
                .uri("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(studentDTO)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("Debe validar edad menor a 1")
    void testCreateStudentInvalidAge() {
        StudentDTO studentDTO = StudentDTO.builder()
                .id("INT_INVALID_AGE")
                .nombre("Carlos")
                .apellido("López")
                .estado("ACTIVO")
                .edad(0)
                .build();

        webTestClient.post()
                .uri("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(studentDTO)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("Debe validar edad mayor a 120")
    void testCreateStudentInvalidAgeOver() {
        StudentDTO studentDTO = StudentDTO.builder()
                .id("INT_INVALID_AGE_OVER")
                .nombre("Carlos")
                .apellido("López")
                .estado("ACTIVO")
                .edad(150)
                .build();

        webTestClient.post()
                .uri("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(studentDTO)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("Debe validar estado inválido")
    void testCreateStudentInvalidStatus() {
        StudentDTO studentDTO = StudentDTO.builder()
                .id("INT_INVALID_STATUS")
                .nombre("Ana")
                .apellido("Sánchez")
                .estado("INVALIDO")
                .edad(28)
                .build();

        webTestClient.post()
                .uri("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(studentDTO)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("Debe obtener lista de estudiantes activos")
    void testGetActiveStudents() {
        webTestClient.get()
                .uri("/api/students/active")
                .exchange()
                .expectStatus().isOk();
    }
}
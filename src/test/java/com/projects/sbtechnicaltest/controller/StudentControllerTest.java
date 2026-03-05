package com.projects.sbtechnicaltest.controller;

import com.projects.sbtechnicaltest.dto.StudentDTO;
import com.projects.sbtechnicaltest.exception.StudentAlreadyExistsException;
import com.projects.sbtechnicaltest.model.Student;
import com.projects.sbtechnicaltest.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@WebFluxTest(StudentController.class)
@DisplayName("StudentController Tests")
class StudentControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private StudentService studentService;

    private StudentDTO studentDTO;
    private Student student;

    @BeforeEach
    void setUp() {
        studentDTO = StudentDTO.builder()
                .id("ST001")
                .nombre("Juan")
                .apellido("Pérez")
                .estado("ACTIVO")
                .edad(25)
                .build();

        student = Student.builder()
                .id("ST001")
                .nombre("Juan")
                .apellido("Pérez")
                .estado("ACTIVO")
                .edad(25)
                .build();
    }

    @Test
    @DisplayName("Debe crear un estudiante correctamente con status 201")
    void testCreateStudentSuccess() {
        when(studentService.saveStudent(any(StudentDTO.class)))
                .thenReturn(Mono.empty());

        webTestClient.post()
                .uri("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(studentDTO)
                .exchange()
                .expectStatus().isCreated();

        verify(studentService, times(1)).saveStudent(any(StudentDTO.class));
    }

    @Test
    @DisplayName("Debe retornar 409 cuando el estudiante ya existe")
    void testCreateStudentAlreadyExists() {
        when(studentService.saveStudent(any(StudentDTO.class)))
                .thenReturn(Mono.error(new StudentAlreadyExistsException("Estudiante ya existe")));

        webTestClient.post()
                .uri("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(studentDTO)
                .exchange()
                .expectStatus().isEqualTo(409);

        verify(studentService, times(1)).saveStudent(any(StudentDTO.class));
    }

    @Test
    @DisplayName("Debe retornar 400 cuando hay error de validación en ID vacío")
    void testCreateStudentInvalidId() {
        StudentDTO invalidDTO = StudentDTO.builder()
                .id("")
                .nombre("Juan")
                .apellido("Pérez")
                .estado("ACTIVO")
                .edad(25)
                .build();

        webTestClient.post()
                .uri("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidDTO)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("Debe retornar 400 cuando el estado es inválido")
    void testCreateStudentInvalidStatus() {
        StudentDTO invalidDTO = StudentDTO.builder()
                .id("ST001")
                .nombre("Juan")
                .apellido("Pérez")
                .estado("INVALIDO")
                .edad(25)
                .build();

        webTestClient.post()
                .uri("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidDTO)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("Debe retornar 400 cuando la edad es menor a 1")
    void testCreateStudentInvalidAge() {
        StudentDTO invalidDTO = StudentDTO.builder()
                .id("ST001")
                .nombre("Juan")
                .apellido("Pérez")
                .estado("ACTIVO")
                .edad(0)
                .build();

        webTestClient.post()
                .uri("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidDTO)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("Debe obtener todos los estudiantes activos")
    void testGetAllActiveStudents() {
        Student student2 = Student.builder()
                .id("ST002")
                .nombre("Maria")
                .apellido("González")
                .estado("ACTIVO")
                .edad(30)
                .build();

        when(studentService.getAllActiveStudents())
                .thenReturn(Flux.just(student, student2));

        webTestClient.get()
                .uri("/api/students/active")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Student.class)
                .hasSize(2)
                .contains(student, student2);

        verify(studentService, times(1)).getAllActiveStudents();
    }

    @Test
    @DisplayName("Debe retornar lista vacía cuando no hay estudiantes activos")
    void testGetAllActiveStudentsEmpty() {
        when(studentService.getAllActiveStudents())
                .thenReturn(Flux.empty());

        webTestClient.get()
                .uri("/api/students/active")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Student.class)
                .hasSize(0);

        verify(studentService, times(1)).getAllActiveStudents();
    }

    @Test
    @DisplayName("Debe retornar 400 cuando el nombre está vacío")
    void testCreateStudentEmptyName() {
        StudentDTO invalidDTO = StudentDTO.builder()
                .id("ST001")
                .nombre("")
                .apellido("Pérez")
                .estado("ACTIVO")
                .edad(25)
                .build();

        webTestClient.post()
                .uri("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidDTO)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("Debe retornar 400 cuando el apellido está vacío")
    void testCreateStudentEmptyLastName() {
        StudentDTO invalidDTO = StudentDTO.builder()
                .id("ST001")
                .nombre("Juan")
                .apellido("")
                .estado("ACTIVO")
                .edad(25)
                .build();

        webTestClient.post()
                .uri("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidDTO)
                .exchange()
                .expectStatus().isBadRequest();
    }
}

package com.projects.sbtechnicaltest.service;

import com.projects.sbtechnicaltest.dto.StudentDTO;
import com.projects.sbtechnicaltest.exception.StudentAlreadyExistsException;
import com.projects.sbtechnicaltest.model.Student;
import com.projects.sbtechnicaltest.repository.StudentRepository;
import com.projects.sbtechnicaltest.service.impl.StudentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("StudentService Tests")
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentServiceImpl studentService;

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
    @DisplayName("Debe guardar un nuevo estudiante correctamente")
    void testSaveStudentSuccess() {
        when(studentRepository.existsById("ST001")).thenReturn(Mono.just(false));
        when(studentRepository.save(any(Student.class))).thenReturn(Mono.just(student));

        StepVerifier.create(studentService.saveStudent(studentDTO))
                .verifyComplete();

        verify(studentRepository, times(1)).existsById("ST001");
        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción si el estudiante ya existe")
    void testSaveStudentAlreadyExists() {
        when(studentRepository.existsById("ST001")).thenReturn(Mono.just(true));

        StepVerifier.create(studentService.saveStudent(studentDTO))
                .expectError(StudentAlreadyExistsException.class)
                .verify();

        verify(studentRepository, times(1)).existsById("ST001");
        verify(studentRepository, times(0)).save(any(Student.class));
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

        when(studentRepository.findByStatus("ACTIVO"))
                .thenReturn(Flux.just(student, student2));

        StepVerifier.create(studentService.getAllActiveStudents())
                .expectNext(student)
                .expectNext(student2)
                .verifyComplete();

        verify(studentRepository, times(1)).findByStatus("ACTIVO");
    }

    @Test
    @DisplayName("Debe retornar flujo vacío si no hay estudiantes activos")
    void testGetAllActiveStudentsEmpty() {
        when(studentRepository.findByStatus("ACTIVO"))
                .thenReturn(Flux.empty());

        StepVerifier.create(studentService.getAllActiveStudents())
                .verifyComplete();

        verify(studentRepository, times(1)).findByStatus("ACTIVO");
    }

    @Test
    @DisplayName("Debe contener el mensaje de error correcto cuando el estudiante ya existe")
    void testSaveStudentErrorMessage() {
        when(studentRepository.existsById("ST001")).thenReturn(Mono.just(true));

        StepVerifier.create(studentService.saveStudent(studentDTO))
                .expectErrorMatches(throwable ->
                        throwable instanceof StudentAlreadyExistsException &&
                        throwable.getMessage().contains("ya existe")
                )
                .verify();
    }
}
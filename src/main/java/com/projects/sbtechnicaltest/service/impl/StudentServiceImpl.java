package com.projects.sbtechnicaltest.service.impl;

import com.projects.sbtechnicaltest.dto.StudentDTO;
import com.projects.sbtechnicaltest.exception.StudentAlreadyExistsException;
import com.projects.sbtechnicaltest.model.Student;
import com.projects.sbtechnicaltest.repository.StudentRepository;
import com.projects.sbtechnicaltest.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    @Override
    public Mono<Void> saveStudent(StudentDTO studentDTO) {
        return studentRepository.existsById(studentDTO.getId())
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new StudentAlreadyExistsException(
                                "No se pudo hacer la grabación. El estudiante con ID: " + studentDTO.getId() + " ya existe."));
                    }
                    Student student = Student.builder()
                            .id(studentDTO.getId())
                            .nombre(studentDTO.getNombre())
                            .apellido(studentDTO.getApellido())
                            .estado(studentDTO.getEstado())
                            .edad(studentDTO.getEdad())
                            .isNew(true)
                            .build();
                    return studentRepository.save(student).then();
                });
    }

    @Override
    public Flux<Student> getAllActiveStudents() {
        return studentRepository.findByStatus("ACTIVO");
    }
}

package com.projects.sbtechnicaltest.service;

import com.projects.sbtechnicaltest.dto.StudentDTO;
import com.projects.sbtechnicaltest.model.Student;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface StudentService {
    Mono<Void> saveStudent(StudentDTO studentDTO);
    Flux<Student> getAllActiveStudents();
}
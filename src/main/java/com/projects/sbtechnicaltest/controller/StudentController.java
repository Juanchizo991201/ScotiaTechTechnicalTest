package com.projects.sbtechnicaltest.controller;

import com.projects.sbtechnicaltest.dto.StudentDTO;
import com.projects.sbtechnicaltest.model.Student;
import com.projects.sbtechnicaltest.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @PostMapping
    public Mono<ResponseEntity<Void>> saveStudent(@Valid @RequestBody StudentDTO studentDTO) {
        return studentService.saveStudent(studentDTO)
                .then(Mono.just(ResponseEntity.status(HttpStatus.CREATED).<Void>build()));
    }

    @GetMapping("/active")
    public Flux<Student> getAllActiveStudents() {
        return studentService.getAllActiveStudents();
    }
}
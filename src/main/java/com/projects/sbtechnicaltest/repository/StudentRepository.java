package com.projects.sbtechnicaltest.repository;

import com.projects.sbtechnicaltest.model.Student;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface StudentRepository extends R2dbcRepository<Student, String> {

    @Query("SELECT * FROM student WHERE status = :status")
    Flux<Student> findByStatus(@Param("status") String status);

    Mono<Student> findById(String id);
}
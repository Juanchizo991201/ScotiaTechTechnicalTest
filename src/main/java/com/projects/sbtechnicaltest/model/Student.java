package com.projects.sbtechnicaltest.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import jakarta.validation.constraints.*;
import java.util.Objects;

@Table("student")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student implements Persistable<String> {

    @Id
    @NotBlank(message = "El id no puede estar vacío")
    @Size(min = 1, max = 50, message = "El id debe tener entre 1 y 50 caracteres")
    private String id;

    @Column("name")
    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(min = 1, max = 100, message = "El nombre debe tener entre 1 y 100 caracteres")
    private String nombre;

    @Column("lastname")
    @NotBlank(message = "El apellido no puede estar vacío")
    @Size(min = 1, max = 100, message = "El apellido debe tener entre 1 y 100 caracteres")
    private String apellido;

    @Column("status")
    @NotNull(message = "El estado no puede estar nulo")
    @Pattern(regexp = "ACTIVO|INACTIVO", message = "El estado debe ser ACTIVO o INACTIVO")
    private String estado;

    @Column("age")
    @NotNull(message = "La edad no puede estar nula")
    @Min(value = 1, message = "La edad debe ser mayor a 0")
    @Max(value = 120, message = "La edad debe ser menor a 120")
    private Integer edad;

    @Transient
    @Builder.Default
    private boolean isNew = true;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return isNew;
    }

    public void setNotNew() {
        this.isNew = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(id, student.id) &&
               Objects.equals(nombre, student.nombre) &&
               Objects.equals(apellido, student.apellido) &&
               Objects.equals(estado, student.estado) &&
               Objects.equals(edad, student.edad);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, apellido, estado, edad);
    }
}
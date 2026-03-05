package com.projects.sbtechnicaltest.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentDTO {

    @NotBlank(message = "El id no puede estar vacío")
    @Size(min = 1, max = 50, message = "El id debe tener entre 1 y 50 caracteres")
    private String id;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(min = 1, max = 100, message = "El nombre debe tener entre 1 y 100 caracteres")
    private String nombre;

    @NotBlank(message = "El apellido no puede estar vacío")
    @Size(min = 1, max = 100, message = "El apellido debe tener entre 1 y 100 caracteres")
    private String apellido;

    @NotNull(message = "El estado no puede estar nulo")
    @Pattern(regexp = "ACTIVO|INACTIVO", message = "El estado debe ser ACTIVO o INACTIVO")
    private String estado;

    @NotNull(message = "La edad no puede estar nula")
    @Min(value = 1, message = "La edad debe ser mayor a 0")
    @Max(value = 120, message = "La edad debe ser menor a 120")
    private Integer edad;
}
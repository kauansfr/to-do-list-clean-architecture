package com.challenge.javatechnicalchallenge.infraestructure.delivery.rest;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class TaskRest {
    private Long id;
    @NotBlank
    private String title;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime completedAt;
    @NotBlank
    @Pattern(regexp = "^(Não iniciado|Em progresso|Concluído)$", message = "status deve ser 'Não iniciado', 'Em progresso' ou 'Concluído'")
    private String status;
}

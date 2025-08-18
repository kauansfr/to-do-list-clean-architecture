package com.challenge.javatechnicalchallenge.infraestructure.delivery.rest;

import java.time.LocalDateTime;

import com.challenge.javatechnicalchallenge.core.tasks.enums.TaskStatus;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(name = "Task", description = "Representa uma tarefa na To-Do List")
public class TaskRest {
    @Schema(description = "Identificador da tarefa", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    @NotBlank
    @Schema(description = "Título da tarefa", example = "Desenvolver Pipeline CI/CD")
    private String title;
    @Schema(description = "Descrição detalhada", example = "Estudar os conceitos de CI/CD e Github Actions")
    private String description;
    @Schema(description = "Data de criação (UTC)", example = "2025-08-16T17:45:00", accessMode = Schema.AccessMode.READ_ONLY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime createdAt;
    @Schema(description = "Data de atualização (UTC)", example = "2025-08-16T18:00:00", accessMode = Schema.AccessMode.READ_ONLY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime updatedAt;
    @Schema(description = "Data de conclusão (UTC)", example = "2025-08-17T09:30:00", accessMode = Schema.AccessMode.READ_ONLY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime completedAt;
    @NotNull
    @Schema(description = "Status atual", allowableValues = {"Não iniciado", "Em progresso", "Concluído"}, example = "Não iniciado")
    private TaskStatus status;
}

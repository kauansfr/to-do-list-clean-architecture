package com.challenge.javatechnicalchallenge.infrastructure.delivery.impl;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.challenge.javatechnicalchallenge.core.tasks.Task;
import com.challenge.javatechnicalchallenge.core.tasks.usecase.CreateTaskUseCase;
import com.challenge.javatechnicalchallenge.core.tasks.usecase.DeleteTaskUseCase;
import com.challenge.javatechnicalchallenge.core.tasks.usecase.ListTasksUseCase;
import com.challenge.javatechnicalchallenge.core.tasks.usecase.UpdateTaskUseCase;
import com.challenge.javatechnicalchallenge.infrastructure.delivery.TaskController;
import com.challenge.javatechnicalchallenge.infrastructure.delivery.mappers.TaskRestMapper;
import com.challenge.javatechnicalchallenge.infrastructure.delivery.rest.TaskRest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("v1/tasks")
@AllArgsConstructor
@Tag(name = "Tasks", description = "Operações para gerenciamento de tarefas")
public class TaskControllerImpl implements TaskController {
    private final CreateTaskUseCase createTaskUseCase;
    private final ListTasksUseCase listTasksUseCase;
    private final UpdateTaskUseCase updateTaskUseCase;
    private final DeleteTaskUseCase deleteTaskUseCase;

    @Override
    @PostMapping
    @Operation(summary = "Criar tarefa", description = "Cria uma nova tarefa")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tarefa criada"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno", content = @Content)
    })
    public ResponseEntity<TaskRest> createTask(@Valid @RequestBody TaskRest task) throws RuntimeException {
    Task domain = TaskRestMapper.toDomain(task);
    Task saved = createTaskUseCase.execute(domain);
    return ResponseEntity.status(HttpStatus.CREATED).body(TaskRestMapper.toRest(saved));
    }

    @Override
    @GetMapping
    @Operation(summary = "Listar tarefas", description = "Lista todas as tarefas")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de tarefas retornada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TaskRest.class)))
    })
    public ResponseEntity<List<TaskRest>> getTasks() throws RuntimeException {
        List<TaskRest> tasks = listTasksUseCase.execute().stream()
                .map(TaskRestMapper::toRest)
                .toList();
        return ResponseEntity.ok(tasks);
    }

    @Override
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar tarefa", description = "Atualiza uma tarefa existente. O atributo \"status\" deve receber \"Não iniciado\", \"Em progresso\" ou \"Concluído\"")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tarefa atualizada"),
            @ApiResponse(responseCode = "400", description = "ID ausente ou inválido", content = @Content)
    })
    public ResponseEntity<TaskRest> updateTask(@PathVariable Long id, @Valid @RequestBody TaskRest task) throws RuntimeException {
        if (id == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Task updated = updateTaskUseCase.execute(id, TaskRestMapper.toDomain(task));
        return ResponseEntity.ok(TaskRestMapper.toRest(updated));
    }

    @Override
    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir tarefa", description = "Exclui uma tarefa pelo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Tarefa excluída", content = @Content),
            @ApiResponse(responseCode = "400", description = "ID inválido", content = @Content)
    })
    public ResponseEntity<TaskRest> deleteTask(@Valid @PathVariable Long id) throws RuntimeException {
        if (id == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        deleteTaskUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}

package com.challenge.javatechnicalchallenge.infraestructure.delivery.impl;

import com.challenge.javatechnicalchallenge.core.tasks.Task;
import com.challenge.javatechnicalchallenge.core.tasks.usecase.CreateTaskUseCase;
import com.challenge.javatechnicalchallenge.core.tasks.usecase.DeleteTaskUseCase;
import com.challenge.javatechnicalchallenge.core.tasks.usecase.ListTasksUseCase;
import com.challenge.javatechnicalchallenge.core.tasks.usecase.UpdateTaskUseCase;
import com.challenge.javatechnicalchallenge.infraestructure.delivery.TaskController;
import com.challenge.javatechnicalchallenge.infraestructure.delivery.mappers.TaskRestMapper;
import com.challenge.javatechnicalchallenge.infraestructure.delivery.rest.TaskRest;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/tasks")
@AllArgsConstructor
public class TaskControllerImpl implements TaskController {
    private final CreateTaskUseCase createTaskUseCase;
    private final ListTasksUseCase listTasksUseCase;
    private final UpdateTaskUseCase updateTaskUseCase;
    private final DeleteTaskUseCase deleteTaskUseCase;

    @Override
    @PostMapping
    public ResponseEntity<TaskRest> createTask(@Valid @RequestBody TaskRest task) throws RuntimeException {
        Task domain = TaskRestMapper.toDomain(task);
        createTaskUseCase.execute(domain);

        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }

    @Override
    @GetMapping
    public ResponseEntity<List<TaskRest>> getTasks() throws RuntimeException {
        List<TaskRest> tasks = listTasksUseCase.execute().stream()
                .map(TaskRestMapper::toRest)
                .toList();
        return ResponseEntity.ok(tasks);
    }

    @Override
    @PutMapping
    public ResponseEntity<TaskRest> updateTask(@Valid @RequestBody TaskRest task) throws RuntimeException {
        if (task.getId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        updateTaskUseCase.execute(task.getId(), TaskRestMapper.toDomain(task));
        return ResponseEntity.ok(task);
    }

    @Override
    @DeleteMapping("{id}")
    public ResponseEntity<TaskRest> deleteTask(@Valid @PathVariable Long id) throws RuntimeException {
        if (id == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        deleteTaskUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}

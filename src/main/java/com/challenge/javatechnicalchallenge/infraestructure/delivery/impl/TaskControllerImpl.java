package com.challenge.javatechnicalchallenge.infraestructure.delivery.impl;

import com.challenge.javatechnicalchallenge.core.tasks.Task;
import com.challenge.javatechnicalchallenge.infraestructure.delivery.TaskController;
import com.challenge.javatechnicalchallenge.infraestructure.delivery.mappers.TaskRestMapper;
import com.challenge.javatechnicalchallenge.infraestructure.delivery.rest.TaskRest;


import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.challenge.javatechnicalchallenge.core.tasks.usecase.CreateTaskUseCase;

@RestController
@RequestMapping("v1/tasks")
@AllArgsConstructor
public class TaskControllerImpl implements TaskController {
    private final CreateTaskUseCase createTaskUseCase;

    @Override
    @PostMapping
    public ResponseEntity<TaskRest> createTask(@Valid @RequestBody TaskRest task) throws RuntimeException {
        Task domain = TaskRestMapper.toDomain(task);
        createTaskUseCase.execute(domain);
        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }

    // @Override
    // @GetMapping
    // public ResponseEntity<List<TaskRest>> getTasks() throws RuntimeException {

    // }

}

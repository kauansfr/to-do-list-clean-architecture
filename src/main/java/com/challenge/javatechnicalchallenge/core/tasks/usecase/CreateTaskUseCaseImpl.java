package com.challenge.javatechnicalchallenge.core.tasks.usecase;

import com.challenge.javatechnicalchallenge.core.tasks.Task;
import com.challenge.javatechnicalchallenge.core.tasks.ports.TaskRepositoryPort;

public class CreateTaskUseCaseImpl implements CreateTaskUseCase {
    private final TaskRepositoryPort taskRepositoryService;

    public CreateTaskUseCaseImpl(TaskRepositoryPort taskRepositoryService) {
        this.taskRepositoryService = taskRepositoryService;
    }

    @Override
    public void execute(Task task) {
        taskRepositoryService.save(task);
    }
}

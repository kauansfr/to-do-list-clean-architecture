package com.challenge.javatechnicalchallenge.core.tasks.usecase;

import com.challenge.javatechnicalchallenge.core.tasks.Task;
import com.challenge.javatechnicalchallenge.core.tasks.ports.TaskRepositoryPort;

public class UpdateTaskUseCaseImpl implements UpdateTaskUseCase {
    private final TaskRepositoryPort repository;

    public UpdateTaskUseCaseImpl(TaskRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public void execute(Long id, Task task) {
        task.setId(id);
        repository.update(task);
    }
}
package com.challenge.javatechnicalchallenge.core.tasks.usecase;

import com.challenge.javatechnicalchallenge.core.tasks.Task;
import com.challenge.javatechnicalchallenge.core.tasks.enums.TaskStatus;
import com.challenge.javatechnicalchallenge.core.tasks.ports.TaskRepositoryPort;

public class CreateTaskUseCaseImpl implements CreateTaskUseCase {
    private final TaskRepositoryPort repository;

    public CreateTaskUseCaseImpl(TaskRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public Task execute(Task task) {
        Task saved = repository.save(task);

    if (TaskStatus.COMPLETED.equals(saved.getStatus())) {
            repository.updateCompletedAt(saved.getId());
            saved = repository.findById(saved.getId());
        }
        return saved;
    }
}

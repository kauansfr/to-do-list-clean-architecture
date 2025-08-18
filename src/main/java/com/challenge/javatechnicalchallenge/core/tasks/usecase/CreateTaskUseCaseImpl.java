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
    public void execute(Task task) {
        repository.save(task);

        if (task.getStatus().equals(TaskStatus.COMPLETED)) {
            repository.updateCompletedAt(task.getId());
        }
    }
}

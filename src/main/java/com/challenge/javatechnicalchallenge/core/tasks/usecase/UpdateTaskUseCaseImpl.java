package com.challenge.javatechnicalchallenge.core.tasks.usecase;

import com.challenge.javatechnicalchallenge.core.tasks.Task;
import com.challenge.javatechnicalchallenge.core.tasks.enums.TaskStatus;
import com.challenge.javatechnicalchallenge.core.tasks.exception.TaskNotFoundException;
import com.challenge.javatechnicalchallenge.core.tasks.ports.TaskRepositoryPort;

public class UpdateTaskUseCaseImpl implements UpdateTaskUseCase {
    private final TaskRepositoryPort repository;

    public UpdateTaskUseCaseImpl(TaskRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public void execute(Long id, Task task) {
        if (id == null || !repository.existsById(id)) {
            throw new TaskNotFoundException("Tarefa não encontrada para o id: " + id);
        }

        if (task.getStatus().equals(TaskStatus.COMPLETED)) {
            Task oldTask = repository.findById(id);

            if (oldTask.getCompletedAt() == null) {
                repository.updateCompletedAt(id);
            }
        }

        task.setId(id);
        repository.update(task);
    }
}
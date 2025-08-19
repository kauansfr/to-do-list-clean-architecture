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
    public Task execute(Long id, Task task) {
        if (id == null || !repository.existsById(id)) {
            throw new TaskNotFoundException("Tarefa não encontrada para o id: " + id);
        }

        Task oldTask = repository.findById(id);
        boolean isTaskCompleted = TaskStatus.COMPLETED.equals(task.getStatus());
        boolean hasOldTaskCompletedAt = oldTask.getCompletedAt() != null;

        if (isTaskCompleted) {
            if (!hasOldTaskCompletedAt) {
                repository.updateCompletedAt(id);
            }
        } else {
            if (hasOldTaskCompletedAt) {
                repository.clearCompletedAt(id);
            }
        }

        task.setId(id);
        return repository.update(task);
    }
}
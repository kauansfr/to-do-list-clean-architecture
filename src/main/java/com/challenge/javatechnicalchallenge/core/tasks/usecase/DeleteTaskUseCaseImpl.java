package com.challenge.javatechnicalchallenge.core.tasks.usecase;

import com.challenge.javatechnicalchallenge.core.tasks.exception.TaskNotFoundException;
import com.challenge.javatechnicalchallenge.core.tasks.ports.TaskRepositoryPort;

public class DeleteTaskUseCaseImpl implements DeleteTaskUseCase {
    private final TaskRepositoryPort repository;

    public DeleteTaskUseCaseImpl(TaskRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public void execute(Long id) {
        if (id == null || !repository.existsById(id)) {
            throw new TaskNotFoundException("Tarefa não encontrada para o id=" + id);
        }
        repository.deleteById(id);
    }
}

package com.challenge.javatechnicalchallenge.core.tasks.usecase;

import com.challenge.javatechnicalchallenge.core.tasks.Task;
import com.challenge.javatechnicalchallenge.core.tasks.ports.TaskRepositoryPort;

import java.util.List;

public class ListTasksUseCaseImpl implements ListTasksUseCase {
    private final TaskRepositoryPort repository;

    public ListTasksUseCaseImpl(TaskRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public List<Task> execute() {
        return repository.findAll();
    }
}

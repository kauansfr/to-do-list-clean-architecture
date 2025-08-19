package com.challenge.javatechnicalchallenge.core.tasks.ports;

import java.util.List;

import com.challenge.javatechnicalchallenge.core.tasks.Task;

public interface TaskRepositoryPort {
    Task save(Task task);

    List<Task> findAll();

    Task update(Task task);

    void updateCompletedAt(Long id);
    void clearCompletedAt(Long id);

    void deleteById(Long id);

    Task findById(Long id);

    boolean existsById(Long id);
}

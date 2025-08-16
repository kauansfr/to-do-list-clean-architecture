package com.challenge.javatechnicalchallenge.core.tasks.ports;

import java.util.List;

import com.challenge.javatechnicalchallenge.core.tasks.Task;

public interface TaskRepositoryPort {
    void save(Task task);

    List<Task> findAll();

    void update(Task task);

    void deleteById(Long id);

    Boolean existsById(Long id);
}

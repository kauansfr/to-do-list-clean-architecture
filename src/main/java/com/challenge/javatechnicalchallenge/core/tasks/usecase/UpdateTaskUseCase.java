package com.challenge.javatechnicalchallenge.core.tasks.usecase;

import com.challenge.javatechnicalchallenge.core.tasks.Task;

public interface UpdateTaskUseCase {
    void execute(Long id, Task task);
}

package com.challenge.javatechnicalchallenge.core.tasks.usecase;

import com.challenge.javatechnicalchallenge.core.tasks.Task;

import java.util.List;

public interface ListTasksUseCase {
    List<Task> execute();
}
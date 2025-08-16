package com.challenge.javatechnicalchallenge.infraestructure.persistence.impl;

import com.challenge.javatechnicalchallenge.core.tasks.ports.TaskRepositoryPort;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TaskServiceAdapter implements TaskRepositoryPort {
    private final TaskRepositoryPort repository;


}

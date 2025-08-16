package com.challenge.javatechnicalchallenge.infraestructure.config;

import com.challenge.javatechnicalchallenge.core.tasks.ports.TaskRepositoryPort;
import com.challenge.javatechnicalchallenge.core.tasks.usecase.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {
    @Bean
    public CreateTaskUseCase createTaskUseCase(TaskRepositoryPort taskRepositoryPort) {
        return new CreateTaskUseCaseImpl(taskRepositoryPort);
    }

    @Bean
    public ListTasksUseCase listTasksUseCase(TaskRepositoryPort taskRepositoryPort) {
        return new ListTasksUseCaseImpl(taskRepositoryPort);
    }

    @Bean
    public UpdateTaskUseCase updateTaskUseCase(TaskRepositoryPort taskRepositoryPort) {
        return new UpdateTaskUseCaseImpl(taskRepositoryPort);
    }

    @Bean
    public DeleteTaskUseCase deleteTaskUseCase(TaskRepositoryPort taskRepositoryPort) {
        return new DeleteTaskUseCaseImpl(taskRepositoryPort);
    }
}

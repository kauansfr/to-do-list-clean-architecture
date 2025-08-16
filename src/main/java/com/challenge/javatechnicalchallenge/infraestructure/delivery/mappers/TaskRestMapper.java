package com.challenge.javatechnicalchallenge.infraestructure.delivery.mappers;

import com.challenge.javatechnicalchallenge.core.tasks.Task;
import com.challenge.javatechnicalchallenge.infraestructure.delivery.rest.TaskRest;

public class TaskRestMapper {
    public static TaskRest toRest(Task domain) {
        if (domain == null) return null;
        TaskRest taskRest = new TaskRest();
        taskRest.setId(domain.getId());
        taskRest.setTitle(domain.getTitle());
        taskRest.setDescription(domain.getDescription());
        taskRest.setCreatedAt(domain.getCreatedAt());
        taskRest.setUpdatedAt(domain.getUpdatedAt());
        taskRest.setCompletedAt(domain.getCompletedAt());
        taskRest.setStatus(domain.getStatus());

        return taskRest;
    }

    public static Task toDomain(TaskRest rest) {
        if (rest == null) return null;
        Task task = new Task();
        task.setId(rest.getId());
        task.setTitle(rest.getTitle());
        task.setDescription(rest.getDescription());
        task.setCreatedAt(rest.getCreatedAt());
        task.setUpdatedAt(rest.getUpdatedAt());
        task.setCompletedAt(rest.getCompletedAt());
        task.setStatus(rest.getStatus());

        return task;
    }
}
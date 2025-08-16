package com.challenge.javatechnicalchallenge.infraestructure.delivery.mappers;

import com.challenge.javatechnicalchallenge.core.tasks.Task;
import com.challenge.javatechnicalchallenge.infraestructure.delivery.rest.TaskRest;

public class TaskRestMapper {
    public static TaskRest toRest(Task domain) {
        if (domain == null) return null;
        TaskRest taskRest = new TaskRest();
        taskRest.setId(domain.getId());
        taskRest.setName(domain.getName());
        taskRest.setDescription(domain.getDescription());
        taskRest.setCreationDate(domain.getCreationDate());
        taskRest.setStatus(domain.getStatus());

        return taskRest;
    }

    public static Task toDomain(TaskRest rest) {
        if (rest == null) return null;
        Task task = new Task();
        task.setId(rest.getId());
        task.setName(rest.getName());
        task.setDescription(rest.getDescription());
        task.setCreationDate(rest.getCreationDate());
        task.setStatus(rest.getStatus());

        return task;
    }
}
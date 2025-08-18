package com.challenge.javatechnicalchallenge.infraestructure.delivery.mappers;

import java.time.LocalDateTime;
import java.time.ZoneId;

import com.challenge.javatechnicalchallenge.core.tasks.Task;
import com.challenge.javatechnicalchallenge.infraestructure.delivery.rest.TaskRest;

public class TaskRestMapper {
    private static final ZoneId UTC = ZoneId.of("UTC");
    private static final ZoneId BRASILIA = ZoneId.of("America/Sao_Paulo");

    private static LocalDateTime toBrazil(LocalDateTime utc) {
        if (utc == null) {
            return null;
        }
        return utc.atZone(UTC).withZoneSameInstant(BRASILIA).toLocalDateTime();
    }

    private static LocalDateTime toUtc(LocalDateTime brazil) {
        if (brazil == null) {
            return null;
        }
        return brazil.atZone(BRASILIA).withZoneSameInstant(UTC).toLocalDateTime();
    }

    public static TaskRest toRest(Task domain) {
        if (domain == null) {
            return null;
        }
        TaskRest taskRest = new TaskRest();
        taskRest.setId(domain.getId());
        taskRest.setTitle(domain.getTitle());
        taskRest.setDescription(domain.getDescription());
        taskRest.setCreatedAt(toBrazil(domain.getCreatedAt()));
        taskRest.setUpdatedAt(toBrazil(domain.getUpdatedAt()));
        taskRest.setCompletedAt(toBrazil(domain.getCompletedAt()));
        taskRest.setStatus(domain.getStatus());
        return taskRest;
    }

    public static Task toDomain(TaskRest rest) {
        if (rest == null) {
            return null;
        }
        Task task = new Task();
        task.setId(rest.getId());
        task.setTitle(rest.getTitle());
        task.setDescription(rest.getDescription());
        task.setCreatedAt(toUtc(rest.getCreatedAt()));
        task.setUpdatedAt(toUtc(rest.getUpdatedAt()));
        task.setCompletedAt(toUtc(rest.getCompletedAt()));
        task.setStatus(rest.getStatus());
        return task;
    }
}
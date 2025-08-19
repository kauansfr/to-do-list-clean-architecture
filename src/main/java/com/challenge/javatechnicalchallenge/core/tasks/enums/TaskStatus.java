package com.challenge.javatechnicalchallenge.core.tasks.enums;

import com.challenge.javatechnicalchallenge.core.tasks.exception.TaskWithoutAllowedStatusException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum TaskStatus {
    NOT_STARTED("Não iniciado"),
    IN_PROGRESS("Em progresso"),
    COMPLETED("Concluído");

    private final String formattedTitle;

    TaskStatus(String formattedTitle) {
        this.formattedTitle = formattedTitle;
    }

    @JsonValue
    public String toJson() {
        return formattedTitle;
    }

    @JsonCreator
    public static TaskStatus toStatusEnum(String status) {
        switch (status) {
            case "Não iniciado":
                return NOT_STARTED;
            case "Em progresso":
                return IN_PROGRESS;
            case "Concluído":
                return COMPLETED;
            default:
                throw new TaskWithoutAllowedStatusException();
        }
    }
}
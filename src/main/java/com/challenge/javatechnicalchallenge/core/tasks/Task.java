package com.challenge.javatechnicalchallenge.core.tasks;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Task {
    private Long id;
    private String name;
    private String description;
    private LocalDate creationDate;
    private String status;
}
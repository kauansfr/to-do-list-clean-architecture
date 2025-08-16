package com.challenge.javatechnicalchallenge.infraestructure.delivery.rest;

import java.time.LocalDate;

import lombok.Data;

@Data
public class TaskRest {
    private String name;
    private String description;
    private LocalDate creationDate;
    private String status;
}

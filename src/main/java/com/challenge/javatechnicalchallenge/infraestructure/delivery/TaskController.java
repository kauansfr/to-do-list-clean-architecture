package com.challenge.javatechnicalchallenge.infraestructure.delivery;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.challenge.javatechnicalchallenge.infraestructure.delivery.rest.TaskRest;

public interface TaskController {
    ResponseEntity<TaskRest> createTask(TaskRest task) throws RuntimeException;

    ResponseEntity<List<TaskRest>> getTasks() throws RuntimeException;

    ResponseEntity<TaskRest> updateTask(TaskRest task) throws RuntimeException;

    ResponseEntity<TaskRest> deleteTask(TaskRest task) throws RuntimeException;
}
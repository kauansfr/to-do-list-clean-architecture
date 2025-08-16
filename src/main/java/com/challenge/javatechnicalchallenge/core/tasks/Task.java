package com.challenge.javatechnicalchallenge.core.tasks;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class Task {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime completedAt;
    private String status;
}
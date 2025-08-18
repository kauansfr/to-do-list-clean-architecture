package com.challenge.javatechnicalchallenge.infraestructure.delivery.impl;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.challenge.javatechnicalchallenge.core.tasks.enums.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.challenge.javatechnicalchallenge.core.tasks.Task;
import com.challenge.javatechnicalchallenge.core.tasks.usecase.CreateTaskUseCase;
import com.challenge.javatechnicalchallenge.core.tasks.usecase.DeleteTaskUseCase;
import com.challenge.javatechnicalchallenge.core.tasks.usecase.ListTasksUseCase;
import com.challenge.javatechnicalchallenge.core.tasks.usecase.UpdateTaskUseCase;
import com.challenge.javatechnicalchallenge.infraestructure.delivery.rest.TaskRest;

@ExtendWith(MockitoExtension.class)
class TaskControllerImplTest {

    @Mock
    private CreateTaskUseCase createTaskUseCase;
    @Mock
    private ListTasksUseCase listTasksUseCase;
    @Mock
    private UpdateTaskUseCase updateTaskUseCase;
    @Mock
    private DeleteTaskUseCase deleteTaskUseCase;

    @InjectMocks
    private TaskControllerImpl controller;

    private TaskRest sampleRest;

    @BeforeEach
    void setUp() {
        sampleRest = new TaskRest();
        sampleRest.setId(1L);
        sampleRest.setTitle("Desenvolver Pipeline CI/CD");
        sampleRest.setDescription("Estudar os conceitos de CI/CD e Github Actions");
        sampleRest.setStatus(TaskStatus.NOT_STARTED);
        sampleRest.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void createTask_shouldReturn201_andCallUseCaseWithMappedDomain() {
        ResponseEntity<TaskRest> response = controller.createTask(sampleRest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(sampleRest, response.getBody());

        ArgumentCaptor<Task> captor = ArgumentCaptor.forClass(Task.class);
        verify(createTaskUseCase, times(1)).execute(captor.capture());
        Task sent = captor.getValue();
        assertEquals(sampleRest.getTitle(), sent.getTitle());
        assertEquals(sampleRest.getDescription(), sent.getDescription());
        assertEquals(sampleRest.getStatus(), sent.getStatus());
    }

    @Test
    void getTasks_shouldReturn200_withMappedList() {
        Task t1 = new Task();
        t1.setId(10L);
        t1.setTitle("Tarefa 1");
        t1.setDescription("Desc 1");
        t1.setStatus(TaskStatus.IN_PROGRESS);
        t1.setCreatedAt(LocalDateTime.now().minusHours(1));

        Task t2 = new Task();
        t2.setId(20L);
        t2.setTitle("Tarefa 2");
        t2.setDescription("Desc 2");
        t2.setStatus(TaskStatus.COMPLETED);
        t2.setCreatedAt(LocalDateTime.now().minusHours(2));

        when(listTasksUseCase.execute()).thenReturn(List.of(t1, t2));

        ResponseEntity<List<TaskRest>> response = controller.getTasks();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        TaskRest r1 = response.getBody().get(0);
        TaskRest r2 = response.getBody().get(1);
        assertEquals(t1.getId(), r1.getId());
        assertEquals(t1.getTitle(), r1.getTitle());
        assertEquals(t1.getStatus(), r1.getStatus());
        assertEquals(t2.getId(), r2.getId());
        assertEquals(t2.getTitle(), r2.getTitle());
        assertEquals(t2.getStatus(), r2.getStatus());

        verify(listTasksUseCase, times(1)).execute();
    }

    @Test
    void updateTask_shouldReturn200_andCallUseCase() {
        ResponseEntity<TaskRest> response = controller.updateTask(1L, sampleRest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sampleRest, response.getBody());

        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Task> taskCaptor = ArgumentCaptor.forClass(Task.class);
        verify(updateTaskUseCase, times(1)).execute(idCaptor.capture(), taskCaptor.capture());
        assertEquals(sampleRest.getId(), idCaptor.getValue());
        Task sent = taskCaptor.getValue();
        assertEquals(sampleRest.getTitle(), sent.getTitle());
        assertEquals(sampleRest.getDescription(), sent.getDescription());
        assertEquals(sampleRest.getStatus(), sent.getStatus());
    }

    @Test
    void updateTask_whenIdMissing_shouldReturn400_andNotCallUseCase() {
        TaskRest body = new TaskRest();
        body.setTitle("Sem ID");
        body.setStatus(TaskStatus.NOT_STARTED);

        ResponseEntity<TaskRest> response = controller.updateTask(body.getId(), body);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
        verifyNoInteractions(updateTaskUseCase);
    }

    @Test
    void deleteTask_shouldReturn204_andCallUseCase() {
        ResponseEntity<TaskRest> response = controller.deleteTask(99L);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(deleteTaskUseCase, times(1)).execute(99L);
    }

    @Test
    void deleteTask_whenIdNull_shouldReturn400_andNotCallUseCase() {
        ResponseEntity<TaskRest> response = controller.deleteTask(null);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verifyNoInteractions(deleteTaskUseCase);
    }
}

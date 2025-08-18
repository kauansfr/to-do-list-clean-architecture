package com.challenge.javatechnicalchallenge.infraestructure.persistence.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.challenge.javatechnicalchallenge.core.tasks.Task;
import com.challenge.javatechnicalchallenge.core.tasks.enums.TaskStatus;
import com.challenge.javatechnicalchallenge.core.tasks.ports.TaskRepositoryPort;

import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class TaskServiceAdapter implements TaskRepositoryPort {
    private final JdbcTemplate jdbcTemplate;

    private static final class TaskRowMapper implements RowMapper<Task> {
        @Override
        public Task mapRow(ResultSet sqlColumnResult, int rowNum) throws SQLException {
            Task task = new Task();
            task.setId(sqlColumnResult.getLong("TaskId"));
            task.setTitle(sqlColumnResult.getString("Title"));
            task.setDescription(sqlColumnResult.getString("Description"));
            Timestamp createdAt = sqlColumnResult.getTimestamp("CreatedAt");
            task.setCreatedAt(createdAt != null ? createdAt.toLocalDateTime() : null);
            Timestamp updatedAt = sqlColumnResult.getTimestamp("UpdatedAt");
            task.setUpdatedAt(updatedAt != null ? updatedAt.toLocalDateTime() : null);
            Timestamp completedAt = sqlColumnResult.getTimestamp("CompletedAt");
            task.setCompletedAt(completedAt != null ? completedAt.toLocalDateTime() : null);
            task.setStatus(TaskStatus.toStatusEnum(sqlColumnResult.getString("Status")));
            return task;
        }
    }

    private static String toDbStatus(TaskStatus status) {
        if (status == null) return null;
        return status.getFormattedTitle();
    }

    @Override
    public void save(Task task) {
        String sql = "INSERT INTO dbo.Tasks (Title, Description, Status)\n"
                + "VALUES (?, ?, ?);";
        jdbcTemplate.update(sql,
                task.getTitle(),
                task.getDescription(),
                toDbStatus(task.getStatus()));
    }

    @Override
    public List<Task> findAll() {
        String sql = "SELECT TaskId, Title, Description, CreatedAt, UpdatedAt, CompletedAt, Status\n"
                + "FROM dbo.Tasks";
        return jdbcTemplate.query(sql, new TaskRowMapper());
    }

    @Override
    public void update(Task task) {
        String sql = "UPDATE dbo.Tasks\n SET Title = ?,\n Description = ?,\n Status = ?,\n UpdatedAt = SYSUTCDATETIME()\n WHERE TaskId = ?";
        jdbcTemplate.update(sql,
                task.getTitle(),
                task.getDescription(),
                toDbStatus(task.getStatus()),
                task.getId());
    }

    @Override
    public void updateCompletedAt(Long id) {
        String sql = "UPDATE dbo.Tasks SET CompletedAt = SYSUTCDATETIME() WHERE TaskId = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public Task findById(Long id) {
        String sql = "SELECT * FROM dbo.Tasks WHERE TaskId = ?";
        return jdbcTemplate.queryForObject(sql, new TaskRowMapper(), id);
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM dbo.Tasks WHERE TaskId = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public boolean existsById(Long id) {
        String sql = "SELECT COUNT(1) FROM dbo.Tasks WHERE TaskId = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }
}

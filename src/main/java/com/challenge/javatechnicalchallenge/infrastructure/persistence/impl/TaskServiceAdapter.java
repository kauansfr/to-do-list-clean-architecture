package com.challenge.javatechnicalchallenge.infrastructure.persistence.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
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
    public Task save(Task task) {
        String insertSql = "INSERT INTO dbo.Tasks (Title, Description, Status) VALUES (?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, task.getTitle());
            ps.setString(2, task.getDescription());
            ps.setString(3, toDbStatus(task.getStatus()));
            return ps;
        }, keyHolder);
        Number key = keyHolder.getKey();
        Long newId = key != null ? key.longValue() : null;
        return newId != null ? findById(newId) : null;
    }

    @Override
    public List<Task> findAll() {
        String sql = "SELECT TaskId, Title, Description, CreatedAt, UpdatedAt, CompletedAt, Status FROM dbo.Tasks";
        return jdbcTemplate.query(sql, new TaskRowMapper());
    }

    @Override
    public Task update(Task task) {
        String sql = "UPDATE dbo.Tasks SET Title = ?, Description = ?, Status = ?, UpdatedAt = SYSUTCDATETIME() WHERE TaskId = ?";
        jdbcTemplate.update(sql,
                task.getTitle(),
                task.getDescription(),
                toDbStatus(task.getStatus()),
                task.getId());
        return findById(task.getId());
    }

    @Override
    public void updateCompletedAt(Long id) {
        String sql = "UPDATE dbo.Tasks SET CompletedAt = SYSUTCDATETIME() WHERE TaskId = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public void clearCompletedAt(Long id) {
        String sql = "UPDATE dbo.Tasks SET CompletedAt = NULL WHERE TaskId = ?";
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

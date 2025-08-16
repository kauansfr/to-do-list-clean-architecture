package com.challenge.javatechnicalchallenge.infraestructure.persistence.impl;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.challenge.javatechnicalchallenge.core.tasks.Task;
import com.challenge.javatechnicalchallenge.core.tasks.ports.TaskRepositoryPort;

import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class TaskServiceAdapter implements TaskRepositoryPort {
    private final JdbcTemplate jdbcTemplate;

    private static final class TaskRowMapper implements RowMapper<Task> {
        @Override
        public Task mapRow(ResultSet rs, int rowNum) throws SQLException {
            Task t = new Task();
            t.setId(rs.getLong("TaskId"));
            t.setTitle(rs.getString("Title"));
            t.setDescription(rs.getString("Description"));
            Timestamp createdAt = rs.getTimestamp("CreatedAt");
            t.setCreatedAt(createdAt != null ? createdAt.toLocalDateTime() : null);
            Timestamp updatedAt = rs.getTimestamp("UpdatedAt");
            t.setUpdatedAt(updatedAt != null ? updatedAt.toLocalDateTime() : null);
            Timestamp completedAt = rs.getTimestamp("CompletedAt");
            t.setCompletedAt(completedAt != null ? completedAt.toLocalDateTime() : null);
            t.setStatus(rs.getString("Status"));
            return t;
        }
    }

    @Override
    public void save(Task task) {
        String sql = "INSERT INTO dbo.Tasks (Title, Description, Status)\n" +
                "VALUES (?, ?, ?);";
        jdbcTemplate.update(sql,
                task.getTitle(),
                task.getDescription(),
                task.getStatus());
    }

    @Override
    public List<Task> findAll() {
        String sql = "SELECT TaskId, Title, Description, CreatedAt, UpdatedAt, CompletedAt, Status\n" +
                "FROM dbo.Tasks";
        return jdbcTemplate.query(sql, new TaskRowMapper());
    }

    @Override
    public void update(Task task) {
        String sql = "UPDATE dbo.Tasks\n SET Title = ?,\n Description = ?,\n Status = ?,\n UpdatedAt = SYSUTCDATETIME()\n WHERE TaskId = ?";
        jdbcTemplate.update(sql,
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getId());
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM dbo.Tasks WHERE TaskId = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public Boolean existsById(Long id) {
        String sql = "SELECT COUNT(1) FROM dbo.Tasks WHERE TaskId = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }
}

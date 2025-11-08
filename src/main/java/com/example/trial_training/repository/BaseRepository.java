package com.example.trial_training.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
public abstract class BaseRepository<T> {
//    protected final JdbcTemplate jdbcTemplate;
    protected final NamedParameterJdbcOperations jdbcOperations;
    protected abstract String getTableName();


    protected BaseRepository(NamedParameterJdbcOperations jdbcOperations) {
//        this.jdbcTemplate = jdbcTemplate;
        this.jdbcOperations = jdbcOperations;

    }

    protected abstract String getColumnName();
    protected abstract RowMapper<T> getRowMapper();

    public List<T> findByUsers() {
        String sql = "SELECT * FROM " + getTableName();

        try {
            return jdbcOperations.query(sql, getRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public T findById(Integer id) {
        String sql = "SELECT * FROM " + getTableName() + " WHERE id = :id";
        try {
            return jdbcOperations.queryForObject(sql, Map.of("id", id), getRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public abstract T create(T entity);


    protected T create(String sql, MapSqlParameterSource params) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcOperations.update(sql, params, keyHolder);

        Integer id = Objects.requireNonNull(keyHolder.getKey()).intValue();
        return findById(id);
    }
}

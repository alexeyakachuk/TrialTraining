package com.example.trial_training.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.OptimisticLockingFailureException;
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

    // метод для получения всех пользователей
    public List<T> findByUsers() {
        String sql = "SELECT * FROM " + getTableName();

        try {
            return jdbcOperations.query(sql, getRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    // получения пользователя по id
    public T findById(Integer id) {
        String sql = "SELECT * FROM " + getTableName() + " WHERE id = :id";
        try {
            return jdbcOperations.queryForObject(sql, Map.of("id", id), getRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    // создание пользователей
    public abstract T create(T entity);


    protected T create(String sql, MapSqlParameterSource params) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcOperations.update(sql, params, keyHolder);

        Integer id = Objects.requireNonNull(keyHolder.getKey()).intValue();
        return findById(id);
    }

    // получение по логину

    public T findByLogin(String login) {
        String sql = "SELECT * FROM " + getTableName() + " WHERE login = :login";
        try {
            return jdbcOperations.queryForObject(sql, Map.of("login", login), getRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    //обновление пользователя
    public abstract T update(T entity);

    protected T update(String sql, MapSqlParameterSource params, Integer id) {
        int rowsAffected = jdbcOperations.update(sql, params);
        if (rowsAffected == 0) {
            throw new OptimisticLockingFailureException("Не удалось обновить запись с ID: " + id + ". Возможно, она была изменена другим процессом.");
        }
        return findById(id);
    }
}

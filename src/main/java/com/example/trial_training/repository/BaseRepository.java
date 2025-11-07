package com.example.trial_training.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.RowMapper;

import java.lang.reflect.Field;
import java.util.List;

//@Repository
@Slf4j
public abstract class BaseRepository<T> {
    protected final JdbcTemplate jdbcTemplate;
    protected final JdbcOperations jdbcOperations;
    protected abstract String getTableName();


    protected BaseRepository(JdbcTemplate jdbcTemplate, JdbcOperations jdbcOperations) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcOperations = jdbcOperations;

    }

    protected abstract String getColumnName();
    protected abstract RowMapper<T> getRowMapper();

    public List<T> findByUsers() {
        String sql = "SELECT * FROM " + getTableName();

        try {
            return jdbcTemplate.query(sql, getRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public T findByUserId(Integer id) {
        String sql = "SELECT * FROM " + getTableName() + " WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, getRowMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

//    public T create(T ) {
//        log.info("Создаем нового клиента : {}", newClient.getName());
//        KeyHolder keyHolder = new GeneratedKeyHolder();
//        MapSqlParameterSource params = new MapSqlParameterSource();
//        params.addValue("name", newClient.getName());
//        params.addValue("surname", newClient.getSurname());
//        params.addValue("birthday", newClient.getBirthday());
//        params.addValue("telephone", newClient.getTelephone());
//        params.addValue("email", newClient.getEmail());
//        params.addValue("login", newClient.getLogin());
//        params.addValue("password", newClient.getPassword());
//
//        String sql = "INSERT INTO client (name, surname, birthday, telephone, email, login, password) " +
//                "VALUES (:name, :surname, :birthday, :telephone, :email, :login, :password)";
//        jdbcOperations.update(sql, params, keyHolder);
//
//        log.info("Новый клиент {} создан", newClient.getName());
//
//        Integer clientId = Objects.requireNonNull(keyHolder.getKey()).intValue();
//        return findClient(clientId);
//    }

    public T create(T entity, String tableName) {
        try {
            Class<?> clazz = entity.getClass(); // получаю необходимый объект для использования рефлексии
            Field[] fields = clazz.getDeclaredFields(); // получаю массив всех полей полученной сущьности
        }
    }
}

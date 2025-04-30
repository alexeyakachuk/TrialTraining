package com.example.TrialTraining.trainer.mapper;

import com.example.TrialTraining.trainer.model.Trainer;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class TrainerRowMapper implements RowMapper<Trainer> {
    @Override
    public Trainer mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Trainer.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .surname(rs.getString("surname"))
                .birthday(rs.getTimestamp("birthday").toLocalDateTime().toLocalDate())
                .telephone(rs.getString("telephone"))
                .email(rs.getString("email"))
                .login(rs.getString("login"))
                .build();
    }
}

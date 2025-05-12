package com.example.trial_training.client.mapper;

import com.example.trial_training.client.model.Client;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ClientRowMapper implements RowMapper<Client> {

    @Override
    public Client mapRow(ResultSet rs, int rowNum) throws SQLException {

        return Client.builder()
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

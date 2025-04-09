package com.example.TrialTraining.client.mapper;

import com.example.TrialTraining.client.dto.ClientDto;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ClientRowMapper implements RowMapper<ClientDto> {

    @Override
    public ClientDto mapRow(ResultSet rs, int rowNum) throws SQLException {

        return ClientDto.builder()
                .name(rs.getString("name"))
                .surname(rs.getString("surname"))
                .birthday(rs.getTimestamp("birthday").toLocalDateTime().toLocalDate())
                .telephone(rs.getString("telephone"))
                .email(rs.getString("email"))
                .build();
    }
}

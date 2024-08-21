package com.elazazy.customer;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class CustomerRowMapperTest {

    @Test
    void mapRow() throws SQLException {

        // Given
        CustomerRowMapper customerRowMapper = new CustomerRowMapper();
        ResultSet resultSet = Mockito.mock(ResultSet.class);
        when(resultSet.getLong("id")).thenReturn(1L);
        when(resultSet.getString("name")).thenReturn("name");
        when(resultSet.getString("email")).thenReturn("name@gmail.com");
        when(resultSet.getInt("age")).thenReturn(20);

        // When
        Customer customer = customerRowMapper.mapRow(resultSet, 1);

        // Then
        Customer expected = new Customer(1L, "name", "name@gmail.com", 20);
        assertEquals(expected, customer);
    }
}
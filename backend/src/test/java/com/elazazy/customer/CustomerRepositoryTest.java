package com.elazazy.customer;

import com.elazazy.AbstractTestContainersUnitTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.security.PrivateKey;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerRepositoryTest extends AbstractTestContainersUnitTest {
    private CustomerRepository underTest;

    @BeforeEach
    void setUp() {
    }

    @Test
    void existsCustomerById() {
        // Given
        // When

        // Then
    }

    @Test
    void existsCustomerByEmail() {
        // Given
        // When

        // Then
    }
}
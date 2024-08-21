package com.elazazy.customer;

import com.elazazy.AbstractTestContainersUnitTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

class CustomerJDBCDataAccessServiceTest extends AbstractTestContainersUnitTest {
    private CustomerJDBCDataAccessService underTest;
    private final CustomerRowMapper customerRowMapper = new CustomerRowMapper();

    @BeforeEach
    void setUp() {
        underTest = new CustomerJDBCDataAccessService(
                getJdbcTemplate(),
                customerRowMapper
        );
    }

    @Test
    void selectAllCustomers() {

        // Given
        Random random = new Random();
        String firstName = FAKER.name().firstName();
        String lastName = FAKER.name().lastName();
        Customer customer = new Customer(
                firstName + " " + lastName,
                lastName.toLowerCase() + '.' + firstName.toLowerCase() + random.nextInt(1, 99) + "@gmail.com",
                random.nextInt(16, 99)
        );
        underTest.insertCustomer(customer);

        // When
        List<Customer> actual = underTest.selectAllCustomers();

        // Then
        assertThat(actual).isNotEmpty();
    }

    @Test
    void selectCustomerByIdSucceed() {
        // Given
        Random random = new Random();
        String firstName = FAKER.name().firstName();
        String lastName = FAKER.name().lastName();
        String email = lastName.toLowerCase() +
                '.' +
                firstName.toLowerCase() +
                random.nextInt(1, 99) +
                "@gmail.com";

        Customer customer = new Customer(
                firstName + " " + lastName,
                email,
                random.nextInt(16, 99)
        );
        underTest.insertCustomer(customer);
        Long id = underTest.selectAllCustomers().stream()
                .filter(c -> c.getEmail().equals(email)).map(c -> c.getId())
                .findFirst().orElseThrow();

        // When
        Optional<Customer> actual = underTest.selectCustomerById(id);

        // Then
        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });
    }

    @Test
    void selectCustomerByIdFail() {
        // Given
        Long id = -1L;

        // When
        Optional<Customer> actual = underTest.selectCustomerById(id);

        // Then
        assertThat(actual).isEmpty();
    }

    @Test
    void insertCustomer() {
        // Given
        Random random = new Random();
        String firstName = FAKER.name().firstName();
        String lastName = FAKER.name().lastName();
        String email = lastName.toLowerCase() +
                '.' +
                firstName.toLowerCase() +
                random.nextInt(1, 99) +
                "@gmail.com";

        Customer customer = new Customer(
                firstName + " " + lastName,
                email,
                random.nextInt(16, 99)
        );
        underTest.insertCustomer(customer);
        Long id = underTest.selectAllCustomers().stream()
                .filter(c -> c.getEmail().equals(email)).map(c -> c.getId())
                .findFirst().orElseThrow();

        // When
        Optional<Customer> actual = underTest.selectCustomerById(id);

        // Then
        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });
    }

    @Test
    void existsPersonByEmailSucceed() {
        // Given
        Random random = new Random();
        String firstName = FAKER.name().firstName();
        String lastName = FAKER.name().lastName();
        String email = lastName.toLowerCase() +
                '.' +
                firstName.toLowerCase() +
                random.nextInt(1, 99) +
                "@gmail.com";

        Customer customer = new Customer(
                firstName + " " + lastName,
                email,
                random.nextInt(16, 99)
        );
        underTest.insertCustomer(customer);

        // When
        boolean actual = underTest.existsPersonByEmail(email);

        // Then
        assertThat(actual).isTrue();
    }

    @Test
    void existsPersonByEmailFail() {
        // Given
        String email = "a@a.com";

        // When
        boolean actual = underTest.existsPersonByEmail(email);

        // Then
        assertThat(actual).isFalse();
    }

    @Test
    void deleteCustomerById() {
        // Given
        Random random = new Random();
        String firstName = FAKER.name().firstName();
        String lastName = FAKER.name().lastName();
        String email = lastName.toLowerCase() +
                '.' +
                firstName.toLowerCase() +
                random.nextInt(1, 99) +
                "@gmail.com";

        Customer customer = new Customer(
                firstName + " " + lastName,
                email,
                random.nextInt(16, 99)
        );
        underTest.insertCustomer(customer);
        Long id = underTest.selectAllCustomers().stream()
                .filter(c -> c.getEmail().equals(email)).map(c -> c.getId())
                .findFirst().orElseThrow();

        // When
        underTest.deleteCustomerById(id);

        // Then
        assertThat(underTest.selectCustomerById(id)).isEmpty();
    }

    @Test
    void existsPersonByIdSucceed() {
        // Given
        Random random = new Random();
        String firstName = FAKER.name().firstName();
        String lastName = FAKER.name().lastName();
        String email = lastName.toLowerCase() +
                '.' +
                firstName.toLowerCase() +
                random.nextInt(1, 99) +
                "@gmail.com";

        Customer customer = new Customer(
                firstName + " " + lastName,
                email,
                random.nextInt(16, 99)
        );
        underTest.insertCustomer(customer);
        Long id = underTest.selectAllCustomers().stream()
                .filter(c -> c.getEmail().equals(email)).map(c -> c.getId())
                .findFirst().orElseThrow();

        // When
        boolean actual = underTest.existsPersonById(id);

        // Then
        assertThat(actual).isTrue();
    }

    @Test
    void existsPersonByIdFail() {
        // Given
        Random random = new Random();
        String firstName = FAKER.name().firstName();
        String lastName = FAKER.name().lastName();
        String email = lastName.toLowerCase() +
                '.' +
                firstName.toLowerCase() +
                random.nextInt(1, 99) +
                "@gmail.com";

        Customer customer = new Customer(
                firstName + " " + lastName,
                email,
                random.nextInt(16, 99)
        );
        underTest.insertCustomer(customer);
        Long id = -1L;

        // When
        boolean actual = underTest.existsPersonById(id);

        // Then
        assertThat(actual).isFalse();
    }

    @Test
    void editCustomer() {
        // Given
        // When

        // Then
    }
}
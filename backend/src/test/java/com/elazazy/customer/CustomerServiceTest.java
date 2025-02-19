package com.elazazy.customer;

import com.elazazy.exception.DuplicateResourceException;
import com.elazazy.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

//@ExtendWith(MockitoExtension.class) we could use this instead of AutoCloseable autoCloseable = MockitoAnnotations.openMocks(this); & autoCloseable.close(); in the teardown
@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {
    @Mock
    private CustomerDao customerDao;
    @Mock
    private PasswordEncoder passwordEncoder;
    private CustomerService underTest;
    private final CustomerDTOMapper customerDTOMapper = new CustomerDTOMapper();
//    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
//        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new CustomerService(customerDao, customerDTOMapper, passwordEncoder);
    }

//    @AfterEach
//    void tearDown() throws Exception {
//        autoCloseable.close();
//    }

    @Test
    void getAllCustomers() {

        // When
        underTest.getAllCustomers();

        // Then
        verify(customerDao).selectAllCustomers();
    }

    @Test
    void canGetCustomer() {

        // Given
        Long id = 10L;
        Customer customer = new Customer(id, "name", "name@gmail.com", "password", 20, Gender.Male);
        Mockito.when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        CustomerDTO expected = customerDTOMapper.apply(customer);

        // When
        CustomerDTO actual = underTest.getCustomer(id);

        // Then
        assertEquals(actual, expected);
    }

    @Test
    void cannotGetCustomer() {

        // Given
        Long id = 10L;
        Mockito.when(customerDao.selectCustomerById(id))
                .thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() -> underTest.getCustomer(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Customer with id " + id + " does not exist");
    }

    @Test
    void canRegisterCustomer() {

        // Given
        String email = "name@gmail.com";
        when(customerDao.existsPersonByEmail(email)).thenReturn(false);
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                "name", email, "password", 20, Gender.Male
        );

        String passwordHash = "$foobar$foobar$foobar$foobar";
        when(passwordEncoder.encode(request.password())).thenReturn(passwordHash);
        // When
        underTest.registerCustomer(request);

        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).insertCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();
        assertThat(capturedCustomer.getId()).isNull();
        assertThat(capturedCustomer.getName()).isEqualTo(request.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(request.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(request.age());
        assertThat(capturedCustomer.getGender()).isEqualTo(request.gender());
        assertThat(capturedCustomer.getPassword()).isEqualTo(passwordHash);
    }

    @Test
    void cannotRegisterCustomer() {

        // Given
        String email = "name@gmail.com";
        when(customerDao.existsPersonByEmail(email)).thenReturn(true);
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                "name", email, "password", 20, Gender.Male
        );

        // When
        assertThatThrownBy(() -> underTest.registerCustomer(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Email already taken");

        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDao, never()).insertCustomer(any());
    }

    @Test
    void deleteCustomer() {
        // Given
        // When

        // Then
    }

    @Test
    void updateCustomer() {
        // Given
        // When

        // Then
    }
}
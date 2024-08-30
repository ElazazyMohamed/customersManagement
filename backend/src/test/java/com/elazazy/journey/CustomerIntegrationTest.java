package com.elazazy.journey;

import com.elazazy.customer.Customer;
import com.elazazy.customer.CustomerRegistrationRequest;
import com.elazazy.customer.CustomerUpdateRequest;
import com.elazazy.customer.Gender;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class CustomerIntegrationTest {
    @Autowired
    private WebTestClient webTestClient;
    private static final String CUSTOMER_URI = "/api/v1/customers";

    @Test
    void canRegisterACustomer() {
        // Create a registration request
        Faker faker = new Faker();
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String name = firstName + " " + lastName;
        Integer age = faker.number().numberBetween(16, 99);
        Gender gender = age%2 == 0 ? Gender.Male : Gender.Female;
        String email = lastName.toLowerCase() + '.' + firstName.toLowerCase() + age + "@gmail.com";
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(name, email, age, gender);
        // send a post request
        webTestClient.post()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        // get all customers
        List<Customer> allCustomers = webTestClient.get()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {})
                .returnResult()
                .getResponseBody();

        // make sure the customer is present
        Customer expectedCustomer = new Customer(name, email, age, gender);
        assertThat(allCustomers)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expectedCustomer);

        // get customer by id
        Long id = (long) allCustomers.stream()
                        .filter(customer -> customer.getEmail()
                        .equals(email))
                        .map(Customer::getId)
                        .findFirst()
                        .orElseThrow();
        expectedCustomer.setId(id);

        Customer actualCustomer = webTestClient.get()
                .uri(CUSTOMER_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Customer.class)
                .returnResult()
                .getResponseBody();

        assertThat(actualCustomer).isEqualTo(expectedCustomer);
    }

    @Test
    void canDeleteACustomer() {
        // Create a registration request
        Faker faker = new Faker();
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String name = firstName + " " + lastName;
        Integer age = faker.number().numberBetween(16, 99);
        Gender gender = age%2 == 0 ? Gender.Male : Gender.Female;
        String email = lastName.toLowerCase() + '.' + firstName.toLowerCase() + age + "@gmail.com";
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(name, email, age, gender);
        // send a post request
        webTestClient.post()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        // get all customers
        List<Customer> allCustomers = webTestClient.get()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {})
                .returnResult()
                .getResponseBody();

        // get customer by id
        Long id = (long) allCustomers.stream()
                .filter(customer -> customer.getEmail()
                        .equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // delete customer
        webTestClient.delete()
                .uri(CUSTOMER_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk();

        webTestClient.get()
                .uri(CUSTOMER_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void canUpdateACustomer() {
        // Create a registration request
        Faker faker = new Faker();
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String name = firstName + " " + lastName;
        Integer age = faker.number().numberBetween(16, 99);
        Gender gender = age%2 == 0 ? Gender.Male : Gender.Female;
        String email = lastName.toLowerCase() + '.' + firstName.toLowerCase() + age + "@gmail.com";
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(name, email, age, gender);

        // send a post request
        webTestClient.post()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        // get all customers
        List<Customer> allCustomers = webTestClient.get()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {})
                .returnResult()
                .getResponseBody();

        // get customer by id
        Long id = (long) allCustomers.stream()
                .filter(customer -> customer.getEmail()
                        .equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // create update request
        String requestFirstName = faker.name().firstName();
        String requestLastName = faker.name().lastName();
        String requestName = requestFirstName + " " + requestLastName;
        Integer requestAge = faker.number().numberBetween(16, 99);
        Gender requestGender = age%2 == 0 ? Gender.Female : Gender.Male;
        String requestEmail = requestLastName.toLowerCase() + '.' + requestFirstName.toLowerCase() + requestAge + ".updated@gmail.com";
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(requestName, requestEmail, requestAge, requestGender);
        Customer requestCustomer = new Customer(requestName, requestEmail, requestAge, requestGender);
        requestCustomer.setId(id);

        // update customer
        webTestClient.put()
                .uri(CUSTOMER_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(updateRequest), CustomerUpdateRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        // get the updated customer
        webTestClient.get()
                .uri(CUSTOMER_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Customer.class)
                .isEqualTo(requestCustomer);
    }
}

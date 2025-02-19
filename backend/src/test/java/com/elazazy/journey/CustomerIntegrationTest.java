package com.elazazy.journey;

import com.elazazy.customer.*;
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
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

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
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(name, email, "password", age, gender);
        // send a post request
        String jwtToken = webTestClient.post()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(Void.class)
                .getResponseHeaders()
                .get(AUTHORIZATION)
                .get(0);

        // get all customers
        List<CustomerDTO> allCustomers = webTestClient.get()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<CustomerDTO>() {})
                .returnResult()
                .getResponseBody();

        // get customer by id
        Long id = (long) allCustomers.stream()
                .filter(customer -> customer.email()
                        .equals(email))
                .map(CustomerDTO::id)
                .findFirst()
                .orElseThrow();

        // make sure the customer is present
        CustomerDTO expectedCustomer = new CustomerDTO(
                id,
                name,
                email,
                age,
                gender
        );

        assertThat(allCustomers).contains(expectedCustomer);

        CustomerDTO actualCustomer = webTestClient.get()
                .uri(CUSTOMER_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(CustomerDTO.class)
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
        String email = lastName.toLowerCase() + '.' + firstName.toLowerCase() + age + "1@gmail.com";
        CustomerRegistrationRequest request1 = new CustomerRegistrationRequest(name, email,  "password", age, gender);
        CustomerRegistrationRequest request2 = new CustomerRegistrationRequest(name, email+".eg",  "password", age, gender);

        // send a post request to create customer 1
        webTestClient.post()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request1), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        // send a post request to create customer 2
        String jwtToken = webTestClient.post()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request2), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(Void.class)
                .getResponseHeaders()
                .get(AUTHORIZATION)
                .get(0);

        // get all customers
        List<CustomerDTO> allCustomers = webTestClient.get()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<CustomerDTO>() {})
                .returnResult()
                .getResponseBody();

        // get customer by id
        Long id = (long) allCustomers.stream()
                .filter(customer -> customer.email()
                        .equals(email))
                .map(CustomerDTO::id)
                .findFirst()
                .orElseThrow();

        // customer 2 delete customer 1
        webTestClient.delete()
                .uri(CUSTOMER_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus()
                .isOk();

        // customer 2 trys to get customer 1 by id
        webTestClient.get()
                .uri(CUSTOMER_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, String.format("Bearer %s", jwtToken))
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
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(name, email, "password", age, gender);

        // send a post request
        String jwtToken = webTestClient.post()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(Void.class)
                .getResponseHeaders()
                .get(AUTHORIZATION)
                .get(0);

        // get all customers
        List<CustomerDTO> allCustomers = webTestClient.get()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<CustomerDTO>() {})
                .returnResult()
                .getResponseBody();

        // get customer by id
        Long id = (long) allCustomers.stream()
                .filter(customer -> customer.email()
                        .equals(email))
                .map(CustomerDTO::id)
                .findFirst()
                .orElseThrow();

        CustomerDTO customertoUpdate = allCustomers.stream()
                .filter(customer -> customer.email().equals(email))
                .findFirst()  // Get the first matching customer (if any)
                .orElseThrow();

        // create update request
        String newName = "newName";
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                newName, null, null, null, null);
        CustomerDTO customerRequestUpdated = new CustomerDTO(
                customertoUpdate.id(),
                newName,
                customertoUpdate.email(),
                customertoUpdate.age(),
                customertoUpdate.gender()
        );

        // update customer
        webTestClient.put()
                .uri(CUSTOMER_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(updateRequest), CustomerUpdateRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        // get the updated customer
        CustomerDTO updatedCustomer = webTestClient.get()
                .uri(CUSTOMER_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(CustomerDTO.class)
                .returnResult()
                .getResponseBody();

        assertThat(updatedCustomer).isEqualTo(customerRequestUpdated);
    }
}

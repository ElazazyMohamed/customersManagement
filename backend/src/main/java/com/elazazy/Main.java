package com.elazazy;

import com.elazazy.customer.Customer;
import com.elazazy.customer.CustomerRepository;
import com.elazazy.customer.Gender;
import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import java.util.Random;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(Main.class, args);
    }

    @Bean
    CommandLineRunner runner(CustomerRepository customerRepository) {
        return args -> {
            for(int i=0; i<10; i++) {
                Faker faker = new Faker();
                Random random = new Random();
                String firstName = faker.name().firstName();
                String lastName = faker.name().lastName();
                Integer age = random.nextInt(16, 99);
                Gender gender = age%2 == 0 ? Gender.Male : Gender.Female;
                Customer customer = new Customer(
                        firstName + " " + lastName,
                        lastName.toLowerCase() + '.' + firstName.toLowerCase() + random.nextInt(1, 99) + "@gmail.com",
                        age,
                        gender);
                customerRepository.save(customer);
            }
        };
    }
}

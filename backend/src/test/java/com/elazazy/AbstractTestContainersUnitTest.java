package com.elazazy;

import com.github.javafaker.Faker;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;

@Testcontainers
public abstract class AbstractTestContainersUnitTest {
    @Container
    protected static final PostgreSQLContainer<?> postgreSQLContainers =
            new PostgreSQLContainer<>("postgres:15")
                    .withDatabaseName("elazazy-dao-unit-test")
                    .withUsername("elazazy")
                    .withPassword("password")
                    .withExposedPorts(5432)
                    .waitingFor(new org.testcontainers
                            .containers.wait.strategy
                            .HostPortWaitStrategy()
                    );

    @BeforeAll
    static void beforeAll() {
        Flyway flyway = Flyway.configure().dataSource(
                postgreSQLContainers.getJdbcUrl(),
                postgreSQLContainers.getUsername(),
                postgreSQLContainers.getPassword()
        ).load();
        flyway.migrate();
    }

    @DynamicPropertySource
    private static void registerDataSourceProperties(DynamicPropertyRegistry propertyaRegistry) {
        propertyaRegistry.add(
                "spring.datasource.url",
                postgreSQLContainers::getJdbcUrl
        );
        propertyaRegistry.add(
                "spring.datasource.username",
                postgreSQLContainers::getUsername
        );
        propertyaRegistry.add(
                "spring.datasource.password",
                postgreSQLContainers::getPassword
        );
    }

    private static DataSource getDataSource() {
        return DataSourceBuilder.create()
                .driverClassName(postgreSQLContainers.getDriverClassName())
                .url(postgreSQLContainers.getJdbcUrl())
                .username(postgreSQLContainers.getUsername())
                .password(postgreSQLContainers.getPassword()).build();
    }

    protected static JdbcTemplate getJdbcTemplate() {
        return new JdbcTemplate(getDataSource());
    }

    protected static final Faker FAKER = new Faker();
}

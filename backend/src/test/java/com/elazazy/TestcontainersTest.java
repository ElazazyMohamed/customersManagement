package com.elazazy;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TestcontainersTest extends AbstractTestContainersUnitTest {

    @Test
    void canStartPostgresDB() {
        assertThat(postgreSQLContainers.isCreated()).isTrue();
        assertThat(postgreSQLContainers.isRunning()).isTrue();
    }
}

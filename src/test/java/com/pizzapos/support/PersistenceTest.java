package com.pizzapos.support;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public abstract class PersistenceTest {

    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:16-alpine")
                    .withDatabaseName("pizza_pos_test")
                    .withUsername("postgres")
                    .withPassword("12345");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {

        registry.add("spring.datasource.url", postgres::getJdbcUrl);

        registry.add("spring.datasource.username", postgres::getUsername);

        registry.add("spring.datasource.password", postgres::getPassword);

        registry.add("spring.datasource.driver-class-name", postgres::getDriverClassName);

    }

}

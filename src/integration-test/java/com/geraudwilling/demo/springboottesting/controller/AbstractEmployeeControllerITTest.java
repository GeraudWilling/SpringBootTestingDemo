package com.geraudwilling.demo.springboottesting.controller;

import org.junit.ClassRule;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;

@Sql("classpath:add_employees.sql") // Execute sql queries to pre-poluate container database before tests
@Transactional // Start a new transaction before all tests methods
@Rollback// Rollback transaction after all tests methods
@ContextConfiguration(initializers = {AbstractEmployeeControllerITTest.Initializer.class})
@TestPropertySource(properties = "classpath:application.properties")
public abstract class AbstractEmployeeControllerITTest {

    @ClassRule
    public static final PostgreSQLContainer postgreSQLContainer;


    static {
        postgreSQLContainer = new PostgreSQLContainer();
        postgreSQLContainer.withDatabaseName("postgres");
        postgreSQLContainer.withUsername("sa");
        postgreSQLContainer.withPassword("password");
    }

    static class Initializer
            implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "spring.datasource.url=" + postgreSQLContainer.getJdbcUrl(),
                    "spring.datasource.username=" + postgreSQLContainer.getUsername(),
                    "spring.datasource.password=" + postgreSQLContainer.getPassword(),
                    "spring.flyway.url=" + postgreSQLContainer.getJdbcUrl()
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }
}

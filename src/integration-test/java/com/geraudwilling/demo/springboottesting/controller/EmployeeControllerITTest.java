package com.geraudwilling.demo.springboottesting.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.geraudwilling.demo.springboottesting.SpringBootTestingApplication;
import com.geraudwilling.demo.springboottesting.entity.Employee;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.sql.DataSource;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
@SpringBootTest(classes = SpringBootTestingApplication.class,
        webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("classpath:add_employees.sql")
@ContextConfiguration(initializers = {EmployeeControllerITTest.Initializer.class})
public class EmployeeControllerITTest {

    @LocalServerPort
    int randomServerPort;

    @Autowired
    private MockMvc mvc;

    private ObjectMapper objectMapper;

    @ClassRule
    public static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer()
            .withDatabaseName("postgres")
            .withUsername("sa")
            .withPassword("password");

    static class Initializer
            implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            String url = postgreSQLContainer.getJdbcUrl();
            String driverClassName = postgreSQLContainer.getDriverClassName();
            postgreSQLContainer.setPortBindings(Arrays.asList(new String[]{"5432:5432"}));

            TestPropertyValues.of(
                    "spring.datasource.url" + postgreSQLContainer.getJdbcUrl(),
                    "spring.datasource.username=" + postgreSQLContainer.getUsername(),
                    "spring.datasource.password=" + postgreSQLContainer.getPassword(),
                    "spring.datasource.driver-class-name=" + postgreSQLContainer.getDriverClassName(),
                    "spring.jpa.hibernate.ddl-auto=create",
                    "spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect",
                    "spring.datasource.hikari.connectionTimeout=20000",
                    "spring.datasource.hikari.maximumPoolSize=5",
                    "feign.employee.url=http://dummy.restapiexample.com"
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }

    @Configuration
    static class DatasourceConfig {
        @Bean
        public DataSource datasource() {
            return DataSourceBuilder.create()
                    .driverClassName(postgreSQLContainer.getDriverClassName())
                    .url(postgreSQLContainer.getJdbcUrl())
                    .username(postgreSQLContainer.getUsername())
                    .password(postgreSQLContainer.getPassword())
                    .build();
        }
    }

    @Test
    public void getEmployee_shouldSuccess() throws Exception {
        // When
        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .get("/api/employees/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        //Then
        String content = result.getResponse().getContentAsString();
        Employee response = objectMapper.readValue(content, Employee.class);
        assertThat(response, is(notNullValue()));
        assertThat(response.getId(), is(1L));
    }

    @Test
    public void createEmployee_shouldSuccess() throws Exception {
        // When
        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Employee.builder().id(4L).employeeName("Bubu").build())))
                .andDo(print())
                .andReturn();
        // Then
        MockHttpServletResponse response = result.getResponse();
        Employee employee = objectMapper.readValue(response.getContentAsString(), Employee.class);
        assertThat(response.getStatus(), is(HttpStatus.CREATED.value()));
        assertThat(response, is(notNullValue()));
        assertThat(employee.getId(), is(4L));
    }

    @Test
    public void deleteEmployee_shouldSuccess() throws Exception {
        // When
        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .delete("/api/employees/3")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn();
        // Then
        assertThat(result.getResponse().getStatus(), is(HttpStatus.NO_CONTENT.value()));
    }

    @Test
    public void getEmployee_shouldReturn404WhenIdNotExistsAnymore() throws Exception {
        // Given
        String id = "4l";
        // When
        ResultActions resultActions = mvc.perform(MockMvcRequestBuilders
                .get("/api/employees/" + id)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());
        //Then
        resultActions.andExpect(status().isNotFound());
    }

    @Before()
    public void setUp() {
        objectMapper = new ObjectMapper();
    }
}

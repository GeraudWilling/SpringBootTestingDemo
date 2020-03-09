package com.geraudwilling.demo.springboottesting.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.geraudwilling.demo.springboottesting.entity.Employee;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.notNullValue;

import java.util.Arrays;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.junit.Assert.assertThat;

import org.hamcrest.collection.IsArray;
import org.testcontainers.containers.PostgreSQLContainer;


@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {EmployeeClientConfig.class})
@TestPropertySource(locations = "classpath:application.properties")
public class EmployeeClientITTest {

    private EmployeeClientResponse employeeClientResponse;
    private ObjectMapper objectMapper;


    @Autowired
    EmployeeClient employeeClient;

    @Test
    public void getEmployees_shouldSucess() throws JsonProcessingException {
        // When
        EmployeeClientResponse responseList = employeeClient.getEmployees();

        // Then
        assertThat(responseList.getStatus(), equalToIgnoringCase("success"));
        assertThat(responseList.getData(), is(notNullValue()));
        assertThat(responseList.getData().get(0).getEmployeeName(), is("Toto"));
        assertThat(responseList.getData().get(1).getEmployeeName(), is("Titi"));
    }

    @Before
    public void setUp() {
        Employee employee1 = Employee.builder()
                .id(1L)
                .employeeAge(25)
                .employeeSalary(2000)
                .employeeName("Toto")
                .profileImage("")
                .build();
        Employee employee2 = Employee.builder()
                .id(2L)
                .employeeAge(52)
                .employeeSalary(2000)
                .employeeName("Titi")
                .profileImage("")
                .build();
        employeeClientResponse = EmployeeClientResponse.builder()
                .data(Arrays.asList(new Employee[]{employee1, employee2}))
                .status("SUCCESS")
                .build();
        objectMapper = new ObjectMapper();
    }


}

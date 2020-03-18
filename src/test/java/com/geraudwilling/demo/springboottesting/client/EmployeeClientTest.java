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
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.junit.Assert.assertThat;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {EmployeeClientConfig.class})
@TestPropertySource(properties = {"feign.employee.url=http://localhost:12345"})
public class EmployeeClientTest {

    private EmployeeClientResponse employeeClientResponse;
    private ObjectMapper objectMapper;

    @Autowired
    EmployeeClient employeeClient;

    @ClassRule
    // Mock Http server
    public static WireMockRule wireMockRule = new WireMockRule(12345);


    @Test
    public void getEmployees_shouldSucess() throws JsonProcessingException {
        // Given
        stubFor(get(urlEqualTo("/api/v1/employees"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(objectMapper.writeValueAsString(employeeClientResponse))));

        // When
        EmployeeClientResponse responseList = employeeClient.getEmployees();

        // Then
        assertThat(responseList.getStatus(), equalToIgnoringCase("success"));
        assertThat(responseList.getData().size(), is(2));
        assertThat(responseList.getData().get(0).getEmployeeName(), is("Toto"));
        assertThat(responseList.getData().get(1).getEmployeeName(), is("Titi"));

    }

    @Before
    public void setUp(){
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

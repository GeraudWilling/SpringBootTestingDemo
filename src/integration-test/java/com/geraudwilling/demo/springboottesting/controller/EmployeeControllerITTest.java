package com.geraudwilling.demo.springboottesting.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.geraudwilling.demo.springboottesting.entity.Employee;
import com.geraudwilling.demo.springboottesting.exception.NotFoundException;
import com.geraudwilling.demo.springboottesting.service.EmployeeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerITTest extends  AbstractEmployeeControllerITTest{

    @Autowired
    private MockMvc mvc;

    @Autowired
    private EmployeeService employeeService;

    private ObjectMapper objectMapper;

    @Before()
    public void setUp(){
        objectMapper = new ObjectMapper();
    }


    @Test
    public void createEmployee_shouldSuccess() throws Exception {
        // Given
        Long employeeId = 4L;
        // When
        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Employee.builder().id(employeeId).employeeName("Bubu").build())))
                .andDo(print())
                .andReturn();
        // Then
        MockHttpServletResponse response = result.getResponse();
        Employee employee = objectMapper.readValue(response.getContentAsString(), Employee.class);
        assertThat(response.getStatus(), is(HttpStatus.CREATED.value()));
        assertThat(response, is(notNullValue()));
        assertThat(employee.getId(), is(employeeId));
        assertThat(employeeService.findEmployeeById(employeeId).getEmployeeName(),is("Bubu") );
    }


    @Test
    public void deleteEmployee_shouldSuccess() throws Exception {
        // Given
        Long employeeId = 1L;
        // When
        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .delete("/api/employees/" + employeeId)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn();
        // Then
        assertThrows(
                NotFoundException.class,
                () -> employeeService.findEmployeeById(employeeId)
        );

    }


}
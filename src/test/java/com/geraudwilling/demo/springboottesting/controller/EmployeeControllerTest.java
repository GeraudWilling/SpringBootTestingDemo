package com.geraudwilling.demo.springboottesting.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.geraudwilling.demo.springboottesting.client.EmployeeClientResponse;
import com.geraudwilling.demo.springboottesting.entity.Employee;
import com.geraudwilling.demo.springboottesting.service.EmployeeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmployeeController.class)
@RunWith(SpringRunner.class)
public class EmployeeControllerTest {

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private MockMvc mvc;

    private List<Employee> employees;
    private ObjectMapper objectMapper;

    @Test
    public void getEmployee_shouldSuccess() throws Exception {
        // Given
        when(employeeService.findEmployeeById(anyLong())).thenReturn(employees.get(0));
        // When
        MvcResult result = mvc.perform( MockMvcRequestBuilders
                .get("/api/employees/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        //Then
        String content = result.getResponse().getContentAsString();
        Employee response = objectMapper.readValue(content, Employee.class);
        assertThat(response, is(notNullValue()));
        assertThat(response.getId(),is(employees.get(0).getId()));
    }

    @Test
    public void getEmployee_shouldReturn400WhenIdIsNotNumber() throws Exception {
        // Given
        String id = "12l";
        // When
        ResultActions resultActions = mvc.perform( MockMvcRequestBuilders
                .get("/api/employees/"+ id)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());
        //Then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    public void getEmployees_shouldSuccess() throws Exception {
        // Given
        when(employeeService.findAllEmployees())
                .thenReturn(EmployeeClientResponse.builder().data(employees).status("success").build());
        // When
        MvcResult result = mvc.perform( MockMvcRequestBuilders
                .get("/api/employees/external"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        // Then
        String content = result.getResponse().getContentAsString();
        Employee[] response = objectMapper.readValue(content, Employee[].class);
        assertThat(response, is(notNullValue()));
        assertThat(response.length,is(2));
    }

    @Test
    public void getEmployees_shouldReturn500WhenNoResponse() throws Exception {
        // Given
        when(employeeService.findAllEmployees())
                .thenReturn(EmployeeClientResponse.builder().status("error").build());
        // When
        ResultActions resultActions = mvc.perform(MockMvcRequestBuilders
                .get("/api/employees/external"))
                .andDo(print());
        // Then
        resultActions.andExpect(status().isInternalServerError());
    }

    @Test
    public void createEmployee_shouldSuccess() throws Exception {
        // Given
        when(employeeService.save(any(Employee.class))).thenAnswer(i -> i.getArguments()[0]);
        // When
        MvcResult result = mvc.perform( MockMvcRequestBuilders
                .post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employees.get(0))))
                .andDo(print())
                .andReturn();
        // Then
        MockHttpServletResponse response = result.getResponse();
        Employee employee = objectMapper.readValue(response.getContentAsString(), Employee.class);
        assertThat(response.getStatus(), is(HttpStatus.CREATED.value()));
        assertThat(response, is(notNullValue()));
        assertThat(employee.getId(),is(employees.get(0).getId()));
    }

    @Test
    public void createEmployee_shouldReturn400WhenEmployeeJsonIsNotValid() throws Exception {
        // Given
        Employee employeeWithoutName = Employee.builder().id(1L).employeeAge(2).employeeSalary(2000).build();
        // When
        MvcResult result = mvc.perform( MockMvcRequestBuilders
                .post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeWithoutName)))
                .andDo(print())
                .andReturn();
        // Then
        assertThat(result.getResponse().getStatus(), is(HttpStatus.BAD_REQUEST.value()));

    }

    @Test
    public void deleteEmployee_shouldSuccess() throws Exception {
        // Given
        doNothing().when(employeeService).delete(anyLong());
        // When
        MvcResult result = mvc.perform( MockMvcRequestBuilders
                .delete("/api/employees/1")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn();
        // Then
        assertThat(result.getResponse().getStatus(), is(HttpStatus.NO_CONTENT.value()));
    }

    @Before()
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
        employees = Arrays.asList(new Employee[]{employee1, employee2});
        objectMapper = new ObjectMapper();
    }
}

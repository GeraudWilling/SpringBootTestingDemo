package com.geraudwilling.demo.springboottesting.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.geraudwilling.demo.springboottesting.client.EmployeeClientResponse;
import com.geraudwilling.demo.springboottesting.entity.Employee;
import com.geraudwilling.demo.springboottesting.service.EmployeeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.jdbc.Sql;
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

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
@Sql("classpath:add_employees.sql")
public class EmployeeControllerITTest {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private MockMvc mvc;

    private ObjectMapper objectMapper;

    @Test
    public void getEmployee_shouldSuccess() throws Exception {
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
        assertThat(response.getId(),is(1L));
    }

    @Test
    public void createEmployee_shouldSuccess() throws Exception {
        // When
        MvcResult result = mvc.perform( MockMvcRequestBuilders
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
        assertThat(employee.getId(),is(4L));
    }

    @Test
    public void deleteEmployee_shouldSuccess() throws Exception {
        // When
        MvcResult result = mvc.perform( MockMvcRequestBuilders
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
        ResultActions resultActions = mvc.perform( MockMvcRequestBuilders
                .get("/api/employees/"+ id)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());
        //Then
        resultActions.andExpect(status().isNotFound());
    }

    @Before()
    public void setUp(){
        objectMapper = new ObjectMapper();
    }
}

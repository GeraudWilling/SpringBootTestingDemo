package com.geraudwilling.demo.springboottesting.service;

import com.geraudwilling.demo.springboottesting.client.EmployeeClient;
import com.geraudwilling.demo.springboottesting.client.EmployeeClientTest;
import com.geraudwilling.demo.springboottesting.entity.Employee;
import com.geraudwilling.demo.springboottesting.exception.ConflictException;
import com.geraudwilling.demo.springboottesting.exception.NotFoundException;
import com.geraudwilling.demo.springboottesting.repository.EmployeeRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class EmployeeServiceTest {

    @Autowired
    // Spring injecte le bean grâce au TestConfiguration
    EmployeeService employeeService;

    @MockBean
    EmployeeClient employeeClient;

    @MockBean
    EmployeeRepository employeeRepository;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private Employee employee;


    @TestConfiguration
    // Ajout du Bean EmployeeServiceImpl dans le context de configuration Spring
    static class employeeServiceTestConfiguration{
        @Bean
        EmployeeService employeeServiceInjector(){
            return new EmployeeServiceImpl();
        }
    }

    @Before
    public void setUp(){
        employee = Employee.builder()
                .id(1L)
                .employeeAge(25)
                .employeeSalary(2000)
                .employeeName("Toto")
                .profileImage("")
                .build();
    }

    @Test
    public void save_ShouldSuccessWhenEmployeeNotAlreadyExists(){
        // Given
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(employeeRepository.save(any(Employee.class))).thenAnswer(i -> i.getArguments()[0]);
        // When
        Employee response = employeeService.save(employee);
        // Then
        assertThat(response.getId(), is(1L));
    }

    @Test
    public void save_shouldThrowExceptionIfEmployeeExists(){
        // Given
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(employee));
        expectedException.expect(ConflictException.class);
        // When
        Employee response = employeeService.save(employee);
        // Then
        expectedException.expectMessage("Employee already exists");
    }

    @Test
    public void  updateById_shouldSuccessWhenEmployeeExists(){
        // Given
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(employee));
        when(employeeRepository.save(Mockito.any(Employee.class))).thenAnswer(i -> i.getArguments()[0]);
        // When
        Employee response = employeeService.updateById(1L);
        // Then
        assertThat(response.getId(), is(1L));
    }

    @Test
    public void update_shouldThrowExceptionIfEmployeeExists(){
        // Given
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.empty());
        expectedException.expect(NotFoundException.class);
        // When
        Employee response = employeeService.updateById(1L);
    }

    @Test
    public void  delete_shouldSuccessWhenEmployeeExists(){
        // Given
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(employee));
        doNothing().when(employeeRepository).delete(any(Employee.class));
        // When
        employeeService.delete(1L);
        // Then
    }

    @Test
    public void delete_shouldThrowExceptionIfEmployeeExists(){
        // Given
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.empty());
        expectedException.expect(NotFoundException.class);
        // When
        employeeService.delete(1L);
    }





}

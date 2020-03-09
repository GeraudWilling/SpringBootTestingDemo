package com.geraudwilling.demo.springboottesting.service;

import com.geraudwilling.demo.springboottesting.client.EmployeeClient;
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
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
@DataJpaTest
public class EmployeeServiceITTest {

    @Autowired
    EmployeeService employeeService;

    @Autowired
    EmployeeClient employeeClient;

    @Autowired
    EmployeeRepository employeeRepository;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private Employee employee;

    @Before
    public void setUp() {
        employee = Employee.builder()
                .id(1L)
                .employeeAge(25)
                .employeeSalary(2000)
                .employeeName("Toto")
                .profileImage("")
                .build();
    }

}
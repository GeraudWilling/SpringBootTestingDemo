package com.geraudwilling.demo.springboottesting.service;

import com.geraudwilling.demo.springboottesting.client.EmployeeClientResponse;
import com.geraudwilling.demo.springboottesting.entity.Employee;

import java.util.List;


public interface EmployeeService {

    Employee save(Employee employee);
    EmployeeClientResponse findAllEmployees();
    Employee findEmployeeById(Long id);
    Employee updateById(Long id);
    void delete(Long id);
}

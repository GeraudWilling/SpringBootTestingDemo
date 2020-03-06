package com.geraudwilling.demo.springboottesting.service.impl;

import com.geraudwilling.demo.springboottesting.client.EmployeeClient;
import com.geraudwilling.demo.springboottesting.client.EmployeeClientResponse;
import com.geraudwilling.demo.springboottesting.entity.Employee;
import com.geraudwilling.demo.springboottesting.exception.ConflictException;
import com.geraudwilling.demo.springboottesting.exception.InternalServerError;
import com.geraudwilling.demo.springboottesting.exception.NotFoundException;
import com.geraudwilling.demo.springboottesting.repository.EmployeeRepository;
import com.geraudwilling.demo.springboottesting.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final String HTTP_SUCCESS = "success";

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    EmployeeClient employeeClient;

    @Override
    public Employee save(Employee employee) {
        Optional<Employee> existing = employeeRepository.findById(employee.getId());
        if(existing.isPresent()){
            throw new ConflictException("Employee already exist :-( ");
        }
        return employeeRepository.save(employee);
    }

    @Override
    public List<Employee> findAllEmployees() {
        EmployeeClientResponse response = employeeClient.getEmployees();
        if(response == null || !HTTP_SUCCESS.equalsIgnoreCase(response.getStatus())){
            throw new InternalServerError("Fatal error when fetching employee list from distant Api.");
        }
        return response.getData();
    }

    @Override
    public Employee findEmployeeById(Long id) {
        return employeeRepository.findById(id).orElseThrow(() -> new NotFoundException());
    }

    @Override
    public Employee updateById(Long id) {
        Optional<Employee> existing = employeeRepository.findById(id);
        if(!existing.isPresent()){
            throw new NotFoundException();
        }
        return employeeRepository.save(existing.get());
    }

    @Override
    public void delete(Long id) {
        Optional<Employee> existing = employeeRepository.findById(id);
        if(!existing.isPresent()){
            throw new NotFoundException();
        }
        employeeRepository.delete(existing.get());
    }
}

package com.geraudwilling.demo.springboottesting.service;

import com.geraudwilling.demo.springboottesting.client.EmployeeClient;
import com.geraudwilling.demo.springboottesting.client.EmployeeClientResponse;
import com.geraudwilling.demo.springboottesting.entity.Employee;
import com.geraudwilling.demo.springboottesting.exception.ConflictException;
import com.geraudwilling.demo.springboottesting.exception.NotFoundException;
import com.geraudwilling.demo.springboottesting.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    EmployeeClient employeeClient;

    @Override
    public Employee save(Employee employee) {
        Optional<Employee> existing = employeeRepository.findById(employee.getId());
        if(existing.isPresent()){
            throw new ConflictException("Employee already exists");
        }
        return employeeRepository.save(employee);
    }

    @Override
    public EmployeeClientResponse findAllEmployees() {
        return employeeClient.getEmployees();
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

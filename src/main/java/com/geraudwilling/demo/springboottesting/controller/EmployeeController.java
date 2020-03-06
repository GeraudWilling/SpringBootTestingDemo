package com.geraudwilling.demo.springboottesting.controller;

import com.geraudwilling.demo.springboottesting.entity.Employee;
import com.geraudwilling.demo.springboottesting.exception.InvalidParameterException;
import com.geraudwilling.demo.springboottesting.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/api/employees", produces = MediaType.APPLICATION_JSON_VALUE)
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;

    @GetMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public Employee getEmployee(@PathVariable String id) {
        Long employeeId = null;
        try {
            employeeId = Long.parseLong(id);
        }catch(NumberFormatException ex){
            throw new InvalidParameterException(ex);
        }
        return employeeService.findEmployeeById(employeeId);
    }

    @GetMapping()
    @ResponseStatus(code = HttpStatus.OK)
    public List<Employee> getEmployees() {
        return employeeService.findAllEmployees();
    }
}

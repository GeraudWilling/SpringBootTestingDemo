package com.geraudwilling.demo.springboottesting.controller;

import com.geraudwilling.demo.springboottesting.client.EmployeeClientResponse;
import com.geraudwilling.demo.springboottesting.entity.Employee;
import com.geraudwilling.demo.springboottesting.exception.InternalServerError;
import com.geraudwilling.demo.springboottesting.exception.InvalidParameterException;
import com.geraudwilling.demo.springboottesting.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/api/employees", produces = MediaType.APPLICATION_JSON_VALUE)
public class EmployeeController {

    private final String HTTP_SUCCESS = "success";

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
        EmployeeClientResponse response = employeeService.findAllEmployees();
        if(response == null || !HTTP_SUCCESS.equalsIgnoreCase(response.getStatus())){
            throw new InternalServerError("Fatal error when fetching employee list from distant Api.");
        }
        return response.getData();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(code = HttpStatus.CREATED)
    public Employee createEmployee(@RequestBody @Valid  Employee employee, BindingResult result) {
        if (result.hasErrors()) {
            throw new InvalidParameterException("Employee JSON is not valid");
        }
        return employeeService.save(employee);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteEmployee(@PathVariable Long id) {
        employeeService.delete(id);
    }
}

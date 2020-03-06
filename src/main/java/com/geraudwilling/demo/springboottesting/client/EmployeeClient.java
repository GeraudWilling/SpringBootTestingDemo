package com.geraudwilling.demo.springboottesting.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "${feign.employee.name}", url = "${feign.employee.url}")
public interface EmployeeClient {

    @RequestMapping(method = RequestMethod.GET, value = "/api/v1/employees")
    EmployeeClientResponse getEmployees();
}


package com.geraudwilling.demo.springboottesting.client;

import feign.RequestLine;

public interface EmployeeClient {

    @RequestLine("GET /api/v1/employees")
    EmployeeClientResponse getEmployees();

}


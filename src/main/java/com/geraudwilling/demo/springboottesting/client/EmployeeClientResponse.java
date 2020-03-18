package com.geraudwilling.demo.springboottesting.client;

import com.geraudwilling.demo.springboottesting.entity.Employee;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeClientResponse {
    String status;
    List<Employee> data;
}

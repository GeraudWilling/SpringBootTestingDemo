package com.geraudwilling.demo.springboottesting.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    @Id
    Long id;

    String employeeName;
    int employeeSalary;
    int employeeAge;
}

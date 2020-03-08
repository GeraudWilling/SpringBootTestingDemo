package com.geraudwilling.demo.springboottesting.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    @Id
    Long id;

    @JsonProperty("employee_name")
    @NotEmpty()
    String employeeName;

    @JsonProperty("employee_salary")
    int employeeSalary;

    @JsonProperty("employee_age")
    int employeeAge;

    @JsonProperty("profile_image")
    String profileImage;
}

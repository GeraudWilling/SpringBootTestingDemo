package com.geraudwilling.demo.springboottesting.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Employee {
    @Id
    Long id;

    @JsonProperty("employee_name")
    @NotEmpty()
    @Column(name = "employee_name", nullable = false)
    String employeeName;


    @JsonProperty("employee_salary")
    @Column(name = "employee_salary")
    int employeeSalary;

    @JsonProperty("employee_age")
    @Column(name = "employee_age")
    int employeeAge;

    @JsonProperty("profile_image")
    @Column(name = "profile_image")
    String profileImage;
}

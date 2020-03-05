package com.geraudwilling.demo.springboottesting.repository;

import com.geraudwilling.demo.springboottesting.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}

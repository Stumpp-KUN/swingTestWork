package com.example.swing.swing.repository;

import com.example.swing.swing.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee,Long> {
    Employee findBySurname(String surname);
}

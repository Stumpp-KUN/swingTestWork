package com.example.swing.swing.service;

import com.example.swing.swing.entity.Employee;
import com.example.swing.swing.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public List<Employee> getEmployees(){
        return employeeRepository.findAll();
    }

    public Employee findBySurname(String surname){
        return employeeRepository.findBySurname(surname);

    }


}

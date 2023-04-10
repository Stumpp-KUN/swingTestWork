package com.example.swing.swing.service;

import com.example.swing.swing.entity.Employee;
import com.example.swing.swing.entity.Task;
import com.example.swing.swing.repository.TaskRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class TaskService {

    private final TaskRepository taskRepository;
    private final EmployeeService employeeService;

    public List<Task> getTasks(String surname){

        return taskRepository.findAllByEmployee(employeeService.findBySurname(surname));

    }
}

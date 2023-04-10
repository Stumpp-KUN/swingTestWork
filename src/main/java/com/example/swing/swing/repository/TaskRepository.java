package com.example.swing.swing.repository;

import com.example.swing.swing.entity.Employee;
import com.example.swing.swing.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task,Long> {
    List<Task> findAllByEmployee(Employee employee);
}

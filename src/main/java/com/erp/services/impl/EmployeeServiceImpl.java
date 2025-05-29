package com.erp.services.impl;

import com.erp.exceptions.AppException;
import com.erp.models.Employee;
import com.erp.models.User;
import com.erp.repositories.IEmployeeRepository;
import com.erp.services.IEmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements IEmployeeService {

    private final IEmployeeRepository employeeRepository;

    @Override
    public Employee createEmployee(Employee employee) {
        // Check if employee with the same code already exists
        if (employeeRepository.findByCode(employee.getCode()).isPresent()) {
            throw new AppException("Employee with code " + employee.getCode() + " already exists");
        }
        
        // Check if employee with the same user already exists
        if (employeeRepository.findByUser(employee.getUser()).isPresent()) {
            throw new AppException("Employee for this user already exists");
        }
        
        return employeeRepository.save(employee);
    }

    @Override
    public Employee updateEmployee(UUID id, Employee employee) {
        Employee existingEmployee = findEmployeeById(id);
        
        // If code is being changed, check if the new code is already in use
        if (!existingEmployee.getCode().equals(employee.getCode()) && 
            employeeRepository.findByCode(employee.getCode()).isPresent()) {
            throw new AppException("Employee with code " + employee.getCode() + " already exists");
        }
        
        // Update fields
        existingEmployee.setCode(employee.getCode());
        existingEmployee.setDateOfBirth(employee.getDateOfBirth());
        existingEmployee.setStatus(employee.getStatus());
        
        return employeeRepository.save(existingEmployee);
    }

    @Override
    public Employee findEmployeeById(UUID id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new AppException("Employee not found with id: " + id));
    }

    @Override
    public Employee findEmployeeByCode(String code) {
        return employeeRepository.findByCode(code)
                .orElseThrow(() -> new AppException("Employee not found with code: " + code));
    }

    @Override
    public Employee findEmployeeByUser(User user) {
        return employeeRepository.findByUser(user)
                .orElseThrow(() -> new AppException("Employee not found for user: " + user.getId()));
    }

    @Override
    public List<Employee> findAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public void deleteEmployee(UUID id) {
        Employee employee = findEmployeeById(id);
        employeeRepository.delete(employee);
    }
}
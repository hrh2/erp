package com.erp.services.impl;

import com.erp.enums.EEmploymentStatus;
import com.erp.exceptions.AppException;
import com.erp.models.Employee;
import com.erp.models.Employment;
import com.erp.repositories.IEmploymentRepository;
import com.erp.services.IEmploymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmploymentServiceImpl implements IEmploymentService {

    private final IEmploymentRepository employmentRepository;

    @Override
    public Employment createEmployment(Employment employment) {
        // Check if employment with the same code already exists
        if (employmentRepository.findByCode(employment.getCode()).isPresent()) {
            throw new AppException("Employment with code " + employment.getCode() + " already exists");
        }
        
        return employmentRepository.save(employment);
    }

    @Override
    public Employment updateEmployment(UUID id, Employment employment) {
        Employment existingEmployment = findEmploymentById(id);
        
        // If code is being changed, check if the new code is already in use
        if (!existingEmployment.getCode().equals(employment.getCode()) && 
            employmentRepository.findByCode(employment.getCode()).isPresent()) {
            throw new AppException("Employment with code " + employment.getCode() + " already exists");
        }
        
        // Update fields
        existingEmployment.setCode(employment.getCode());
        existingEmployment.setDepartment(employment.getDepartment());
        existingEmployment.setPosition(employment.getPosition());
        existingEmployment.setBaseSalary(employment.getBaseSalary());
        existingEmployment.setStatus(employment.getStatus());
        existingEmployment.setJoiningDate(employment.getJoiningDate());
        
        return employmentRepository.save(existingEmployment);
    }

    @Override
    public Employment findEmploymentById(UUID id) {
        return employmentRepository.findById(id)
                .orElseThrow(() -> new AppException("Employment not found with id: " + id));
    }

    @Override
    public Employment findEmploymentByCode(String code) {
        return employmentRepository.findByCode(code)
                .orElseThrow(() -> new AppException("Employment not found with code: " + code));
    }

    @Override
    public List<Employment> findEmploymentsByEmployee(Employee employee) {
        return employmentRepository.findByEmployee(employee);
    }

    @Override
    public List<Employment> findEmploymentsByEmployeeAndStatus(Employee employee, EEmploymentStatus status) {
        return employmentRepository.findByEmployeeAndStatus(employee, status);
    }

    @Override
    public Employment findActiveEmploymentByEmployee(Employee employee) {
        return employmentRepository.findByEmployeeAndStatusOrderByJoiningDateDesc(employee, EEmploymentStatus.ACTIVE)
                .orElseThrow(() -> new AppException("No active employment found for employee: " + employee.getId()));
    }

    @Override
    public List<Employment> findAllEmployments() {
        return employmentRepository.findAll();
    }

    @Override
    public void deleteEmployment(UUID id) {
        Employment employment = findEmploymentById(id);
        employmentRepository.delete(employment);
    }
}
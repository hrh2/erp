package com.erp.services;

import com.erp.models.Employee;
import com.erp.models.Employment;
import com.erp.enums.EEmploymentStatus;

import java.util.List;
import java.util.UUID;

public interface IEmploymentService {
    Employment createEmployment(Employment employment);
    Employment updateEmployment(UUID id, Employment employment);
    Employment findEmploymentById(UUID id);
    Employment findEmploymentByCode(String code);
    List<Employment> findEmploymentsByEmployee(Employee employee);
    List<Employment> findEmploymentsByEmployeeAndStatus(Employee employee, EEmploymentStatus status);
    Employment findActiveEmploymentByEmployee(Employee employee);
    List<Employment> findAllEmployments();
    void deleteEmployment(UUID id);
}
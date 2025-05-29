package com.erp.repositories;

import com.erp.models.Employee;
import com.erp.models.Employment;
import com.erp.enums.EEmploymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IEmploymentRepository extends JpaRepository<Employment, UUID> {
    Optional<Employment> findByCode(String code);
    List<Employment> findByEmployee(Employee employee);
    List<Employment> findByEmployeeAndStatus(Employee employee, EEmploymentStatus status);
    Optional<Employment> findByEmployeeAndStatusOrderByJoiningDateDesc(Employee employee, EEmploymentStatus status);
}
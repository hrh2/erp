package com.erp.repositories;

import com.erp.models.Employee;
import com.erp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IEmployeeRepository extends JpaRepository<Employee, UUID> {
    Optional<Employee> findByCode(String code);
    Optional<Employee> findByUser(User user);
}
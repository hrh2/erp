package com.erp.repositories;

import com.erp.models.Employee;
import com.erp.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface IMessageRepository extends JpaRepository<Message, UUID> {
    List<Message> findByEmployee(Employee employee);
    List<Message> findByEmployeeAndMonthYear(Employee employee, String monthYear);
}
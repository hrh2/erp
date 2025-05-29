package com.erp.repositories;

import com.erp.models.Employee;
import com.erp.models.Payslip;
import com.erp.enums.EPayslipStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IPayslipRepository extends JpaRepository<Payslip, UUID> {
    List<Payslip> findByEmployee(Employee employee);
    List<Payslip> findByEmployeeAndStatus(Employee employee, EPayslipStatus status);
    List<Payslip> findByStatus(EPayslipStatus status);
    List<Payslip> findByMonthAndYear(Integer month, Integer year);
    List<Payslip> findByMonthAndYearAndStatus(Integer month, Integer year, EPayslipStatus status);
    Optional<Payslip> findByEmployeeAndMonthAndYear(Employee employee, Integer month, Integer year);
    boolean existsByEmployeeAndMonthAndYear(Employee employee, Integer month, Integer year);
}
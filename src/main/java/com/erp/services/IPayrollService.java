package com.erp.services;

import com.erp.models.Employee;
import com.erp.models.Payslip;
import com.erp.enums.EPayslipStatus;

import java.util.List;
import java.util.UUID;

public interface IPayrollService {
    Payslip generatePayslip(Employee employee, Integer month, Integer year);
    List<Payslip> generatePayrollForMonth(Integer month, Integer year);
    Payslip approvePayslip(UUID payslipId);
    List<Payslip> approvePayrollForMonth(Integer month, Integer year);
    Payslip findPayslipById(UUID id);
    List<Payslip> findPayslipsByEmployee(Employee employee);
    List<Payslip> findPayslipsByEmployeeAndStatus(Employee employee, EPayslipStatus status);
    List<Payslip> findPayslipsByStatus(EPayslipStatus status);
    List<Payslip> findPayslipsByMonthAndYear(Integer month, Integer year);
    List<Payslip> findPayslipsByMonthAndYearAndStatus(Integer month, Integer year, EPayslipStatus status);
    Payslip findPayslipByEmployeeAndMonthAndYear(Employee employee, Integer month, Integer year);
    boolean existsPayslipByEmployeeAndMonthAndYear(Employee employee, Integer month, Integer year);
}
package com.erp.services.impl;

import com.erp.enums.EPayslipStatus;
import com.erp.exceptions.AppException;
import com.erp.models.Deduction;
import com.erp.models.Employee;
import com.erp.models.Employment;
import com.erp.models.Payslip;
import com.erp.repositories.IPayslipRepository;
import com.erp.services.IDeductionService;
import com.erp.services.IEmployeeService;
import com.erp.services.IEmploymentService;
import com.erp.services.IMessageService;
import com.erp.services.IPayrollService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PayrollServiceImpl implements IPayrollService {

    private final IPayslipRepository payslipRepository;
    private final IEmployeeService employeeService;
    private final IEmploymentService employmentService;
    private final IDeductionService deductionService;
    private final IMessageService messageService;

    @Override
    @Transactional
    public Payslip generatePayslip(Employee employee, Integer month, Integer year) {
        // Check if payslip already exists for this employee and month/year
        if (existsPayslipByEmployeeAndMonthAndYear(employee, month, year)) {
            throw new AppException("Payslip already exists for employee " + employee.getCode() + " for " + month + "/" + year);
        }
        
        // Get active employment for the employee
        Employment employment = employmentService.findActiveEmploymentByEmployee(employee);
        
        // Get all deductions
        List<Deduction> deductions = deductionService.findAllDeductions();
        
        // Calculate housing and transport allowances (14% of base salary each)
        BigDecimal housingPercentage = findDeductionPercentage(deductions, "Housing");
        BigDecimal transportPercentage = findDeductionPercentage(deductions, "Transport");
        
        BigDecimal housingAmount = calculatePercentage(employment.getBaseSalary(), housingPercentage);
        BigDecimal transportAmount = calculatePercentage(employment.getBaseSalary(), transportPercentage);
        
        // Calculate gross salary
        BigDecimal grossSalary = employment.getBaseSalary().add(housingAmount).add(transportAmount);
        
        // Calculate deductions
        BigDecimal employeeTaxPercentage = findDeductionPercentage(deductions, "Employee Tax");
        BigDecimal pensionPercentage = findDeductionPercentage(deductions, "Pension");
        BigDecimal medicalInsurancePercentage = findDeductionPercentage(deductions, "Medical Insurance");
        BigDecimal otherDeductionsPercentage = findDeductionPercentage(deductions, "Others");
        
        BigDecimal employeeTaxAmount = calculatePercentage(employment.getBaseSalary(), employeeTaxPercentage);
        BigDecimal pensionAmount = calculatePercentage(employment.getBaseSalary(), pensionPercentage);
        BigDecimal medicalInsuranceAmount = calculatePercentage(employment.getBaseSalary(), medicalInsurancePercentage);
        BigDecimal otherDeductions = calculatePercentage(employment.getBaseSalary(), otherDeductionsPercentage);
        
        // Calculate total deductions
        BigDecimal totalDeductions = employeeTaxAmount.add(pensionAmount).add(medicalInsuranceAmount).add(otherDeductions);
        
        // Ensure deductions don't exceed gross salary
        if (totalDeductions.compareTo(grossSalary) > 0) {
            throw new AppException("Total deductions exceed gross salary for employee " + employee.getCode());
        }
        
        // Calculate net salary
        BigDecimal netSalary = grossSalary.subtract(totalDeductions);
        
        // Create and save payslip
        Payslip payslip = Payslip.builder()
                .employee(employee)
                .housingAmount(housingAmount)
                .transportAmount(transportAmount)
                .employeeTaxAmount(employeeTaxAmount)
                .pensionAmount(pensionAmount)
                .medicalInsuranceAmount(medicalInsuranceAmount)
                .otherDeductions(otherDeductions)
                .grossSalary(grossSalary)
                .netSalary(netSalary)
                .month(month)
                .year(year)
                .status(EPayslipStatus.PENDING)
                .build();
        
        return payslipRepository.save(payslip);
    }

    @Override
    @Transactional
    public List<Payslip> generatePayrollForMonth(Integer month, Integer year) {
        List<Payslip> payslips = new ArrayList<>();
        
        // Get all active employees
        List<Employee> employees = employeeService.findAllEmployees();
        
        for (Employee employee : employees) {
            try {
                // Skip if payslip already exists for this employee and month/year
                if (existsPayslipByEmployeeAndMonthAndYear(employee, month, year)) {
                    continue;
                }
                
                // Skip if employee doesn't have an active employment
                try {
                    employmentService.findActiveEmploymentByEmployee(employee);
                } catch (AppException e) {
                    continue;
                }
                
                Payslip payslip = generatePayslip(employee, month, year);
                payslips.add(payslip);
            } catch (Exception e) {
                // Log error but continue with next employee
                System.err.println("Error generating payslip for employee " + employee.getCode() + ": " + e.getMessage());
            }
        }
        
        return payslips;
    }

    @Override
    @Transactional
    public Payslip approvePayslip(UUID payslipId) {
        Payslip payslip = findPayslipById(payslipId);
        
        if (payslip.getStatus() == EPayslipStatus.PAID) {
            throw new AppException("Payslip is already approved");
        }
        
        payslip.setStatus(EPayslipStatus.PAID);
        Payslip approvedPayslip = payslipRepository.save(payslip);
        
        // Create message for the employee
        messageService.createPayslipApprovalMessage(approvedPayslip);
        
        return approvedPayslip;
    }

    @Override
    @Transactional
    public List<Payslip> approvePayrollForMonth(Integer month, Integer year) {
        List<Payslip> payslips = findPayslipsByMonthAndYearAndStatus(month, year, EPayslipStatus.PENDING);
        List<Payslip> approvedPayslips = new ArrayList<>();
        
        for (Payslip payslip : payslips) {
            try {
                Payslip approvedPayslip = approvePayslip(payslip.getId());
                approvedPayslips.add(approvedPayslip);
            } catch (Exception e) {
                // Log error but continue with next payslip
                System.err.println("Error approving payslip " + payslip.getId() + ": " + e.getMessage());
            }
        }
        
        return approvedPayslips;
    }

    @Override
    public Payslip findPayslipById(UUID id) {
        return payslipRepository.findById(id)
                .orElseThrow(() -> new AppException("Payslip not found with id: " + id));
    }

    @Override
    public List<Payslip> findPayslipsByEmployee(Employee employee) {
        return payslipRepository.findByEmployee(employee);
    }

    @Override
    public List<Payslip> findPayslipsByEmployeeAndStatus(Employee employee, EPayslipStatus status) {
        return payslipRepository.findByEmployeeAndStatus(employee, status);
    }

    @Override
    public List<Payslip> findPayslipsByStatus(EPayslipStatus status) {
        return payslipRepository.findByStatus(status);
    }

    @Override
    public List<Payslip> findPayslipsByMonthAndYear(Integer month, Integer year) {
        return payslipRepository.findByMonthAndYear(month, year);
    }

    @Override
    public List<Payslip> findPayslipsByMonthAndYearAndStatus(Integer month, Integer year, EPayslipStatus status) {
        return payslipRepository.findByMonthAndYearAndStatus(month, year, status);
    }

    @Override
    public Payslip findPayslipByEmployeeAndMonthAndYear(Employee employee, Integer month, Integer year) {
        return payslipRepository.findByEmployeeAndMonthAndYear(employee, month, year)
                .orElseThrow(() -> new AppException("Payslip not found for employee " + employee.getCode() + " for " + month + "/" + year));
    }

    @Override
    public boolean existsPayslipByEmployeeAndMonthAndYear(Employee employee, Integer month, Integer year) {
        return payslipRepository.existsByEmployeeAndMonthAndYear(employee, month, year);
    }
    
    private BigDecimal findDeductionPercentage(List<Deduction> deductions, String deductionName) {
        return deductions.stream()
                .filter(d -> d.getName().equalsIgnoreCase(deductionName))
                .findFirst()
                .map(Deduction::getPercentage)
                .orElse(BigDecimal.ZERO);
    }
    
    private BigDecimal calculatePercentage(BigDecimal amount, BigDecimal percentage) {
        return amount.multiply(percentage.divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP))
                .setScale(2, RoundingMode.HALF_UP);
    }
}
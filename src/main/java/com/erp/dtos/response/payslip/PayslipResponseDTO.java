package com.erp.dtos.response.payslip;

import com.erp.dtos.response.employee.EmployeeResponseDTO;
import com.erp.enums.EPayslipStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayslipResponseDTO {
    
    private UUID id;
    
    private EmployeeResponseDTO employee;
    
    private BigDecimal housingAmount;
    
    private BigDecimal transportAmount;
    
    private BigDecimal employeeTaxAmount;
    
    private BigDecimal pensionAmount;
    
    private BigDecimal medicalInsuranceAmount;
    
    private BigDecimal otherDeductions;
    
    private BigDecimal grossSalary;
    
    private BigDecimal netSalary;
    
    private Integer month;
    
    private Integer year;
    
    private EPayslipStatus status;
    
    private LocalDateTime createdDate;
}
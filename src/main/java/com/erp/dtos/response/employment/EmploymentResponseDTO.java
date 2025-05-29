package com.erp.dtos.response.employment;

import com.erp.dtos.response.employee.EmployeeResponseDTO;
import com.erp.enums.EEmploymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmploymentResponseDTO {
    
    private UUID id;
    
    private String code;
    
    private EmployeeResponseDTO employee;
    
    private String department;
    
    private String position;
    
    private BigDecimal baseSalary;
    
    private EEmploymentStatus status;
    
    private LocalDate joiningDate;
    
    private LocalDate createdDate;
}
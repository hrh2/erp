package com.erp.dtos.request.employment;

import com.erp.enums.EEmploymentStatus;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEmploymentDTO {
    
    private String code;
    
    private String department;
    
    private String position;
    
    @Positive(message = "Base salary must be positive")
    private BigDecimal baseSalary;
    
    private EEmploymentStatus status;
    
    private LocalDate joiningDate;
}
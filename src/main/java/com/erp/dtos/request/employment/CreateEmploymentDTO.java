package com.erp.dtos.request.employment;

import com.erp.enums.EEmploymentStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
public class CreateEmploymentDTO {
    
    @NotBlank(message = "Code is required")
    private String code;
    
    @NotNull(message = "Employee ID is required")
    private UUID employeeId;
    
    @NotBlank(message = "Department is required")
    private String department;
    
    @NotBlank(message = "Position is required")
    private String position;
    
    @NotNull(message = "Base salary is required")
    @Positive(message = "Base salary must be positive")
    private BigDecimal baseSalary;
    
    @NotNull(message = "Status is required")
    private EEmploymentStatus status;
    
    @NotNull(message = "Joining date is required")
    private LocalDate joiningDate;
}
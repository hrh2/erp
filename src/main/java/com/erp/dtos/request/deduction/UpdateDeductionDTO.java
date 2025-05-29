package com.erp.dtos.request.deduction;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateDeductionDTO {
    
    private String code;
    
    private String name;
    
    @Positive(message = "Percentage must be positive")
    private BigDecimal percentage;
}
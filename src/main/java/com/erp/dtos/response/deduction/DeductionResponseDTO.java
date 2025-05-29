package com.erp.dtos.response.deduction;

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
public class DeductionResponseDTO {
    
    private UUID id;
    
    private String code;
    
    private String name;
    
    private BigDecimal percentage;
    
    private LocalDateTime createdDate;
}
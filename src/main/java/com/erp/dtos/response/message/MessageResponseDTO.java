package com.erp.dtos.response.message;

import com.erp.dtos.response.employee.EmployeeResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponseDTO {
    
    private UUID id;
    
    private EmployeeResponseDTO employee;
    
    private String message;
    
    private String monthYear;
    
    private LocalDateTime sentAt;
    
    private LocalDateTime createdDate;
}
package com.erp.dtos.response.employee;

import com.erp.dtos.request.user.UserResponseDTO;
import com.erp.enums.EEmployeeStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponseDTO {
    
    private UUID id;
    
    private String code;
    
    private UserResponseDTO user;
    
    private LocalDate dateOfBirth;
    
    private EEmployeeStatus status;
    
    private LocalDate createdDate;
}
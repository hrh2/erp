package com.erp.dtos.request.employee;

import com.erp.enums.EEmployeeStatus;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEmployeeDTO {
    
    private String code;
    
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;
    
    private EEmployeeStatus status;
}
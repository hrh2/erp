package com.erp.dtos.response.role;


import com.erp.models.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RolesResponseDTO {
    private Page<Role> roles;
}

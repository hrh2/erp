package com.erp.services;


import com.erp.dtos.request.role.CreateRoleDTO;
import com.erp.dtos.response.role.RoleResponseDTO;
import com.erp.dtos.response.role.RolesResponseDTO;
import com.erp.enums.ERole;
import com.erp.models.Role;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface IRoleService {
    public Role getRoleById(UUID roleId);

    public Role getRoleByName(ERole roleName);

    public void createRole(ERole roleName);

    public RoleResponseDTO createRole(CreateRoleDTO createRoleDTO);

    public RolesResponseDTO getRoles(Pageable pageable);

    public void deleteRole(UUID roleId);

    public boolean isRolePresent(ERole roleName);
}
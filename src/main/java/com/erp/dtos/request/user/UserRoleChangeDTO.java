package com.erp.dtos.request.user;

import com.erp.enums.ERole;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class UserRoleChangeDTO {
    private ERole newRole;
}
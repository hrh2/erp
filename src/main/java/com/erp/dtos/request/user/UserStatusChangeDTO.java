package com.erp.dtos.request.user;

import com.erp.enums.EAccountStatus;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class UserStatusChangeDTO {
    private EAccountStatus newStatus;
}
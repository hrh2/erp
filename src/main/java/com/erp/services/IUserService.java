package com.erp.services;



import com.erp.dtos.request.auth.RegisterUserDTO;
import com.erp.dtos.request.auth.UpdateUserDTO;
import com.erp.dtos.request.user.CreateAdminDTO;
import com.erp.dtos.request.user.UserResponseDTO;
import com.erp.dtos.request.user.UserRoleChangeDTO;
import com.erp.dtos.request.user.UserRoleModificationDTO;
import com.erp.dtos.request.user.UserStatusChangeDTO;
import com.erp.models.User;

import java.util.List;
import java.util.UUID;

public interface IUserService {

    User findUserById(UUID userId);

    User getLoggedInUser();

    UserResponseDTO createAdmin(CreateAdminDTO createUserDTO);

    UserResponseDTO createEmployee(RegisterUserDTO registerUserDTO);

    List<User> getUsers();

    UserResponseDTO getUserById(UUID uuid);

    UserResponseDTO updateUser(UUID userId, UpdateUserDTO updateUserDTO);

    UserResponseDTO addRoles(UUID userId, UserRoleModificationDTO userRoleModificationDTO);

    UserResponseDTO removeRoles(UUID userId, UserRoleModificationDTO userRoleModificationDTO);

    UserResponseDTO changeRole(UUID userId, UserRoleChangeDTO userRoleChangeDTO);

    UserResponseDTO changeAccountStatus(UUID userId, UserStatusChangeDTO userStatusChangeDTO);

    void deleteUser(UUID userId);
}

package com.erp.security;

import com.erp.models.Employee;
import com.erp.models.User;
import com.erp.services.IEmployeeService;
import com.erp.services.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component("userSecurity")
@RequiredArgsConstructor
public class UserSecurity {

    private final IUserService userService;
    private final IEmployeeService employeeService;

    /**
     * Check if the current user is the same as the user with the given ID
     * @param userId the ID of the user to check
     * @return true if the current user is the same as the user with the given ID, false otherwise
     */
    public boolean isCurrentUser(UUID userId) {
        User currentUser = userService.getLoggedInUser();
        return currentUser != null && currentUser.getId().equals(userId);
    }

    /**
     * Check if the current user is the employee with the given ID
     * @param employeeId the ID of the employee to check
     * @return true if the current user is the employee with the given ID, false otherwise
     */
    public boolean isCurrentEmployee(UUID employeeId) {
        User currentUser = userService.getLoggedInUser();
        if (currentUser == null) {
            return false;
        }
        
        try {
            Employee employee = employeeService.findEmployeeById(employeeId);
            return employee.getUser().getId().equals(currentUser.getId());
        } catch (Exception e) {
            return false;
        }
    }
}
package com.erp.controllers;

import com.erp.dtos.request.employee.CreateEmployeeDTO;
import com.erp.dtos.request.employee.UpdateEmployeeDTO;
import com.erp.dtos.response.employee.EmployeeResponseDTO;
import com.erp.models.Employee;
import com.erp.models.User;
import com.erp.services.IEmployeeService;
import com.erp.services.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/employees")
@RequiredArgsConstructor
@Tag(name = "Employee Management", description = "APIs for managing employees")
@SecurityRequirement(name = "bearerAuth")
public class EmployeeController {

    private final IEmployeeService employeeService;
    private final IUserService userService;
    private final ModelMapper modelMapper;

    @PostMapping
    @Operation(summary = "Create a new employee")
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    public ResponseEntity<EmployeeResponseDTO> createEmployee(@Valid @RequestBody CreateEmployeeDTO createEmployeeDTO) {
        User user = userService.findUserById(createEmployeeDTO.getUserId());

        Employee employee = Employee.builder()
                .code(createEmployeeDTO.getCode())
                .user(user)
                .dateOfBirth(createEmployeeDTO.getDateOfBirth())
                .status(createEmployeeDTO.getStatus())
                .build();

        Employee savedEmployee = employeeService.createEmployee(employee);
        return new ResponseEntity<>(convertToResponseDTO(savedEmployee), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all employees")
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    public ResponseEntity<List<EmployeeResponseDTO>> getAllEmployees() {
        List<Employee> employees = employeeService.findAllEmployees();
        List<EmployeeResponseDTO> employeeResponseDTOs = employees.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(employeeResponseDTOs);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get employee by ID")
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN') or @userSecurity.isCurrentEmployee(#id)")
    public ResponseEntity<EmployeeResponseDTO> getEmployeeById(@PathVariable UUID id) {
        Employee employee = employeeService.findEmployeeById(id);
        return ResponseEntity.ok(convertToResponseDTO(employee));
    }

    @GetMapping("/code/{code}")
    @Operation(summary = "Get employee by code")
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    public ResponseEntity<EmployeeResponseDTO> getEmployeeByCode(@PathVariable String code) {
        Employee employee = employeeService.findEmployeeByCode(code);
        return ResponseEntity.ok(convertToResponseDTO(employee));
    }

    @GetMapping("/current")
    @Operation(summary = "Get current employee")
    @PreAuthorize("hasAnyAuthority('ROLE_EMPLOYEE', 'ROLE_MANAGER', 'ROLE_ADMIN')")
    public ResponseEntity<EmployeeResponseDTO> getCurrentEmployee() {
        User currentUser = userService.getLoggedInUser();
        Employee employee = employeeService.findEmployeeByUser(currentUser);
        return ResponseEntity.ok(convertToResponseDTO(employee));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update employee")
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    public ResponseEntity<EmployeeResponseDTO> updateEmployee(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateEmployeeDTO updateEmployeeDTO) {

        Employee existingEmployee = employeeService.findEmployeeById(id);

        if (updateEmployeeDTO.getCode() != null) {
            existingEmployee.setCode(updateEmployeeDTO.getCode());
        }

        if (updateEmployeeDTO.getDateOfBirth() != null) {
            existingEmployee.setDateOfBirth(updateEmployeeDTO.getDateOfBirth());
        }

        if (updateEmployeeDTO.getStatus() != null) {
            existingEmployee.setStatus(updateEmployeeDTO.getStatus());
        }

        Employee updatedEmployee = employeeService.updateEmployee(id, existingEmployee);
        return ResponseEntity.ok(convertToResponseDTO(updatedEmployee));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete employee")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteEmployee(@PathVariable UUID id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }

    private EmployeeResponseDTO convertToResponseDTO(Employee employee) {
        EmployeeResponseDTO responseDTO = modelMapper.map(employee, EmployeeResponseDTO.class);
        return responseDTO;
    }
}

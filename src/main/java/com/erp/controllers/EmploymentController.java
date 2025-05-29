package com.erp.controllers;

import com.erp.dtos.request.employment.CreateEmploymentDTO;
import com.erp.dtos.request.employment.UpdateEmploymentDTO;
import com.erp.dtos.response.employment.EmploymentResponseDTO;
import com.erp.enums.EEmploymentStatus;
import com.erp.models.Employee;
import com.erp.models.Employment;
import com.erp.services.IEmployeeService;
import com.erp.services.IEmploymentService;
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
@RequestMapping("/api/v1/employments")
@RequiredArgsConstructor
@Tag(name = "Employment Management", description = "APIs for managing employments")
@SecurityRequirement(name = "bearerAuth")
public class EmploymentController {

    private final IEmploymentService employmentService;
    private final IEmployeeService employeeService;
    private final ModelMapper modelMapper;

    @PostMapping
    @Operation(summary = "Create a new employment")
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    public ResponseEntity<EmploymentResponseDTO> createEmployment(@Valid @RequestBody CreateEmploymentDTO createEmploymentDTO) {
        Employee employee = employeeService.findEmployeeById(createEmploymentDTO.getEmployeeId());

        Employment employment = Employment.builder()
                .code(createEmploymentDTO.getCode())
                .employee(employee)
                .department(createEmploymentDTO.getDepartment())
                .position(createEmploymentDTO.getPosition())
                .baseSalary(createEmploymentDTO.getBaseSalary())
                .status(createEmploymentDTO.getStatus())
                .joiningDate(createEmploymentDTO.getJoiningDate())
                .build();

        Employment savedEmployment = employmentService.createEmployment(employment);
        return new ResponseEntity<>(convertToResponseDTO(savedEmployment), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all employments")
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    public ResponseEntity<List<EmploymentResponseDTO>> getAllEmployments() {
        List<Employment> employments = employmentService.findAllEmployments();
        List<EmploymentResponseDTO> employmentResponseDTOs = employments.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(employmentResponseDTOs);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get employment by ID")
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    public ResponseEntity<EmploymentResponseDTO> getEmploymentById(@PathVariable UUID id) {
        Employment employment = employmentService.findEmploymentById(id);
        return ResponseEntity.ok(convertToResponseDTO(employment));
    }

    @GetMapping("/code/{code}")
    @Operation(summary = "Get employment by code")
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    public ResponseEntity<EmploymentResponseDTO> getEmploymentByCode(@PathVariable String code) {
        Employment employment = employmentService.findEmploymentByCode(code);
        return ResponseEntity.ok(convertToResponseDTO(employment));
    }

    @GetMapping("/employee/{employeeId}")
    @Operation(summary = "Get employments by employee ID")
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN') or @userSecurity.isCurrentEmployee(#employeeId)")
    public ResponseEntity<List<EmploymentResponseDTO>> getEmploymentsByEmployee(@PathVariable UUID employeeId) {
        Employee employee = employeeService.findEmployeeById(employeeId);
        List<Employment> employments = employmentService.findEmploymentsByEmployee(employee);
        List<EmploymentResponseDTO> employmentResponseDTOs = employments.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(employmentResponseDTOs);
    }

    @GetMapping("/employee/{employeeId}/active")
    @Operation(summary = "Get active employment by employee ID")
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN') or @userSecurity.isCurrentEmployee(#employeeId)")
    public ResponseEntity<EmploymentResponseDTO> getActiveEmploymentByEmployee(@PathVariable UUID employeeId) {
        Employee employee = employeeService.findEmployeeById(employeeId);
        Employment employment = employmentService.findActiveEmploymentByEmployee(employee);
        return ResponseEntity.ok(convertToResponseDTO(employment));
    }

    @GetMapping("/employee/{employeeId}/status/{status}")
    @Operation(summary = "Get employments by employee ID and status")
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN') or @userSecurity.isCurrentEmployee(#employeeId)")
    public ResponseEntity<List<EmploymentResponseDTO>> getEmploymentsByEmployeeAndStatus(
            @PathVariable UUID employeeId,
            @PathVariable EEmploymentStatus status) {
        Employee employee = employeeService.findEmployeeById(employeeId);
        List<Employment> employments = employmentService.findEmploymentsByEmployeeAndStatus(employee, status);
        List<EmploymentResponseDTO> employmentResponseDTOs = employments.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(employmentResponseDTOs);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update employment")
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    public ResponseEntity<EmploymentResponseDTO> updateEmployment(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateEmploymentDTO updateEmploymentDTO) {

        Employment existingEmployment = employmentService.findEmploymentById(id);

        if (updateEmploymentDTO.getCode() != null) {
            existingEmployment.setCode(updateEmploymentDTO.getCode());
        }

        if (updateEmploymentDTO.getDepartment() != null) {
            existingEmployment.setDepartment(updateEmploymentDTO.getDepartment());
        }

        if (updateEmploymentDTO.getPosition() != null) {
            existingEmployment.setPosition(updateEmploymentDTO.getPosition());
        }

        if (updateEmploymentDTO.getBaseSalary() != null) {
            existingEmployment.setBaseSalary(updateEmploymentDTO.getBaseSalary());
        }

        if (updateEmploymentDTO.getStatus() != null) {
            existingEmployment.setStatus(updateEmploymentDTO.getStatus());
        }

        if (updateEmploymentDTO.getJoiningDate() != null) {
            existingEmployment.setJoiningDate(updateEmploymentDTO.getJoiningDate());
        }

        Employment updatedEmployment = employmentService.updateEmployment(id, existingEmployment);
        return ResponseEntity.ok(convertToResponseDTO(updatedEmployment));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete employment")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteEmployment(@PathVariable UUID id) {
        employmentService.deleteEmployment(id);
        return ResponseEntity.noContent().build();
    }

    private EmploymentResponseDTO convertToResponseDTO(Employment employment) {
        EmploymentResponseDTO responseDTO = modelMapper.map(employment, EmploymentResponseDTO.class);
        return responseDTO;
    }
}

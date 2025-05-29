package com.erp.controllers;

import com.erp.dtos.response.payslip.PayslipResponseDTO;
import com.erp.enums.EPayslipStatus;
import com.erp.models.Employee;
import com.erp.models.Payslip;
import com.erp.services.IEmployeeService;
import com.erp.services.IPayrollService;
import com.erp.services.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("/payroll")
@RequiredArgsConstructor
@Tag(name = "Payroll Management", description = "APIs for managing payroll")
@SecurityRequirement(name = "bearerAuth")
public class PayrollController {

    private final IPayrollService payrollService;
    private final IEmployeeService employeeService;
    private final IUserService userService;
    private final ModelMapper modelMapper;

    @PostMapping("/generate/{employeeId}/{month}/{year}")
    @Operation(summary = "Generate payslip for an employee")
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    public ResponseEntity<PayslipResponseDTO> generatePayslip(
            @PathVariable UUID employeeId,
            @PathVariable Integer month,
            @PathVariable Integer year) {

        Employee employee = employeeService.findEmployeeById(employeeId);
        Payslip payslip = payrollService.generatePayslip(employee, month, year);
        return new ResponseEntity<>(convertToResponseDTO(payslip), HttpStatus.CREATED);
    }

    @PostMapping("/generate/month/{month}/{year}")
    @Operation(summary = "Generate payroll for a month")
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    public ResponseEntity<List<PayslipResponseDTO>> generatePayrollForMonth(
            @PathVariable Integer month,
            @PathVariable Integer year) {

        List<Payslip> payslips = payrollService.generatePayrollForMonth(month, year);
        List<PayslipResponseDTO> payslipResponseDTOs = payslips.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(payslipResponseDTOs);
    }

    @PutMapping("/approve/{payslipId}")
    @Operation(summary = "Approve a payslip")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<PayslipResponseDTO> approvePayslip(@PathVariable UUID payslipId) {
        Payslip payslip = payrollService.approvePayslip(payslipId);
        return ResponseEntity.ok(convertToResponseDTO(payslip));
    }

    @PutMapping("/approve/month/{month}/{year}")
    @Operation(summary = "Approve payroll for a month")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<PayslipResponseDTO>> approvePayrollForMonth(
            @PathVariable Integer month,
            @PathVariable Integer year) {

        List<Payslip> payslips = payrollService.approvePayrollForMonth(month, year);
        List<PayslipResponseDTO> payslipResponseDTOs = payslips.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(payslipResponseDTOs);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get payslip by ID")
    public ResponseEntity<PayslipResponseDTO> getPayslipById(@PathVariable UUID id) {
        Payslip payslip = payrollService.findPayslipById(id);
        return ResponseEntity.ok(convertToResponseDTO(payslip));
    }

    @GetMapping("/employee/{employeeId}")
    @Operation(summary = "Get payslips by employee ID")
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN') or @userSecurity.isCurrentEmployee(#employeeId)")
    public ResponseEntity<List<PayslipResponseDTO>> getPayslipsByEmployee(@PathVariable UUID employeeId) {
        Employee employee = employeeService.findEmployeeById(employeeId);
        List<Payslip> payslips = payrollService.findPayslipsByEmployee(employee);
        List<PayslipResponseDTO> payslipResponseDTOs = payslips.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(payslipResponseDTOs);
    }

    @GetMapping("/employee/{employeeId}/status/{status}")
    @Operation(summary = "Get payslips by employee ID and status")
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN') or @userSecurity.isCurrentEmployee(#employeeId)")
    public ResponseEntity<List<PayslipResponseDTO>> getPayslipsByEmployeeAndStatus(
            @PathVariable UUID employeeId,
            @PathVariable EPayslipStatus status) {

        Employee employee = employeeService.findEmployeeById(employeeId);
        List<Payslip> payslips = payrollService.findPayslipsByEmployeeAndStatus(employee, status);
        List<PayslipResponseDTO> payslipResponseDTOs = payslips.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(payslipResponseDTOs);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get payslips by status")
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    public ResponseEntity<List<PayslipResponseDTO>> getPayslipsByStatus(@PathVariable EPayslipStatus status) {
        List<Payslip> payslips = payrollService.findPayslipsByStatus(status);
        List<PayslipResponseDTO> payslipResponseDTOs = payslips.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(payslipResponseDTOs);
    }

    @GetMapping("/month/{month}/{year}")
    @Operation(summary = "Get payslips by month and year")
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    public ResponseEntity<List<PayslipResponseDTO>> getPayslipsByMonthAndYear(
            @PathVariable Integer month,
            @PathVariable Integer year) {

        List<Payslip> payslips = payrollService.findPayslipsByMonthAndYear(month, year);
        List<PayslipResponseDTO> payslipResponseDTOs = payslips.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(payslipResponseDTOs);
    }

    @GetMapping("/month/{month}/{year}/status/{status}")
    @Operation(summary = "Get payslips by month, year, and status")
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    public ResponseEntity<List<PayslipResponseDTO>> getPayslipsByMonthAndYearAndStatus(
            @PathVariable Integer month,
            @PathVariable Integer year,
            @PathVariable EPayslipStatus status) {

        List<Payslip> payslips = payrollService.findPayslipsByMonthAndYearAndStatus(month, year, status);
        List<PayslipResponseDTO> payslipResponseDTOs = payslips.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(payslipResponseDTOs);
    }

    @GetMapping("/employee/{employeeId}/month/{month}/{year}")
    @Operation(summary = "Get payslip by employee ID, month, and year")
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN') or @userSecurity.isCurrentEmployee(#employeeId)")
    public ResponseEntity<PayslipResponseDTO> getPayslipByEmployeeAndMonthAndYear(
            @PathVariable UUID employeeId,
            @PathVariable Integer month,
            @PathVariable Integer year) {

        Employee employee = employeeService.findEmployeeById(employeeId);
        Payslip payslip = payrollService.findPayslipByEmployeeAndMonthAndYear(employee, month, year);
        return ResponseEntity.ok(convertToResponseDTO(payslip));
    }

    @GetMapping("/current")
    @Operation(summary = "Get payslips for current employee")
    @PreAuthorize("hasAnyAuthority('ROLE_EMPLOYEE', 'ROLE_MANAGER', 'ROLE_ADMIN')")
    public ResponseEntity<List<PayslipResponseDTO>> getPayslipsForCurrentEmployee() {
        Employee employee = employeeService.findEmployeeByUser(userService.getLoggedInUser());
        List<Payslip> payslips = payrollService.findPayslipsByEmployee(employee);
        List<PayslipResponseDTO> payslipResponseDTOs = payslips.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(payslipResponseDTOs);
    }

    private PayslipResponseDTO convertToResponseDTO(Payslip payslip) {
        PayslipResponseDTO responseDTO = modelMapper.map(payslip, PayslipResponseDTO.class);
        return responseDTO;
    }
}

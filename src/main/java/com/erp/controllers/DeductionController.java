package com.erp.controllers;

import com.erp.dtos.request.deduction.CreateDeductionDTO;
import com.erp.dtos.request.deduction.UpdateDeductionDTO;
import com.erp.dtos.response.deduction.DeductionResponseDTO;
import com.erp.models.Deduction;
import com.erp.services.IDeductionService;
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
@RequestMapping("/api/v1/deductions")
@RequiredArgsConstructor
@Tag(name = "Deduction Management", description = "APIs for managing deductions")
@SecurityRequirement(name = "bearerAuth")
public class DeductionController {

    private final IDeductionService deductionService;
    private final ModelMapper modelMapper;

    @PostMapping
    @Operation(summary = "Create a new deduction")
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    public ResponseEntity<DeductionResponseDTO> createDeduction(@Valid @RequestBody CreateDeductionDTO createDeductionDTO) {
        Deduction deduction = Deduction.builder()
                .code(createDeductionDTO.getCode())
                .name(createDeductionDTO.getName())
                .percentage(createDeductionDTO.getPercentage())
                .build();
        
        Deduction savedDeduction = deductionService.createDeduction(deduction);
        return new ResponseEntity<>(convertToResponseDTO(savedDeduction), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all deductions")
    @PreAuthorize("hasAnyAuthority('ROLE_EMPLOYEE', 'ROLE_MANAGER', 'ROLE_ADMIN')")
    public ResponseEntity<List<DeductionResponseDTO>> getAllDeductions() {
        List<Deduction> deductions = deductionService.findAllDeductions();
        List<DeductionResponseDTO> deductionResponseDTOs = deductions.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(deductionResponseDTOs);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get deduction by ID")
    @PreAuthorize("hasAnyAuthority('ROLE_EMPLOYEE', 'ROLE_MANAGER', 'ROLE_ADMIN')")
    public ResponseEntity<DeductionResponseDTO> getDeductionById(@PathVariable UUID id) {
        Deduction deduction = deductionService.findDeductionById(id);
        return ResponseEntity.ok(convertToResponseDTO(deduction));
    }

    @GetMapping("/code/{code}")
    @Operation(summary = "Get deduction by code")
    @PreAuthorize("hasAnyAuthority('ROLE_EMPLOYEE', 'ROLE_MANAGER', 'ROLE_ADMIN')")
    public ResponseEntity<DeductionResponseDTO> getDeductionByCode(@PathVariable String code) {
        Deduction deduction = deductionService.findDeductionByCode(code);
        return ResponseEntity.ok(convertToResponseDTO(deduction));
    }

    @GetMapping("/name/{name}")
    @Operation(summary = "Get deduction by name")
    @PreAuthorize("hasAnyAuthority('ROLE_EMPLOYEE', 'ROLE_MANAGER', 'ROLE_ADMIN')")
    public ResponseEntity<DeductionResponseDTO> getDeductionByName(@PathVariable String name) {
        Deduction deduction = deductionService.findDeductionByName(name);
        return ResponseEntity.ok(convertToResponseDTO(deduction));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update deduction")
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    public ResponseEntity<DeductionResponseDTO> updateDeduction(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateDeductionDTO updateDeductionDTO) {
        
        Deduction existingDeduction = deductionService.findDeductionById(id);
        
        if (updateDeductionDTO.getCode() != null) {
            existingDeduction.setCode(updateDeductionDTO.getCode());
        }
        
        if (updateDeductionDTO.getName() != null) {
            existingDeduction.setName(updateDeductionDTO.getName());
        }
        
        if (updateDeductionDTO.getPercentage() != null) {
            existingDeduction.setPercentage(updateDeductionDTO.getPercentage());
        }
        
        Deduction updatedDeduction = deductionService.updateDeduction(id, existingDeduction);
        return ResponseEntity.ok(convertToResponseDTO(updatedDeduction));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete deduction")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteDeduction(@PathVariable UUID id) {
        deductionService.deleteDeduction(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/initialize")
    @Operation(summary = "Initialize default deductions")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> initializeDefaultDeductions() {
        deductionService.initializeDefaultDeductions();
        return ResponseEntity.ok().build();
    }

    private DeductionResponseDTO convertToResponseDTO(Deduction deduction) {
        DeductionResponseDTO responseDTO = modelMapper.map(deduction, DeductionResponseDTO.class);
        return responseDTO;
    }
}
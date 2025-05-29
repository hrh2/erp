package com.erp.services.impl;

import com.erp.exceptions.AppException;
import com.erp.models.Deduction;
import com.erp.repositories.IDeductionRepository;
import com.erp.services.IDeductionService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeductionServiceImpl implements IDeductionService {

    private final IDeductionRepository deductionRepository;

    @Override
    public Deduction createDeduction(Deduction deduction) {
        // Check if deduction with the same code already exists
        if (deductionRepository.findByCode(deduction.getCode()).isPresent()) {
            throw new AppException("Deduction with code " + deduction.getCode() + " already exists");
        }
        
        // Check if deduction with the same name already exists
        if (deductionRepository.findByName(deduction.getName()).isPresent()) {
            throw new AppException("Deduction with name " + deduction.getName() + " already exists");
        }
        
        return deductionRepository.save(deduction);
    }

    @Override
    public Deduction updateDeduction(UUID id, Deduction deduction) {
        Deduction existingDeduction = findDeductionById(id);
        
        // If code is being changed, check if the new code is already in use
        if (!existingDeduction.getCode().equals(deduction.getCode()) && 
            deductionRepository.findByCode(deduction.getCode()).isPresent()) {
            throw new AppException("Deduction with code " + deduction.getCode() + " already exists");
        }
        
        // If name is being changed, check if the new name is already in use
        if (!existingDeduction.getName().equals(deduction.getName()) && 
            deductionRepository.findByName(deduction.getName()).isPresent()) {
            throw new AppException("Deduction with name " + deduction.getName() + " already exists");
        }
        
        // Update fields
        existingDeduction.setCode(deduction.getCode());
        existingDeduction.setName(deduction.getName());
        existingDeduction.setPercentage(deduction.getPercentage());
        
        return deductionRepository.save(existingDeduction);
    }

    @Override
    public Deduction findDeductionById(UUID id) {
        return deductionRepository.findById(id)
                .orElseThrow(() -> new AppException("Deduction not found with id: " + id));
    }

    @Override
    public Deduction findDeductionByCode(String code) {
        return deductionRepository.findByCode(code)
                .orElseThrow(() -> new AppException("Deduction not found with code: " + code));
    }

    @Override
    public Deduction findDeductionByName(String name) {
        return deductionRepository.findByName(name)
                .orElseThrow(() -> new AppException("Deduction not found with name: " + name));
    }

    @Override
    public List<Deduction> findAllDeductions() {
        return deductionRepository.findAll();
    }

    @Override
    public void deleteDeduction(UUID id) {
        Deduction deduction = findDeductionById(id);
        deductionRepository.delete(deduction);
    }

    @Override
    @PostConstruct
    public void initializeDefaultDeductions() {
        // Only initialize if no deductions exist
        if (deductionRepository.count() == 0) {
            // Create default deductions
            createDefaultDeduction("DED001", "Employee Tax", new BigDecimal("30.0"));
            createDefaultDeduction("DED002", "Pension", new BigDecimal("6.0"));
            createDefaultDeduction("DED003", "Medical Insurance", new BigDecimal("5.0"));
            createDefaultDeduction("DED004", "Housing", new BigDecimal("14.0"));
            createDefaultDeduction("DED005", "Transport", new BigDecimal("14.0"));
            createDefaultDeduction("DED006", "Others", new BigDecimal("5.0"));
        }
    }
    
    private void createDefaultDeduction(String code, String name, BigDecimal percentage) {
        try {
            Deduction deduction = Deduction.builder()
                    .code(code)
                    .name(name)
                    .percentage(percentage)
                    .build();
            deductionRepository.save(deduction);
        } catch (Exception e) {
            // Log the error but don't fail initialization
            System.err.println("Error creating default deduction " + name + ": " + e.getMessage());
        }
    }
}
package com.erp.services;

import com.erp.models.Deduction;

import java.util.List;
import java.util.UUID;

public interface IDeductionService {
    Deduction createDeduction(Deduction deduction);
    Deduction updateDeduction(UUID id, Deduction deduction);
    Deduction findDeductionById(UUID id);
    Deduction findDeductionByCode(String code);
    Deduction findDeductionByName(String name);
    List<Deduction> findAllDeductions();
    void deleteDeduction(UUID id);
    void initializeDefaultDeductions();
}
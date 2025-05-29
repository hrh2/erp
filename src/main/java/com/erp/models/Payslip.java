package com.erp.models;

import com.erp.common.AbstractEntity;
import com.erp.enums.EPayslipStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "payslips")
@SuperBuilder
public class Payslip extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    private BigDecimal housingAmount;

    private BigDecimal transportAmount;

    private BigDecimal employeeTaxAmount;

    private BigDecimal pensionAmount;

    private BigDecimal medicalInsuranceAmount;

    private BigDecimal otherDeductions;

    private BigDecimal grossSalary;

    private BigDecimal netSalary;

    private Integer month;

    private Integer year;

    @Enumerated(EnumType.STRING)
    private EPayslipStatus status;
}
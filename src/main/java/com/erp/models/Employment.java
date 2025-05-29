package com.erp.models;

import com.erp.common.AbstractEntity;
import com.erp.enums.EEmploymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "employments")
@SuperBuilder
public class Employment extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(unique = true)
    private String code;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    private String department;

    private String position;

    private BigDecimal baseSalary;

    @Enumerated(EnumType.STRING)
    private EEmploymentStatus status;

    private LocalDate joiningDate;
}
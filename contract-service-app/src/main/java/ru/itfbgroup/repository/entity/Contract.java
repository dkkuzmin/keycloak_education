package ru.itfbgroup.repository.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Entity
@Table(name = "contracts")
public class Contract {
    @Id
    private UUID id;

    private String contractNumber;

    private LocalDate contractDate;

    private String currency;

    private String status;

    @OneToOne
    private Insurer insurer;

    private BigDecimal amount;

    private String description;
}

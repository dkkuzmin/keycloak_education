package ru.itfbgroup.model;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.itfbgroup.repository.entity.Insurer;

import javax.persistence.OneToOne;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO объекта "Договор". Набор полей минимален, исключительно для примера.
 */
@Data
@RequiredArgsConstructor
public class ContractDto {
    private UUID id;

    private String contractNumber;

    private LocalDate contractDate;

    private String currency;

    private String status;

    private Insurer insurer;

    private BigDecimal amount;
}

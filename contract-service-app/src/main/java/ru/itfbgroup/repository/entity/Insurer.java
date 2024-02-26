package ru.itfbgroup.repository.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Entity
@Table(name = "insurers")
public class Insurer {
    @Id
    private UUID id;

    private String lastname;

    private String firstname;

    private String patronymic;

    private String phone;

    private String birthplace;

    private LocalDate birthdate;

    private String gender;
}

package ru.itfbgroup.service;

import ru.itfbgroup.model.ContractDto;

import java.util.UUID;

public interface ContractService {
    ContractDto getById(UUID uuid);
}

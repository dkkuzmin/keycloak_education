package ru.itfbgroup.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import ru.itfbgroup.model.ContractDto;
import ru.itfbgroup.repository.ContractRepository;
import ru.itfbgroup.repository.entity.Contract;
import ru.itfbgroup.service.ContractService;
import ru.itfbgroup.service.mapper.ContractMapper;

import java.util.UUID;

/**
 * Сервис для работы с договорами
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ContractServiceImpl implements ContractService {

    private final ContractRepository repository;
    private final ContractMapper mapper;

    @Override
    public ContractDto getById(UUID uuid) {
        var contract = repository.findById(uuid).orElse(new Contract());
        return mapper.map(contract);
    }
}

package ru.itfbgroup.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.itfbgroup.repository.entity.Contract;

import java.util.UUID;

@Repository
public interface ContractRepository extends CrudRepository<Contract, UUID> {
}

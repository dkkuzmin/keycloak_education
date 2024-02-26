package ru.itfbgroup.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.itfbgroup.repository.entity.Insurer;

import java.util.UUID;

@Repository
public interface InsurerRepository extends CrudRepository<Insurer, UUID> {
}

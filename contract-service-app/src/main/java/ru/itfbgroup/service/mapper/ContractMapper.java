package ru.itfbgroup.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.itfbgroup.model.ContractDto;
import ru.itfbgroup.repository.entity.Contract;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_DEFAULT)
public interface ContractMapper {

    ContractDto map (Contract contract);
}

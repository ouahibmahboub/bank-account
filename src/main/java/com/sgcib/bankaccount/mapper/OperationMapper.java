package com.sgcib.bankaccount.mapper;

import com.sgcib.bankaccount.dto.OperationDTO;
import com.sgcib.bankaccount.model.Operation;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface OperationMapper {
    OperationDTO toOperationDTO(Operation operation);

}

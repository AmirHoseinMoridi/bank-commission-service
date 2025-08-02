package com.example.bank_commission_service.mapper;

import com.example.bank_commission_service.dto.TransactionParam;
import com.example.bank_commission_service.model.transaction.Transaction;
import org.mapstruct.factory.Mappers;

@org.mapstruct.Mapper(componentModel = "spring")
public interface Mapper {
    Mapper MAPPER = Mappers.getMapper(Mapper.class);

    Transaction paramToTransaction(TransactionParam param);
}

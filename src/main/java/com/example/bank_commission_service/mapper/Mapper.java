package com.example.bank_commission_service.mapper;

import com.example.bank_commission_service.dto.TransactionParam;
import com.example.bank_commission_service.model.transaction.Transaction;

@org.mapstruct.Mapper(componentModel = "spring")
public interface Mapper {
    Transaction paramToTransaction(TransactionParam param);
}

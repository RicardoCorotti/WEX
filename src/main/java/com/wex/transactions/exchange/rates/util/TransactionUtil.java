package com.wex.transactions.exchange.rates.util;

import com.wex.transactions.exchange.rates.domain.transaction.Transaction;
import com.wex.transactions.exchange.rates.dtos.TransactionCurrencyDTO;
import com.wex.transactions.exchange.rates.dtos.TransactionDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionUtil {

    @Autowired
    private ModelMapper modelMapper;

    public TransactionCurrencyDTO toTransactionCurrencyDTO(Transaction transaction) {
        return this.modelMapper.map(transaction, TransactionCurrencyDTO.class);
    }

    public Transaction toTransaction(TransactionDTO transactionDTO) {
        return this.modelMapper.map(transactionDTO, Transaction.class);
    }

}

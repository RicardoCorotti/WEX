package com.wex.transactions.exchange.rates.services;

import com.wex.transactions.exchange.rates.domain.transaction.Transaction;
import com.wex.transactions.exchange.rates.dtos.TransactionCurrencyDTO;
import com.wex.transactions.exchange.rates.dtos.TransactionDTO;
import com.wex.transactions.exchange.rates.repositories.TransactionRepository;
import com.wex.transactions.exchange.rates.util.TransactionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class TransactionService {

    @Autowired
    ExchangeRatesClientService exchangeRatesClientService;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    TransactionUtil transactionUtil;

    public Transaction createTransaction(TransactionDTO transactionDTO) {

        Transaction newTransaction = this.transactionUtil.toTransaction(transactionDTO);
        newTransaction.setAmount(newTransaction.getAmount().setScale(2, RoundingMode.HALF_EVEN));
        this.transactionRepository.save(newTransaction);

        return newTransaction;
    }

    public TransactionCurrencyDTO getTransactionConvertedToCurrency(Long transactionId, String country) throws Exception {

        Transaction transaction = this.transactionRepository.findTransactionById(transactionId).orElseThrow(() -> new Exception("Transaction not found!"));
        TransactionCurrencyDTO transactionCurrencyDTO = this.transactionUtil.toTransactionCurrencyDTO(transaction);

        BigDecimal exchangeRate = this.exchangeRatesClientService.retrieveExchangeRate(country, transaction.getDate());

        transactionCurrencyDTO.setExchangeRate(exchangeRate);
        transactionCurrencyDTO.setConvertedAmount(transactionCurrencyDTO.getAmount().multiply(exchangeRate).setScale(2, RoundingMode.HALF_EVEN));

        return transactionCurrencyDTO;
    }

}

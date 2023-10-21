package com.wex.transactions.exchange.rates.services;

import com.wex.transactions.exchange.rates.domain.transaction.Transaction;
import com.wex.transactions.exchange.rates.dtos.TransactionCurrencyDTO;
import com.wex.transactions.exchange.rates.dtos.TransactionDTO;
import com.wex.transactions.exchange.rates.model.ExchangeRate;
import com.wex.transactions.exchange.rates.model.ExchangeRates;
import com.wex.transactions.exchange.rates.repositories.TransactionRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository repository;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ModelMapper modelMapper;

    public Transaction createTransaction(TransactionDTO transactionDTO) {

        Transaction newTransaction = toTransaction(transactionDTO);
        newTransaction.setAmount(newTransaction.getAmount().setScale(2, RoundingMode.HALF_EVEN));
        this.repository.save(newTransaction);

        return newTransaction;
    }

    public TransactionCurrencyDTO getTransactionById(Long id) throws Exception {
        Transaction transaction = this.repository.findTransactionById(id).orElseThrow(() -> new Exception("Transaction not found!"));
        TransactionCurrencyDTO transactionCurrencyDTO = toTransactionCurrencyDTO(transaction);

        String baseUrl = "https://api.fiscaldata.treasury.gov/services/api/fiscal_service/v1/accounting/od/rates_of_exchange";
        String country = "Brazil";
        LocalDate filterEndDate = transaction.getDate();
        LocalDate filterStartDate = filterEndDate.minusMonths(6);

        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
                .fromHttpUrl(baseUrl)
                .queryParam("filter", "country:eq:" + country + ",record_date:lte:" + filterEndDate.toString() + ",record_date:gte:" + filterStartDate.toString())
                .queryParam("sort", "-record_date");
        String url = String.valueOf(uriComponentsBuilder.build().toUri());

        ResponseEntity<ExchangeRates> response = restTemplate.getForEntity(url, ExchangeRates.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new Exception("Communication failure with fiscal service");
        }

        ExchangeRates exchangeRates = response.getBody();

        if (exchangeRates == null || exchangeRates.getData() == null || exchangeRates.getData().size() == 0) {
            throw new Exception("Purchase cannot be converted to the target currency");
        }

        ExchangeRate exchangeRate = exchangeRates.getData().get(0);
        transactionCurrencyDTO.setExchangeRate(exchangeRate.getExchange_rate());
        transactionCurrencyDTO.setConvertedAmount(transactionCurrencyDTO.getAmount().multiply(exchangeRate.getExchange_rate()));

        return transactionCurrencyDTO;
    }

    private TransactionCurrencyDTO toTransactionCurrencyDTO(Transaction transaction) {
        return modelMapper.map(transaction, TransactionCurrencyDTO.class);
    }

    private Transaction toTransaction(TransactionDTO transactionDTO) {
        return modelMapper.map(transactionDTO, Transaction.class);
    }

    public List<Transaction> getAllTransactions() {
        return this.repository.findAll();
    }

}

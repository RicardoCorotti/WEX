package com.wex.transactions.exchange.rates.services;

import com.wex.transactions.exchange.rates.domain.transaction.Transaction;
import com.wex.transactions.exchange.rates.dtos.TransactionCurrencyDTO;
import com.wex.transactions.exchange.rates.repositories.TransactionRepository;
import com.wex.transactions.exchange.rates.util.TransactionUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class TransactionServiceTest {

    @Mock
    ExchangeRatesClientService exchangeRatesClientService;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private TransactionUtil transactionUtil;

    @Autowired
    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("Should get transaction with amount converted to currency when everything is OK")
    void getTransactionConvertedToCurrencyCase1() throws Exception {
        Transaction transaction1 = new Transaction(
                1L, "Product Test 1", LocalDate.of(2023, 10, 23), BigDecimal.valueOf(100L));
        when(transactionRepository.findTransactionById(1L)).thenReturn(Optional.of(transaction1));
        when(exchangeRatesClientService.retrieveExchangeRate(any(), any())).thenReturn(BigDecimal.valueOf(5L));
        when(transactionUtil.toTransactionCurrencyDTO(any())).thenReturn(new TransactionCurrencyDTO(
                1L, "Product Test 1", LocalDate.of(2023, 10, 23), BigDecimal.valueOf(100L),
                                null, null));

        TransactionCurrencyDTO expectedTransactionCurrencyDTO = new TransactionCurrencyDTO(
                1L, "Product Test 1", LocalDate.of(2023, 10, 23), BigDecimal.valueOf(100L),
                BigDecimal.valueOf(5L), BigDecimal.valueOf(500.0).setScale(2, RoundingMode.HALF_EVEN));
        TransactionCurrencyDTO returnedTransactionCurrencyDTO = transactionService.getTransactionConvertedToCurrency(1L, "Brazil");

        assertEquals(expectedTransactionCurrencyDTO, returnedTransactionCurrencyDTO);
    }

}
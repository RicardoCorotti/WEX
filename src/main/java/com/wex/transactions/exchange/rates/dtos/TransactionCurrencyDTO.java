package com.wex.transactions.exchange.rates.dtos;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class TransactionCurrencyDTO {

    private Long id;
    private String description;
    private LocalDate date;
    private BigDecimal amount;
    private BigDecimal exchangeRate;
    private BigDecimal convertedAmount;

}

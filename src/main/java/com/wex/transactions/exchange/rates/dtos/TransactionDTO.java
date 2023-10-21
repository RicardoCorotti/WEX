package com.wex.transactions.exchange.rates.dtos;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
@Data
public class TransactionDTO {

    private Long id;
    private String description;
    private LocalDate date;
    private BigDecimal amount;

}

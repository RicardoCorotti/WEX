package com.wex.transactions.exchange.rates.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TransactionDTO {

    private Long id;
    private String description;
    private LocalDate date;
    private BigDecimal amount;

}

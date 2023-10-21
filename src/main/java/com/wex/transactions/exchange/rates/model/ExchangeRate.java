package com.wex.transactions.exchange.rates.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
@Data
public class ExchangeRate {

    private LocalDate record_date;
    private String country;
    private String currency;
    private String country_currency_desc;
    private BigDecimal exchange_rate;
    private LocalDate effective_date;
    private Long src_line_nbr;
    private String record_fiscal_year;
    private String record_fiscal_quarter;
    private String record_calendar_year;
    private String record_calendar_quarter;
    private String record_calendar_month;
    private String record_calendar_day;

}

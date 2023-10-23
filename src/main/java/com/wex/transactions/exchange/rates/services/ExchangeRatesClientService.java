package com.wex.transactions.exchange.rates.services;

import com.wex.transactions.exchange.rates.model.ExchangeRate;
import com.wex.transactions.exchange.rates.model.ExchangeRates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class ExchangeRatesClientService {

    @Autowired
    private RestTemplate restTemplate;

    BigDecimal retrieveExchangeRate(String country, LocalDate transactionDate) throws Exception {

        String baseUrl = "https://api.fiscaldata.treasury.gov/services/api/fiscal_service/v1/accounting/od/rates_of_exchange";

        LocalDate filterEndDate = transactionDate;
        LocalDate filterStartDate = filterEndDate.minusMonths(6);

        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
                .fromHttpUrl(baseUrl)
                .queryParam("filter", "country:eq:" + country + ",record_date:lte:" + filterEndDate.toString() + ",record_date:gte:" + filterStartDate.toString())
                .queryParam("sort", "-record_date");
        String url = String.valueOf(uriComponentsBuilder.build().toUri());

        ResponseEntity<ExchangeRates> response = this.restTemplate.getForEntity(url, ExchangeRates.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new Exception("Communication failure with fiscal service");
        }

        ExchangeRates exchangeRates = response.getBody();

        if (exchangeRates == null || exchangeRates.getData() == null || exchangeRates.getData().size() == 0) {
            throw new Exception("Purchase cannot be converted to the target currency");
        }

        ExchangeRate exchangeRate = exchangeRates.getData().get(0);

        return exchangeRate.getExchange_rate();

    }
}

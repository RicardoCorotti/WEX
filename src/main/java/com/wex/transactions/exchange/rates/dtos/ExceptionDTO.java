package com.wex.transactions.exchange.rates.dtos;

import java.util.List;

public record ExceptionDTO(String statusCode, List<String> messages) {
}

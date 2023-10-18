package com.wex.transactions.exchange.rates.repositories;

import com.wex.transactions.exchange.rates.domain.transaction.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Optional<Transaction> findTransactionById(Long id);

}

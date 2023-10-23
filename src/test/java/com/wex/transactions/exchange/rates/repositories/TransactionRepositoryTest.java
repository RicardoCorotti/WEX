package com.wex.transactions.exchange.rates.repositories;

import com.wex.transactions.exchange.rates.domain.transaction.Transaction;
import com.wex.transactions.exchange.rates.dtos.TransactionDTO;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
@ActiveProfiles("test")
class TransactionRepositoryTest {

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    EntityManager entityManager;

    @Test
    @DisplayName("Should find transaction on DB")
    void findTransactionByIdCase1() {
        TransactionDTO transactionDTO = new TransactionDTO(null, "Test Product 1", LocalDate.now(), new BigDecimal(100));
        this.createTransaction(transactionDTO);

        Optional<Transaction> foundedTransaction = this.transactionRepository.findTransactionById(1L);
        assertThat(foundedTransaction.isPresent()).isTrue();
    }

    @Test
    @DisplayName("Should not find transaction on DB")
    void findTransactionByIdCase2() {
        Optional<Transaction> foundedTransaction = this.transactionRepository.findTransactionById(1L);
        assertThat(foundedTransaction.isEmpty()).isTrue();
    }


    private Transaction createTransaction(TransactionDTO transactionDTO) {
        Transaction newTransaction = new Transaction(transactionDTO);
        this.entityManager.persist(newTransaction);
        return newTransaction;
    }
}
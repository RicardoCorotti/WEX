package com.wex.transactions.exchange.rates.controllers;

import com.wex.transactions.exchange.rates.domain.transaction.Transaction;
import com.wex.transactions.exchange.rates.dtos.TransactionCurrencyDTO;
import com.wex.transactions.exchange.rates.dtos.TransactionDTO;
import com.wex.transactions.exchange.rates.services.TransactionService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService service;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<TransactionDTO> createTransaction(@RequestBody TransactionDTO transactionDTO) {

        Transaction newTransaction = this.service.createTransaction(transactionDTO);
        return new ResponseEntity<>(toTransactionDTO(newTransaction), HttpStatus.CREATED);

    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionCurrencyDTO> getTransactionById(@PathVariable Long id) throws Exception {
        TransactionCurrencyDTO transactionCurrencyDTO = this.service.getTransactionById(id);
        return new ResponseEntity<>(transactionCurrencyDTO, HttpStatus.OK);
    }

    private TransactionDTO toTransactionDTO(Transaction transaction) {
        return modelMapper.map(transaction, TransactionDTO.class);
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        List<Transaction> transactions = this.service.getAllTransactions();
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

}

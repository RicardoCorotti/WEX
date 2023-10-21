package com.wex.transactions.exchange.rates.domain.transaction;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity(name="transactions")
@Table(name="transactions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of="id")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max=50, message = "Product description should not exceed 50 characters!")
    private String description;

    private LocalDate date;

    @Positive(message = "Purchase amount should be a positive value!")
    private BigDecimal amount;

}

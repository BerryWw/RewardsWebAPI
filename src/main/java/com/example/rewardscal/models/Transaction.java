package com.example.rewardscal.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

/*
 Model for each transaction
 */
@Data
@AllArgsConstructor
public class Transaction {
    private String customerId;
    private double purchaseAmount;
    private LocalDate transactionData;
}
package com.example.rewardscal.controller;


import com.example.rewardscal.models.Transaction;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 Controller class for this project, including post single/multiple transaction, get per month/total points per customer
 */
@RestController
@RequestMapping("/rewards")
public class RewardsController {
    //all the transactions
    private List<Transaction> transactions = new ArrayList<>();

    //post method to add a single transaction
    @PostMapping("/transatcions")
    public ResponseEntity<String> addTransaction(@RequestBody Transaction transaction) {
        transactions.add(transaction);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //post method to add more than one transactions
    @PostMapping("/transactions/all")
    public ResponseEntity<String> addTransactions(@RequestBody List<Transaction> allTransactions) {
        transactions.addAll(allTransactions);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    //Get a customer's monthly points
    @GetMapping("/points/{customerId}/{yearMonth}")
    public ResponseEntity<Map<String, Integer>> getRewardsPoints(
            @PathVariable String customerId,
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM") YearMonth yearMonth
    ) {
        int totalPoints = 0;
        Map<String, Integer> pointsPerMonth = new HashMap<>();
        for (Transaction transaction : transactions) {
            if (transaction.getCustomerId().equals(customerId)
                    && YearMonth.from(transaction.getTransactionData()).equals(yearMonth)) {
                int points = calculateRewardPoints(transaction.getPurchaseAmount());
                totalPoints += points;
                String monthKey = transaction.getTransactionData().getMonth().toString();
                pointsPerMonth.put(monthKey, pointsPerMonth.getOrDefault(monthKey, 0) + totalPoints);
            }
        }
        return ResponseEntity.ok(pointsPerMonth);
    }

    //Get the total points of a customer
    @GetMapping("points/{customerId}/total")
    public ResponseEntity<Integer> getTotalPoints(@PathVariable String customerId) {
        int totalPoints = 0;
        for (Transaction transaction : transactions) {
            if (transaction.getCustomerId().equals(customerId)) {
                totalPoints += calculateRewardPoints(transaction.getPurchaseAmount());
            }
        }
        return ResponseEntity.ok(totalPoints);
    }

    //points calculator
    private int calculateRewardPoints(double purchaseAmount) {
        int points = 0;

        if (purchaseAmount > 100) {
            points += (int) ((purchaseAmount - 100) * 2);
            points += 50;
        } else if (purchaseAmount > 50 && purchaseAmount < 100) {
            points += (int) (purchaseAmount - 50);
        }
        return points;

    }

}

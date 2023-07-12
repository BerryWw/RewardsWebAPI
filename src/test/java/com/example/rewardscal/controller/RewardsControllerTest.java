package com.example.rewardscal.controller;

import com.example.rewardscal.models.Transaction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;



public class RewardsControllerTest {

    private RewardsController rewardsController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        rewardsController = new RewardsController();
    }

    @Test
    public void testGetRewardPoints() {
        // Test transactions
        Transaction transaction1 = new Transaction("123", 120.0, LocalDate.of(2023, 1, 15));
        ResponseEntity<String> postResponse = rewardsController.addTransaction(transaction1);
        // Expected result
        Map<String, Integer> expectedPointsPerMonth = new HashMap<>();
        expectedPointsPerMonth.put("JANUARY", 90);

        // Call the endpoint
        ResponseEntity<Map<String, Integer>> responseEntity = rewardsController.getRewardsPoints("123", YearMonth.of(2023, 1));

        // Verify the response
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertEquals(expectedPointsPerMonth, responseEntity.getBody());
    }

    @Test
    public void testGetTotalPoints() {
        // Mock transactions
        Transaction transaction1 = new Transaction("123", 120.0, LocalDate.of(2023, 1, 15));
        Transaction transaction2 = new Transaction("123", 70.0, LocalDate.of(2023, 1, 20));
        Transaction transaction3 = new Transaction("123", 80.0, LocalDate.of(2023, 2, 10));
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction1);
        transactions.add(transaction2);
        transactions.add(transaction3);
        // post the transactions
        ResponseEntity<String> postResponse = rewardsController.addTransactions(transactions);
        // Expected result
        int expectedTotalPoints = 140;

        // Call the endpoint
        ResponseEntity<Integer> responseEntity = rewardsController.getTotalPoints("123");

        // Verify the response
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertEquals(expectedTotalPoints, responseEntity.getBody());
    }
}

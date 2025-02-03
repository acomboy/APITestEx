package com.api.controller;


import com.api.entity.Account;
import com.api.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/api/v1/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping
    public ResponseEntity<?> createAccount(@Valid @RequestBody Account account) {
        Account newAccount = accountService.createAccount(account);
        return new ResponseEntity<>(newAccount, HttpStatus.CREATED);
    }

    @GetMapping("/{customerNumber}")
    public ResponseEntity<?> getAccount(@PathVariable Long customerNumber) {
        Optional<Account> accountOptional = accountService.getAccountByCustomerNumber(customerNumber);

        if (!accountOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found");
        }

        Account account = accountOptional.get();

        // Sample response structure - can add savings and other related data
        Map<String, Object> response = new HashMap<>();
        response.put("customerNumber", account.getCustomerNumber());
        response.put("customerName", account.getCustomerName());
        response.put("customerMobile", account.getCustomerMobile());
        response.put("customerEmail", account.getCustomerEmail());
        response.put("address1", account.getAddress1());
        response.put("address2", account.getAddress2());

        // Add a dummy savings account for the sake of the example
        List<Map<String, Object>> savings = new ArrayList<>();
        Map<String, Object> savingsAccount = new HashMap<>();
        savingsAccount.put("accountNumber", 10001);
        savingsAccount.put("accountType", "Savings");
        savingsAccount.put("availableBalance", 500);
        savings.add(savingsAccount);

        response.put("savings", savings);

        // Add transaction status
        response.put("transactionStatusCode", HttpStatus.FOUND.value());
        response.put("transactionStatusDescription", "Customer Account found");

        return ResponseEntity.status(HttpStatus.FOUND).body(response);
    }
}

package com.api.controller;

import com.api.entity.Account;
import com.api.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/api/v1/account")
public class AccountController {

    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    private AccountService accountService;

    @PostMapping
    public ResponseEntity<?> createAccount(@Valid @RequestBody Account account) {
            Account newAccount = accountService.createAccount(account);
            logger.info("Account created successfully for customerNumber: {}", newAccount.getCustomerNumber());
            return new ResponseEntity<>(newAccount, HttpStatus.CREATED);

    }

    @GetMapping("/{customerNumber}")
    public ResponseEntity<?> getAccount(@PathVariable Long customerNumber) {
        try {
            Optional<Account> accountOptional = accountService.getAccountByCustomerNumber(customerNumber);
            Map<String, Object> response = new HashMap<>();

            if (!accountOptional.isPresent()) {
                logger.warn("Customer not found: {}", customerNumber);
                return buildErrorResponse(HttpStatus.NOT_FOUND, "Customer not found");
            }

            Account account = accountOptional.get();

            response.put("customerNumber", account.getCustomerNumber());
            response.put("customerName", account.getCustomerName());
            response.put("customerMobile", account.getCustomerMobile());
            response.put("customerEmail", account.getCustomerEmail());
            response.put("address1", account.getAddress1());
            response.put("address2", account.getAddress2());

            List<Map<String, Object>> savings = new ArrayList<>();
            Map<String, Object> savingsAccount = new HashMap<>();
            savingsAccount.put("accountNumber", 10001);
            savingsAccount.put("accountType", "Savings");
            savingsAccount.put("availableBalance", 500);
            savings.add(savingsAccount);

            response.put("savings", savings);
            response.put("transactionStatusCode", HttpStatus.OK.value());
            response.put("transactionStatusDescription", "Customer Account found");

            logger.info("Customer account retrieved successfully: {}", customerNumber);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            logger.error("Error retrieving account for customerNumber {}: {}", customerNumber, ex.getMessage(), ex);
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while retrieving the account");
        }
    }


    private ResponseEntity<Map<String, Object>> buildErrorResponse(HttpStatus status, String message) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("transactionStatusCode", status.value());
        errorResponse.put("transactionStatusDescription", message);
        return ResponseEntity.status(status).body(errorResponse);
    }
}
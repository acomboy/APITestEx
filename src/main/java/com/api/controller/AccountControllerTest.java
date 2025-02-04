package com.api.controller;

import com.api.entity.Account;
import com.api.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class AccountControllerTest {

    @Mock
    private AccountService accountService;  

    @InjectMocks
    private AccountController accountController; 

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);  
    }

    @Test
    void testCreateAccount() {

        Account newAccount = new Account();
        newAccount.setCustomerNumber(123L);
        when(accountService.createAccount(any(Account.class))).thenReturn(newAccount);


        ResponseEntity<?> response = accountController.createAccount(newAccount);

  
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof Account);
        assertEquals(123L, ((Account) response.getBody()).getCustomerNumber());
        verify(accountService, times(1)).createAccount(any(Account.class));  
    }

    @Test
    void testGetAccount_Success() {
       
        Account existingAccount = new Account();
        existingAccount.setCustomerNumber(123L);
        existingAccount.setCustomerName("Allan C");
        when(accountService.getAccountByCustomerNumber(123L)).thenReturn(Optional.of(existingAccount));

        ResponseEntity<?> response = accountController.getAccount(123L);

     
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof Map);
        assertTrue(((Map<?, ?>) response.getBody()).containsKey("customerName"));
        assertEquals("Allan C", ((Map<?, ?>) response.getBody()).get("customerName"));
        verify(accountService, times(1)).getAccountByCustomerNumber(123L);  
    }

    @Test
    void testGetAccount_NotFound() {

        when(accountService.getAccountByCustomerNumber(123L)).thenReturn(Optional.empty());

        
        ResponseEntity<?> response = accountController.getAccount(123L);


        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        Map<?, ?> responseBody = (Map<?, ?>) response.getBody();
        assertEquals("Customer not found", responseBody.get("transactionStatusDescription"));
        verify(accountService, times(1)).getAccountByCustomerNumber(123L); 
    }

    @Test
    void testGetAccount_InternalServerError() {

        when(accountService.getAccountByCustomerNumber(123L)).thenThrow(new RuntimeException("Error"));

     
        ResponseEntity<?> response = accountController.getAccount(123L);


        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        Map<?, ?> responseBody = (Map<?, ?>) response.getBody();
        assertEquals("An error occurred while retrieving the account", responseBody.get("transactionStatusDescription"));
        verify(accountService, times(1)).getAccountByCustomerNumber(123L); 
    }
}

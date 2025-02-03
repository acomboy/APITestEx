package com.api.service;

import com.api.entity.Account;
import com.api.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    public Optional<Account> getAccountByCustomerNumber(Long customerNumber) {
        return accountRepository.findById(customerNumber);
    }

}

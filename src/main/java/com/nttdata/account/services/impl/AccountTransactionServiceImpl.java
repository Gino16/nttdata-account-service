package com.nttdata.account.services.impl;

import com.nttdata.account.entities.AccountTransaction;
import com.nttdata.account.repository.AccountTransactionRepository;
import com.nttdata.account.services.AccountTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountTransactionServiceImpl implements AccountTransactionService {

    @Autowired
    private AccountTransactionRepository accountTransactionRepository;

    @Override
    public AccountTransaction save(AccountTransaction accountTransaction) {
        return accountTransactionRepository.save(accountTransaction);
    }

    @Override
    public List<AccountTransaction> findAll() {
        return accountTransactionRepository.findAll();
    }

    @Override
    public AccountTransaction findOneById(Long id) {
        return accountTransactionRepository.findById(id).orElse(null);
    }

    @Override
    public List<AccountTransaction> findAllByBankAccountId(Long id) {
        return accountTransactionRepository.findAllByBankAccount(id);
    }
}

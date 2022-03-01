package com.nttdata.account.services.impl;

import com.nttdata.account.entities.AccountType;
import com.nttdata.account.entities.BankAccount;
import com.nttdata.account.repository.BankAccountRepository;
import com.nttdata.account.services.BankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankAccountServiceImpl implements BankAccountService {

    @Autowired
    private BankAccountRepository repository;

    @Override
    public List<BankAccount> findAll() {
        return repository.findAll();
    }

    @Override
    public BankAccount findOneById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public BankAccount create(BankAccount bankAccount) {
        return repository.save(bankAccount);
    }

    @Override
    public BankAccount edit(Long id, BankAccount bankAccount) {
        BankAccount newBankAccount = this.findOneById(id);
        newBankAccount.setCommission(bankAccount.getCommission());
        newBankAccount.setMovement_limit(bankAccount.getMovement_limit());
        return newBankAccount;
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<AccountType> findAllAccountTypes() {
        return repository.listAccountType();
    }
}

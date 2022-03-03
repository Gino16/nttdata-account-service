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
    private BankAccountRepository bankAccountRepository;

    @Override
    public List<BankAccount> findAll() {
        return bankAccountRepository.findAll();
    }

    @Override
    public BankAccount findOneById(Long id) {
        return bankAccountRepository.findById(id).orElse(null);
    }

    @Override
    public BankAccount save(BankAccount bankAccount) {
        return bankAccountRepository.save(bankAccount);
    }

    @Override
    public void delete(Long id) {
        bankAccountRepository.deleteById(id);
    }

    @Override
    public List<AccountType> findAllAccountTypes() {
        return bankAccountRepository.listAccountType();
    }

    @Override
    public AccountType searchAccountTypeById(Long id) {
        return bankAccountRepository.searchAccountTypeById(id);
    }
}

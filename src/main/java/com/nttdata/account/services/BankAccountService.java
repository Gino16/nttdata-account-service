package com.nttdata.account.services;

import com.nttdata.account.entities.AccountType;
import com.nttdata.account.entities.BankAccount;

import java.util.List;

public interface BankAccountService {
    public List<BankAccount> findAll();

    public BankAccount findOneById(Long id);

    public BankAccount save(BankAccount creditCard);


    public void delete(Long id);

    public List<AccountType> findAllAccountTypes();

    public AccountType searchAccountTypeById(Long id);
}

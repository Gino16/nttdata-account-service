package com.nttdata.account.services;

import com.nttdata.account.entities.AccountType;
import com.nttdata.account.entities.BankAccount;

import java.util.List;

public interface BankAccountService {
    public List<BankAccount> findAll();

    public BankAccount findOneById(Long id);

    public BankAccount create(BankAccount creditCard);

    public BankAccount edit(Long id, BankAccount creditCard);

    public void delete(Long id);

    public List<AccountType> findAllAccountTypes();


}

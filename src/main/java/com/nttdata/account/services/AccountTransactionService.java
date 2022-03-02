package com.nttdata.account.services;

import com.nttdata.account.entities.AccountTransaction;

import java.util.List;

public interface AccountTransactionService {
    public AccountTransaction save(AccountTransaction accountTransaction);

    public List<AccountTransaction> findAll();

    public AccountTransaction findOneById(Long id);

    public List<AccountTransaction> findAllByBankAccountId(Long id);
}

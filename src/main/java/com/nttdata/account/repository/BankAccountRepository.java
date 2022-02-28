package com.nttdata.account.repository;

import com.nttdata.account.entities.AccountType;
import com.nttdata.account.entities.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {

    @Query("SELECT at from AccountType at")
    public List<AccountType> listAccountType();

    @Query("SELECT at from AccountType at WHERE at.id = :id")
    public List<AccountType> searchAccountTypeById(Long id);
}

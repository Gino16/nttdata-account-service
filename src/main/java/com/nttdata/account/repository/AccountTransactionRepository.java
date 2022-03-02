package com.nttdata.account.repository;

import com.nttdata.account.entities.AccountTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AccountTransactionRepository extends JpaRepository<AccountTransaction, Long> {

    @Query("SELECT at from AccountTransaction at where at.bankAccount.id = :id")
    public List<AccountTransaction> findAllByBankAccount(Long id);
}

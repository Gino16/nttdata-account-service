package com.nttdata.account.repository;

import com.nttdata.account.entities.AccountTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountTransactionRepository extends JpaRepository<AccountTransaction, Long> {
}

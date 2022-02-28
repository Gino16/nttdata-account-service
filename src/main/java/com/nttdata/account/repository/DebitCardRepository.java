package com.nttdata.account.repository;

import com.nttdata.account.entities.DebitCard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DebitCardRepository extends JpaRepository<DebitCard, Long> {
}

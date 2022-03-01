package com.nttdata.account.repository;

import com.nttdata.account.entities.DebitCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DebitCardRepository extends JpaRepository<DebitCard, Long> {

    @Query("SELECT dc from DebitCard dc where dc.idCustomer = :id")
    public List<DebitCard> findAllByCustomers(Long id);
}

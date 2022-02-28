package com.nttdata.account.services;

import com.nttdata.account.entities.Customer;
import com.nttdata.account.entities.DebitCard;

import java.util.List;

public interface DebitCardService {

    public Customer findCustomerById(Long id);

    public List<DebitCard> findAll();

    public DebitCard findOneById(Long id);

    public DebitCard create(DebitCard debitCard);

    public DebitCard edit(Long id, DebitCard debitCard);

    public void delete(Long id);
}

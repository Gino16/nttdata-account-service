package com.nttdata.account.services.impl;

import com.nttdata.account.clients.CustomerClient;
import com.nttdata.account.entities.Customer;
import com.nttdata.account.entities.DebitCard;
import com.nttdata.account.repository.DebitCardRepository;
import com.nttdata.account.services.DebitCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DebitCardServiceImpl implements DebitCardService {

    @Autowired
    private DebitCardRepository repository;

    @Autowired
    private CustomerClient customerClient;

    public Customer findCustomerById(Long id){
        return customerClient.findOneById(id);
    }

    @Autowired
    private CustomerClient customerRepository;

    @Override
    public List<DebitCard> findAll() {
        return repository.findAll();
    }

    @Override
    public DebitCard findOneById(Long id) {
        DebitCard debitCard = repository.findById(id).orElse(null);

        Customer customer = customerRepository.findOneById(debitCard.getIdCustomer());

        if (customer == null){
            return null;
        }

        debitCard.setCustomer(customer);
        return debitCard;
    }

    @Override
    public DebitCard create(DebitCard debitCard) {
        Customer customer = customerRepository.findOneById(debitCard.getIdCustomer());
        if (customer == null){
            return null;
        }

        return repository.save(debitCard);
    }

    @Override
    public DebitCard edit(Long id, DebitCard debitCard) {
        DebitCard newCard = this.findOneById(id);
        if (newCard == null)
            return null;

        newCard.setNumber(debitCard.getNumber());
        return repository.save(newCard);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}

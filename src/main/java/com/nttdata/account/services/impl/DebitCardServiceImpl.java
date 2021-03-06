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
    private DebitCardRepository debitCardRepository;

    @Autowired
    private CustomerClient customerRepository;

    public Customer findCustomerById(Long id){
        return customerRepository.findOneById(id);
    }


    @Override
    public List<DebitCard> findAll() {
        return debitCardRepository.findAll();
    }

    @Override
    public List<DebitCard> findAllByIdCustomer(Long id){
        return debitCardRepository.findAllByCustomers(id);
    }


    @Override
    public DebitCard findOneById(Long id) {
        DebitCard debitCard = debitCardRepository.findById(id).orElse(null);

        Customer customer = customerRepository.findOneById(debitCard.getIdCustomer());

        if (customer == null){
            return null;
        }

        debitCard.setCustomer(customer);
        return debitCard;
    }

    @Override
    public DebitCard save(DebitCard debitCard) {
        Customer customer = customerRepository.findOneById(debitCard.getIdCustomer());
        if (customer == null){
            return null;
        }

        return debitCardRepository.save(debitCard);
    }

    @Override
    public DebitCard edit(Long id, DebitCard debitCard) {
        DebitCard newCard = this.findOneById(id);
        if (newCard == null)
            return null;

        newCard.setNumber(debitCard.getNumber());
        return debitCardRepository.save(newCard);
    }

    @Override
    public void delete(Long id) {
        debitCardRepository.deleteById(id);
    }
}

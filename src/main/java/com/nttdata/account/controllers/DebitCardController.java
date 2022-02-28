package com.nttdata.account.controllers;

import com.nttdata.account.entities.AccountType;
import com.nttdata.account.entities.BankAccount;
import com.nttdata.account.entities.Customer;
import com.nttdata.account.entities.DebitCard;
import com.nttdata.account.services.BankAccountService;
import com.nttdata.account.services.DebitCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/debit-cards")
public class DebitCardController {

    @Autowired
    private DebitCardService debitCardService;

    @Autowired
    private BankAccountService bankAccountService;

    @GetMapping("/")
    public ResponseEntity<List<DebitCard>> list(){
        return ResponseEntity.ok(debitCardService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DebitCard> findOneById(@PathVariable Long id){
        DebitCard debitCard = debitCardService.findOneById(id);
        if (debitCard == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(debitCard);
    }

    @PostMapping("/")
    public ResponseEntity<DebitCard> create(@RequestBody DebitCard debitCard){

        Customer customer = debitCardService.findCustomerById(debitCard.getIdCustomer());
        BankAccount bankAccount = bankAccountService.findOneById(debitCard.getBankAccount().getId());
        if (customer == null){
            return ResponseEntity.badRequest().build();
        }

        if (Objects.equals(customer.getCustomerType().getName(), "Empresarial")){
            if (!Objects.equals(bankAccount.getAccountType().getName(), "cuenta corriente")){
                return ResponseEntity.badRequest().build();
            }
        }

        DebitCard newDebitCard = debitCardService.create(debitCard);

        return ResponseEntity.status(HttpStatus.CREATED).body(newDebitCard);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DebitCard> update(@PathVariable Long id, @RequestBody DebitCard debitCard){
        DebitCard debitCardUpdate = debitCardService.edit(id, debitCard);
        if (debitCardUpdate == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(debitCardUpdate);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id){
        debitCardService.delete(id);
    }

}

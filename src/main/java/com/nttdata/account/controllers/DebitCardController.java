package com.nttdata.account.controllers;

import com.nttdata.account.entities.AccountTransaction;
import com.nttdata.account.entities.BankAccount;
import com.nttdata.account.entities.Customer;
import com.nttdata.account.entities.DebitCard;
import com.nttdata.account.services.AccountTransactionService;
import com.nttdata.account.services.BankAccountService;
import com.nttdata.account.services.DebitCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/debit-cards")
public class DebitCardController {

    @Autowired
    private DebitCardService debitCardService;

    @Autowired
    private BankAccountService bankAccountService;

    @Autowired
    private AccountTransactionService accountTransactionService;

    // Get All Debit Card
    @GetMapping("/")
    public ResponseEntity<List<DebitCard>> list() {
        return ResponseEntity.ok(debitCardService.findAll());
    }

    // Get one Debit Card by ID
    @GetMapping("/{id}")
    public ResponseEntity<DebitCard> findOneById(@PathVariable Long id) {
        DebitCard debitCard = debitCardService.findOneById(id);
        if (debitCard == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(debitCard);
    }

    // Create a new Debit Card
    @PostMapping("/")
    public ResponseEntity<DebitCard> create(@RequestBody DebitCard debitCard) {

        try {
            Customer customer = debitCardService.findCustomerById(debitCard.getIdCustomer());
            BankAccount bankAccount = bankAccountService.findOneById(debitCard.getBankAccount().getId());

            if (customer == null || bankAccount == null) {
                return ResponseEntity.badRequest().build();
            }

            List<DebitCard> cards = debitCardService.findAllByIdCustomer(customer.getId());

            // Validate if debit card is EMPRESARIAL or PERSONAL
            if (Objects.equals(customer.getCustomerType().getName(), "Empresarial")) {
                if (!Objects.equals(bankAccount.getAccountType().getName(), "cuenta corriente")) {
                    return ResponseEntity.badRequest().build();
                }
            } else if (Objects.equals(customer.getCustomerType().getName(), "Personal")) {
                // Validate if exist PERSONAL debit cards
                if (!cards.isEmpty()) {
                    AtomicInteger quantSavingAccounts = new AtomicInteger();
                    AtomicInteger quantCurrentAndFixedTermAccount = new AtomicInteger();
                    cards.forEach((card) -> {
                        if (Objects.equals(card.getBankAccount().getAccountType().getName(), "ahorro")) {
                            quantSavingAccounts.getAndIncrement();
                        } else {
                            quantCurrentAndFixedTermAccount.getAndIncrement();
                        }
                    });

                    if (quantSavingAccounts.get() >= 1 && quantCurrentAndFixedTermAccount.get() >= 1) {
                        return ResponseEntity.badRequest().build();
                    }

                    if (Objects.equals(bankAccount.getAccountType().getName(), "ahorro") && quantSavingAccounts.get() >= 1) {
                        return ResponseEntity.badRequest().build();
                    }

                    if (!Objects.equals(bankAccount.getAccountType().getName(), "ahorro") && quantCurrentAndFixedTermAccount.get() >= 1) {
                        return ResponseEntity.badRequest().build();
                    }
                }
            }

            DebitCard newDebitCard = debitCardService.save(debitCard);

            return ResponseEntity.status(HttpStatus.CREATED).body(newDebitCard);
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    // Update data of Debit Card
    @PutMapping("/{id}")
    public ResponseEntity<DebitCard> update(@PathVariable Long id, @RequestBody DebitCard debitCard) {
        DebitCard debitCardUpdate = debitCardService.edit(id, debitCard);
        if (debitCardUpdate == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(debitCardUpdate);
    }

    // Delete a Debit Card
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        debitCardService.delete(id);
    }


    // Make Transactions and Save History
    // transaction -> 1 = deposit, 2 = withdrawal
    @GetMapping("/customer/{id}/id-card/{idDebitCard}/transaction/{transaction}/quantity/{quantity}")
    public ResponseEntity<?> transaction(@PathVariable Long id, @PathVariable Long idDebitCard, @PathVariable Long transaction, @PathVariable Double quantity) {

        Customer customer = debitCardService.findCustomerById(id);

        if (customer == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        List<DebitCard> debitCards = debitCardService.findAllByIdCustomer(customer.getId());

        if (debitCards == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        List<DebitCard> cardOwners = debitCards.stream().filter((debitCard) -> Objects.equals(debitCard.getId(), idDebitCard)).collect(Collectors.toList());

        if (cardOwners.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }


        DebitCard card = cardOwners.get(0);

        if (card.getBankAccount().getMovementLimit() > 0 && card.getBankAccount().getMovementQuant() >= card.getBankAccount().getMovementLimit()) {
            return ResponseEntity.badRequest().build();
        }

        if (transaction == 1) {
            card.deposit(quantity);
        } else if (transaction == 2 && card.getBankAccount().getCurrentBalance() >= quantity) {
            card.withdrawal(quantity);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        card.getBankAccount().setMovementQuant(card.getBankAccount().getMovementQuant() + 1);


        // Save History
        AccountTransaction accountTransaction = new AccountTransaction();
        accountTransaction.setConcept(transaction == 1 ? "Deposit" : "Withdrawal");
        accountTransaction.setAmount(quantity);
        accountTransaction.setDate(new Date());
        accountTransaction.setBankAccount(card.getBankAccount());

        debitCardService.save(card);
        accountTransactionService.save(accountTransaction);

        return ResponseEntity.ok(card);

    }

    // Get History of a Debit Card
    @GetMapping("/history/debit-card/{idDebitCard}")
    public ResponseEntity<List<AccountTransaction>> history(@PathVariable Long idDebitCard) {
        DebitCard debitCard = this.debitCardService.findOneById(idDebitCard);
        if (debitCard == null) {
            return ResponseEntity.badRequest().build();
        }
        List<AccountTransaction> accountTransactions = this.accountTransactionService.findAllByBankAccountId(debitCard.getBankAccount().getId());
        return ResponseEntity.ok(accountTransactions);
    }
}

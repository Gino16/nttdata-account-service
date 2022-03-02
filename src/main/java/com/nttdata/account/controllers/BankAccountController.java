package com.nttdata.account.controllers;

import com.nttdata.account.entities.AccountType;
import com.nttdata.account.entities.BankAccount;
import com.nttdata.account.services.BankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bank-accounts")
public class BankAccountController {

    @Autowired
    private BankAccountService bankAccountService;

    @GetMapping("/")
    public ResponseEntity<List<BankAccount>> list() {
        return ResponseEntity.ok(bankAccountService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BankAccount> findOneById(@PathVariable Long id) {
        BankAccount bankAccount = bankAccountService.findOneById(id);
        if (bankAccount == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(bankAccount);
    }

    @PostMapping("/")
    public ResponseEntity<BankAccount> create(@RequestBody BankAccount bankAccount) {
        AccountType accountType = bankAccountService.searchAccountTypeById(bankAccount.getAccountType().getId());

        switch (accountType.getName()) {
            case "ahorro":
                bankAccount.setCommission(0.0);
                bankAccount.setMovementLimit(3);
                break;
            case "cuenta corriente":
                bankAccount.setCommission(12.0);
                bankAccount.setMovementLimit(-1);
                break;
            case "plazo fijo":
                bankAccount.setCommission(0.0);
                bankAccount.setMovementLimit(1);
                break;
        }

        bankAccount.setMovementQuant(0);
        bankAccount.setCurrentBalance(0.0);
        return ResponseEntity.status(HttpStatus.CREATED).body(bankAccountService.create(bankAccount));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BankAccount> update(@PathVariable Long id, @RequestBody BankAccount bankAccount) {
        BankAccount newBankAccount = bankAccountService.edit(id, bankAccount);
        if (newBankAccount == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(newBankAccount);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        bankAccountService.delete(id);
    }

    @GetMapping("/account-types")
    public List<AccountType> listTypes() {
        return bankAccountService.findAllAccountTypes();
    }

}

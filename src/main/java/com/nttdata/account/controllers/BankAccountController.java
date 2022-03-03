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

    // Get All Bank Accounts
    @GetMapping("/")
    public ResponseEntity<List<BankAccount>> list() {
        return ResponseEntity.ok(bankAccountService.findAll());
    }

    // Get One Bank Account By ID
    @GetMapping("/{id}")
    public ResponseEntity<BankAccount> findOneById(@PathVariable Long id) {
        BankAccount bankAccount = bankAccountService.findOneById(id);
        if (bankAccount == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(bankAccount);
    }

    // Create a new Bank Account
    @PostMapping("/")
    public ResponseEntity<BankAccount> create(@RequestBody BankAccount bankAccount) {
        AccountType accountType = bankAccountService.searchAccountTypeById(bankAccount.getAccountType().getId());
        if (accountType == null){
            return ResponseEntity.badRequest().build();
        }
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
        BankAccount newBankAccount = bankAccountService.save(bankAccount);
        return ResponseEntity.status(HttpStatus.CREATED).body(newBankAccount);
    }


    // Delete a Bank Account
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        bankAccountService.delete(id);
    }

    // Get All Account Types
    @GetMapping("/account-types")
    public List<AccountType> listTypes() {
        return bankAccountService.findAllAccountTypes();
    }

}

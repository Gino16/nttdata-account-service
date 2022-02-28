package com.nttdata.account.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "debit_cards")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DebitCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String number;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = {"applications", "hibernateLazyInitializer"})
    private BankAccount bankAccount;

    private Long idCustomer;

    @Transient
    private Customer customer;
}

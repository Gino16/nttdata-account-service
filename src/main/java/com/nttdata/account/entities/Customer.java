package com.nttdata.account.entities;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Customer {
    private Long id;
    private String name;
    private String identDoc;

    private CustomerType customerType;
}

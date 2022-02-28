package com.nttdata.account.clients;

import com.nttdata.account.entities.Customer;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "customer-service", path = "/customers", decode404 = true)
public interface CustomerClient {

    @GetMapping("/{id}")
    public Customer findOneById(@PathVariable Long id);
}

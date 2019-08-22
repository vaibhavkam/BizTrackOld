package com.intuit.vkamble.biztrack.customer.controllers;

import com.intuit.vkamble.biztrack.customer.models.Customer;
import com.intuit.vkamble.biztrack.customer.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/v1/customers")
public class CustomerController {

    private CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public Customer create(@Valid @RequestBody Customer customer){
        return customerService.create(customer);
    }

    @PutMapping(value = "/{id}")
    public Customer update(@Valid @RequestBody Customer updatedCustomer, @PathVariable long id){
        return customerService.update(updatedCustomer, id);
    }

    @GetMapping(value = "/{id}")
    public Customer getById(@PathVariable long id){
        return customerService.getById(id);
    }

    @GetMapping
    public List<Customer> getAll(@RequestParam(required = false) Integer offset, @RequestParam(required = false) Integer limit){
        return customerService.getAll(offset,limit);
    }
}
package com.intuit.vkamble.biztrack.customer.services;

import com.intuit.vkamble.biztrack.customer.models.Customer;
import com.intuit.vkamble.biztrack.customer.repositories.CustomerRepository;
import exceptions.EntityNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class CustomerServiceTest {

    private CustomerRepository customerRepository = Mockito.mock(CustomerRepository.class);

    private CustomerService customerService;

    @Before
    public void init(){
        customerService = new CustomerService(customerRepository);
    }

    @Test
    public void createCustomerTest(){

        Customer customer = new Customer();
        customer.setFirstName("Vaibhav");
        customer.setLastName("Kamble");

        when(customerRepository.save(any(Customer.class))).then(returnsFirstArg());

        Customer customerCreated = customerService.create(customer);
        assertEquals(customer, customerCreated);
    }

    @Test
    public void updateCustomerTest(){

        Customer customer = new Customer();
        customer.setId(1L);
        customer.setFirstName("Vaibhav");
        customer.setLastName("Kamble");

        when(customerRepository.save(customer)).then(returnsFirstArg());
        when(customerRepository.existsById(customer.getId())).thenReturn(true);
        when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));
        Customer customerSaved = customerService.update(customer,1);
        assertEquals(customer, customerSaved);
    }

    @Test
    public void getExitingCustomerByIdTest(){

        Customer customer = new Customer();
        customer.setFirstName("Vaibhav");
        customer.setLastName("Kamble");

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        Customer customerFetched = customerService.getById(1L);
        assertNotNull(customerFetched);
        assertEquals(customer, customerFetched);
    }

    @Test(expected = EntityNotFoundException.class)
    public void getNonExitingCustomerByIdTest(){

        when(customerRepository.findById(1L)).thenReturn(Optional.empty());
        customerService.getById(1L);
    }

    @Test
    public void isCustomerExistsTestForExitingCustomer(){

        when(customerRepository.existsById(1L)).thenReturn(true);
        boolean exists = customerService.existsById(1L);
        assertTrue(exists);
    }

    @Test
    public void isCustomerExistsTestForNonExitingCustomer(){

        when(customerRepository.findById(1L)).thenReturn(Optional.empty());
        boolean exists = customerService.existsById(1L);
        assertFalse(exists);
    }
}

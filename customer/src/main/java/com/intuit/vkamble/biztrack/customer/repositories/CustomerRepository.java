package com.intuit.vkamble.biztrack.customer.repositories;


import com.intuit.vkamble.biztrack.customer.models.Customer;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CustomerRepository extends PagingAndSortingRepository<Customer, Long> {
}

package com.intuit.vkamble.biztrack.customer.services;

import com.intuit.vkamble.biztrack.customer.models.Customer;
import com.intuit.vkamble.biztrack.customer.performance.MeasurementPoint;
import com.intuit.vkamble.biztrack.customer.repositories.CustomerRepository;
import com.intuit.vkamble.biztrack.customer.utils.CustomBeanUtils;
import constants.EntityType;
import constants.Pagination;
import exceptions.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Service for Customer operations
 */
@Service
public class CustomerService {

    private static Logger log = LoggerFactory.getLogger(CustomerService.class);

    private CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    /**
     * Method to create customer
     * @param customer
     * @return
     */
    @MeasurementPoint
    public Customer create(Customer customer){
        customer.setAmountDue(new BigDecimal(0));
        customer.setCreateDate(new Date());
        return customerRepository.save(customer);
    }

    /**
     * Method to update customer
     * @param updatedCustomer
     * @param id
     * @return
     */
    @MeasurementPoint
    public Customer update(Customer updatedCustomer, long id){
        log.debug("update with id {}",id);

        if(customerRepository.existsById(id)){

            Customer customer = getById(id);
            CustomBeanUtils.copyProperties(updatedCustomer,customer);
            customer.setLastModifydDate(new Date());
            return customerRepository.save(customer);
        } else{
            log.error("Customer not found. id={}",id);
            throw new EntityNotFoundException(EntityType.CUSTOMER,id);
        }
    }

    /**
     * Method to fetch customer by id
     * @param id
     * @return
     */
    public Customer getById(long id){
        log.debug("getById with id {}",id);

        Optional<Customer> optionalCustomer = customerRepository.findById(id);
        if(!optionalCustomer.isPresent()) {
            log.error("Customer not found. id={}",id);
            throw new EntityNotFoundException(EntityType.CUSTOMER, id);
        }
        return optionalCustomer.get();
    }

    /**
     * Method to check if customer exists
     * @param id
     * @return
     */
    public boolean existsById(long id){
        log.debug("existsById with id {}",id);

        return customerRepository.existsById(id);
    }


    /**
     * Method to fetch all customer
     * @return
     */
    @MeasurementPoint
    public List<Customer> getAll(Integer offset, Integer limit){
        log.debug("getAll with offset={} limit={}",offset,limit);

        int finalOffset = offset!=null?offset: Pagination.OFFSET;
        int finalLimit = limit!=null?limit:Pagination.LIMIT;

        Page<Customer> customerPage =  customerRepository.findAll(PageRequest.of(finalOffset,finalLimit));
        return customerPage.getContent();
    }

    public Customer adjustDueAmount(long id, BigDecimal amount){
        log.debug("adjustDueAmount with id {}",id);

        Optional<Customer> optionalCustomer = customerRepository.findById(id);
        if(!optionalCustomer.isPresent()) {
            log.error("Customer not found. id={}",id);
            throw new EntityNotFoundException(EntityType.CUSTOMER, id);
        }
        Customer customer = optionalCustomer.get();
        customer.setLastModifydDate(new Date());
        customer.setAmountDue(customer.getAmountDue().add(amount));
        return customerRepository.save(customer);
    }
}

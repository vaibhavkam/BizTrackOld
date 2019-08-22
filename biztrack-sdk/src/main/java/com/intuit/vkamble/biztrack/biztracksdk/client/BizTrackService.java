package com.intuit.vkamble.biztrack.biztracksdk.client;



import com.intuit.vkamble.biztrack.biztracksdk.config.EndPoint;

import com.intuit.vkamble.biztrack.biztracksdk.core.Response;
import com.intuit.vkamble.biztrack.biztracksdk.enums.ErrorCode;
import com.intuit.vkamble.biztrack.biztracksdk.enums.ErrorMessage;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import enums.InvoiceStatus;
import models.Customer;
import models.Invoice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;

public class BizTrackService {

    private RestTemplate restTemplate;

    private EndPoint endpoint;

    private static Logger log = LoggerFactory.getLogger(BizTrackService.class);


    public BizTrackService(RestTemplate restTemplate, EndPoint endpoint) {
        this.restTemplate = restTemplate;
        this.endpoint = endpoint;
    }

    @HystrixCommand(fallbackMethod = "defaultIsCustomerExists")
    public Response isCustomerExists(long id) {

        log.info("isCustomerExists with id:{}",id);

        try {
            String customerEndPoint = endpoint.getCustomers() + String.format("/%d", id);
            ParameterizedTypeReference type = new ParameterizedTypeReference<Customer>() {};
            Customer customer = (Customer) restTemplate.exchange(customerEndPoint, HttpMethod.GET, null, type).getBody();

            if (customer != null) {
                return new Response(true);
            }
        }
        catch (HttpClientErrorException.NotFound e) {
            return new Response(false);
        }
        return new Response(false);
    }

    @HystrixCommand(fallbackMethod = "defaultIsInvoiceExists")
    public Response isInvoiceExists(long id) {
        log.info("isInvoiceExists with id:{}",id);
        try {
            String invoiceEndPoint = endpoint.getInvoices() + String.format("/%d", id);
            ParameterizedTypeReference type = new ParameterizedTypeReference<Invoice>() {};
            Invoice invoice = (Invoice) restTemplate.exchange(invoiceEndPoint, HttpMethod.GET, null, type).getBody();

            if (invoice != null) {
                return new Response(true);
            }
        } catch (HttpClientErrorException.NotFound e) { //404 issue
            return new Response(false);
        }

        return new Response(false);
    }

    @HystrixCommand(fallbackMethod = "defaultAdjustDueAmount")
    public Response adjustDueAmount(long id, BigDecimal changeInAmount) {
        log.info("adjustDueAmount with id:{}, changeInAmount:{}",id,changeInAmount);

        try {
            String customerEndPoint = endpoint.getCustomers() + String.format("/%d", id);
            ParameterizedTypeReference type = new ParameterizedTypeReference<Customer>() {};
            Customer customer = (Customer) restTemplate.exchange(customerEndPoint, HttpMethod.GET, null, type).getBody();

            if (customer != null) {
                customer.setLastModifydDate(new Date());
                customer.setAmountDue(customer.getAmountDue().add(changeInAmount));

                HttpEntity<Customer> requestEntity = new HttpEntity<>(customer);
                Customer updatedCustomer = (Customer) restTemplate.exchange(customerEndPoint, HttpMethod.PUT, requestEntity, type).getBody();

                if (updatedCustomer.getAmountDue().subtract(customer.getAmountDue()).longValue() == changeInAmount.longValue()) {

                    return new Response(true);
                }
            }
            else{
                return new Response(ErrorMessage.BUSINESS_ERROR_ENTITY_NOT_FOUND.getMessage(),ErrorCode.BUSINESS_ERROR_ENTITY_NOT_FOUND.getCode(), Collections.singletonList("Customer not found. id:"+id));
            }
        } catch (HttpClientErrorException.NotFound e) {
            return new Response(ErrorMessage.BUSINESS_ERROR_ENTITY_NOT_FOUND.getMessage(),ErrorCode.BUSINESS_ERROR_ENTITY_NOT_FOUND.getCode(), Collections.singletonList("Customer not found. id:"+id));
        }

        return new Response(false);
    }

    @HystrixCommand(fallbackMethod = "defaultUpdateInvoiceStatus")
    // Update to delegate rules check at service side
    public Response updateInvoiceStatus(long id, BigDecimal paidAmount, InvoiceStatus invoiceStatus) {
        log.info("updateInvoiceStatus with id:{}, paidAmount:{}, invoiceStatus:{}",id,paidAmount,invoiceStatus);

        try {
            String invoiceEndPoint = endpoint.getInvoices() + String.format("/%d", id);
            ParameterizedTypeReference type = new ParameterizedTypeReference<Invoice>() {};
            Invoice invoice = (Invoice) restTemplate.exchange(invoiceEndPoint, HttpMethod.GET, null, type).getBody();

            if (invoice != null) {

                if (invoice.getStatus().name().equalsIgnoreCase(InvoiceStatus.PAID.name())) {
                    return new Response(ErrorMessage.BUSINESS_ERROR_ENTITY_NOT_FOUND.getMessage(),ErrorCode.BUSINESS_ERROR_ENTITY_NOT_FOUND.getCode(), Collections.singletonList("Invoice is already paid. id=" + id));
                }

                if (invoice.getAmount().longValue() != paidAmount.longValue()) {
                    return new Response(ErrorMessage.BUSINESS_ERROR_ENTITY_NOT_FOUND.getMessage(),ErrorCode.BUSINESS_ERROR_ENTITY_NOT_FOUND.getCode(), Collections.singletonList("Invoice need to be fully paid. Payment can not be completed. id=" + id));
                }
                invoice.setLastModifydDate(new Date());
                invoice.setStatus(invoiceStatus);

                HttpEntity<Invoice> requestEntity = new HttpEntity<>(invoice);
                Invoice updatedInvoice = (Invoice) restTemplate.exchange(invoiceEndPoint, HttpMethod.PUT, requestEntity, type).getBody();

                if (updatedInvoice.getStatus().name().equalsIgnoreCase(InvoiceStatus.PAID.name())) {
                    return new Response(true);
                }
            } else{
                return new Response(ErrorMessage.BUSINESS_ERROR_ENTITY_NOT_FOUND.getMessage(),ErrorCode.BUSINESS_ERROR_ENTITY_NOT_FOUND.getCode(), Collections.singletonList("Invoice not found. id:"+id));
            }
        } catch (HttpClientErrorException.NotFound e) { //404 issue
            return new Response(ErrorMessage.BUSINESS_ERROR_ENTITY_NOT_FOUND.getMessage(),ErrorCode.BUSINESS_ERROR_ENTITY_NOT_FOUND.getCode(), Collections.singletonList("Invoice not found. id:"+id));
        }

        return new Response(false);
    }


    private Response defaultIsCustomerExists(long id) {
        log.info("defaultIsCustomerExists with id:{}",id);
        return new Response(ErrorMessage.SERVICE_UNAVAILABLE.getMessage(),ErrorCode.SERVICE_UNAVAILABLE.getCode(),null);
    }

    private Response defaultIsInvoiceExists(long id) {
        log.info("defaultIsInvoiceExists with id:{}",id);
        return new Response(ErrorMessage.SERVICE_UNAVAILABLE.getMessage(),ErrorCode.SERVICE_UNAVAILABLE.getCode(),null);
    }

    private Response defaultAdjustDueAmount(long id, BigDecimal changeInAmount) {
        log.info("defaultAdjustDueAmount with id:{}, changeInAmount:{}",id,changeInAmount);
        return new Response(false);
    }

    private Response defaultUpdateInvoiceStatus(long id, BigDecimal paidAmount, InvoiceStatus invoiceStatus) {
        log.info("defaultUpdateInvoiceStatus with id:{}, paidAmount:{}, invoiceStatus:{}",id,paidAmount,invoiceStatus);
        return new Response(false);
    }
}

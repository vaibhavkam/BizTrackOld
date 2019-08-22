package com.intuit.vkamble.biztrack.invoicing.services;

import com.intuit.vkamble.biztrack.biztracksdk.client.BizTrackService;

import com.intuit.vkamble.biztrack.biztracksdk.core.Response;
import com.intuit.vkamble.biztrack.invoicing.models.Invoice;
import com.intuit.vkamble.biztrack.invoicing.repositories.InvoiceRepository;
import enums.InvoiceStatus;
import exceptions.EntityNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class InvoiceServiceTest {

    private InvoiceRepository invoiceRepository = Mockito.mock(InvoiceRepository.class);
    
    private BizTrackService bizTrackService = Mockito.mock(BizTrackService.class);

    private InvoiceService invoiceService;

    @Before
    public void init(){
        invoiceService = new InvoiceService(invoiceRepository,bizTrackService);
    }

    @Test
    public void createInvoiceTestForExistingCustomer(){

        Invoice invoice = new Invoice();
        invoice.setAmount(new BigDecimal(10));
        invoice.setCustomerId(1L);
        invoice.setStatus(InvoiceStatus.OPEN);

        when(bizTrackService.isCustomerExists(1L)).thenReturn(any(Response.class));
        when(invoiceRepository.save(any(Invoice.class))).then(returnsFirstArg());

        Invoice invoiceCreated = invoiceService.create(invoice);
        assertEquals(invoice, invoiceCreated);

    }

    @Test(expected = EntityNotFoundException.class)
    public void createInvoiceTestForNonExistingCustomer(){

        Invoice invoice = new Invoice();
        invoice.setAmount(new BigDecimal(10));
        invoice.setCustomerId(1L);
        invoice.setStatus(InvoiceStatus.OPEN);

        when(bizTrackService.isCustomerExists(1L)).thenReturn(any(Response.class));

        invoiceService.create(invoice);
    }

    @Test
    public void updateInvoiceTestForExistingCustomer(){

        Invoice invoice = new Invoice();
        invoice.setAmount(new BigDecimal(10));
        invoice.setCustomerId(1L);
        invoice.setStatus(InvoiceStatus.OPEN);

        when(bizTrackService.isCustomerExists(1L)).thenReturn(any(Response.class));
        when(invoiceRepository.existsById(1L)).thenReturn(true);
        when(invoiceRepository.findById(1L)).thenReturn(Optional.of(invoice));
        when(invoiceRepository.save(any(Invoice.class))).then(returnsFirstArg());

        Invoice invoiceUpdated = invoiceService.update(invoice,1);
        assertEquals(invoice, invoiceUpdated);
    }

    @Test
    public void getExitingInvoiceByIdTest(){

        Invoice invoice = new Invoice();
        invoice.setAmount(new BigDecimal(10));
        invoice.setCustomerId(1L);
        invoice.setStatus(InvoiceStatus.OPEN);

        when(invoiceRepository.findById(1L)).thenReturn(Optional.of(invoice));
        Invoice invoiceFetched = invoiceService.getById(1L);
        assertNotNull(invoiceFetched);
        assertEquals(invoice, invoiceFetched);
    }

    @Test(expected = EntityNotFoundException.class)
    public void getNonExitingInvoiceByIdTest(){

        when(invoiceRepository.findById(1L)).thenReturn(Optional.empty());
        invoiceService.getById(1L);
    }
}

package com.intuit.vkamble.biztrack.invoicing.controllers;

import com.intuit.vkamble.biztrack.invoicing.models.Invoice;
import com.intuit.vkamble.biztrack.invoicing.services.InvoiceService;
import enums.InvoiceStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/v1/invoices")
public class InvoiceController {

    private InvoiceService invoiceService;

    @Autowired
    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @PostMapping
    public Invoice create(@Valid @RequestBody Invoice invoice){
        return invoiceService.create(invoice);
    }

    @PutMapping(value = "/{id}")
    public Invoice update(@Valid @RequestBody Invoice invoice, @PathVariable long id){
        return invoiceService.update(invoice, id);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable long id){
        invoiceService.deleteById(id);
    }

    @GetMapping(value = "/{id}")
    public Invoice getById(@PathVariable long id){
        return invoiceService.getById(id);
    }

    @GetMapping
    public List<Invoice> getAll(@RequestParam(required = false) Long customerId, @RequestParam(required = false) InvoiceStatus invoiceStatus, @RequestParam(required = false) Integer offset, @RequestParam(required = false) Integer limit){
        return invoiceService.getAll(customerId, invoiceStatus, offset, limit);
    }

    @GetMapping(value = "/stats")
    public BigDecimal getStat(@RequestParam String stat, @RequestParam(required = false) Long customerId){
        return invoiceService.getStat(stat, customerId);
    }

}

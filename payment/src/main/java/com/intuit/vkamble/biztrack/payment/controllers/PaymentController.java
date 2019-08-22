package com.intuit.vkamble.biztrack.payment.controllers;

import com.intuit.vkamble.biztrack.payment.models.Payment;
import com.intuit.vkamble.biztrack.payment.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/v1/payments")
public class PaymentController {

    private PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public Payment create(@Valid @RequestBody Payment payment){
        return paymentService.create(payment);
    }

    @PutMapping(value = "/{id}")
    public Payment update(@Valid @RequestBody Payment payment, @PathVariable long id){
        return paymentService.update(payment, id);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable long id){
        paymentService.deleteById(id);
    }

    @GetMapping(value = "/{id}")
    public Payment getById(@PathVariable long id){
        return paymentService.getById(id);
    }

    @GetMapping
    public List<Payment> getAll(@RequestParam(required = false) Long customerId, @RequestParam(required = false) Long invoiceId, @RequestParam(required = false) Integer offset, @RequestParam(required = false) Integer limit){
        return paymentService.getAll(customerId,invoiceId,offset,limit);
    }

    @GetMapping(value = "/stats")
    public BigDecimal getStat(@RequestParam String stat, @RequestParam(required = false) Long customerId){
        return paymentService.getStat(stat, customerId);
    }
}

package com.intuit.vkamble.biztrack.payment.services;

import com.intuit.vkamble.biztrack.biztracksdk.client.BizTrackService;
import com.intuit.vkamble.biztrack.biztracksdk.core.Response;
import com.intuit.vkamble.biztrack.payment.messaging.KafkaClient;
import com.intuit.vkamble.biztrack.payment.models.Payment;
import com.intuit.vkamble.biztrack.payment.repositories.PaymentRepository;
import exceptions.EntityNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class PaymentServiceTest {

    private PaymentRepository paymentRepository = Mockito.mock(PaymentRepository.class);

    private BizTrackService bizTrackService = Mockito.mock(BizTrackService.class);

    private KafkaClient kafkaClient = Mockito.mock(KafkaClient.class);

    private PaymentService paymentService;



    @Before
    public void init(){
        paymentService = new PaymentService(paymentRepository,bizTrackService,kafkaClient);
    }

    @Test
    public void createPaymentTestForExistingCustomer(){

        Payment payment = new Payment();
        payment.setAmount(new BigDecimal(10));
        payment.setCustomerId(1L);
        payment.setInvoiceId(1L);

        when(bizTrackService.isCustomerExists(1L)).thenReturn(any(Response.class));
        when(bizTrackService.isInvoiceExists(1L)).thenReturn(any(Response.class));
        when(paymentRepository.save(any(Payment.class))).then(returnsFirstArg());

        Payment paymentCreated = paymentService.create(payment);
        assertEquals(payment, paymentCreated);

    }

    @Test(expected = EntityNotFoundException.class)
    public void createPaymentTestForNonExistingCustomer(){

        Payment payment = new Payment();
        payment.setAmount(new BigDecimal(10));
        payment.setCustomerId(1L);
        payment.setInvoiceId(1L);

        when(bizTrackService.isCustomerExists(1L)).thenReturn(any(Response.class));

        paymentService.create(payment);
    }

    @Test
    public void updatePaymentTestForExistingCustomer(){

        Payment payment = new Payment();
        payment.setAmount(new BigDecimal(10));
        payment.setCustomerId(1L);
        payment.setInvoiceId(1L);

        when(bizTrackService.isCustomerExists(1L)).thenReturn(any(Response.class));
        when(paymentRepository.existsById(1L)).thenReturn(true);
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));
        when(paymentRepository.save(any(Payment.class))).then(returnsFirstArg());
        when(bizTrackService.isInvoiceExists(1L)).thenReturn(any(Response.class));

        Payment paymentUpdated = paymentService.update(payment,1);
        assertEquals(payment, paymentUpdated);
    }

    @Test
    public void getExitingPaymentByIdTest(){

        Payment payment = new Payment();
        payment.setAmount(new BigDecimal(10));
        payment.setCustomerId(1L);
        payment.setInvoiceId(1L);

        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));
        Payment paymentFetched = paymentService.getById(1L);
        assertNotNull(paymentFetched);
        assertEquals(payment, paymentFetched);
    }

    @Test(expected = EntityNotFoundException.class)
    public void getNonExitingPaymentByIdTest(){

        when(paymentRepository.findById(1L)).thenReturn(Optional.empty());
        paymentService.getById(1L);
    }
}

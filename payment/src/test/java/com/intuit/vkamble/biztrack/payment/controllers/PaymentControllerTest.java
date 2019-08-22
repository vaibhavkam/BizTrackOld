package com.intuit.vkamble.biztrack.payment.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.intuit.vkamble.biztrack.payment.models.Payment;
import com.intuit.vkamble.biztrack.payment.services.PaymentService;
import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentService paymentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void createTest() throws Exception {

        Payment payment = new Payment();
        payment.setId(1L);
        payment.setInvoiceId(1L);
        payment.setCustomerId(1L);
        payment.setAmount(new BigDecimal(10));
        when(paymentService.create(any(Payment.class))).thenReturn(payment);
        this.mockMvc.perform(post("/v1/payments").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(payment))).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(CoreMatchers.containsString("{\"id\":1,\"invoiceId\":1,\"customerId\":1,\"amount\":10}")));
    }

    @Test
    public void updateTest() throws Exception {

        Payment payment = new Payment();
        payment.setId(1L);
        payment.setInvoiceId(1L);
        payment.setCustomerId(1L);
        payment.setAmount(new BigDecimal(10));
        when(paymentService.update(any(Payment.class),anyLong())).thenReturn(payment);
        this.mockMvc.perform(put("/v1/payments/1").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(payment))).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(CoreMatchers.containsString("{\"id\":1,\"invoiceId\":1,\"customerId\":1,\"amount\":10}")));
    }

    @Test
    public void getByIdTest() throws Exception {

        Payment payment = new Payment();
        payment.setId(1L);
        payment.setInvoiceId(1L);
        payment.setCustomerId(1L);
        payment.setAmount(new BigDecimal(10));
        when(paymentService.getById(anyLong())).thenReturn(payment);
        this.mockMvc.perform(get("/v1/payments/1")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(CoreMatchers.containsString("{\"id\":1,\"invoiceId\":1,\"customerId\":1,\"amount\":10}")));
    }

    @Test
    public void deleteByIdTest() throws Exception {

        this.mockMvc.perform(delete("/v1/payments/1")).andDo(print()).andExpect(status().isNoContent());
    }

    @Test
    public void getAllTest() throws Exception {

        Payment payment = new Payment();
        payment.setId(1L);
        payment.setInvoiceId(1L);
        payment.setCustomerId(1L);
        payment.setAmount(new BigDecimal(10));
        when(paymentService.getAll(null,null,null,null)).thenReturn(Collections.singletonList(payment));
        this.mockMvc.perform(get("/v1/payments")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(CoreMatchers.containsString("[{\"id\":1,\"invoiceId\":1,\"customerId\":1,\"amount\":10}]")));
    }
}

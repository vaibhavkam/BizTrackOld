package com.intuit.vkamble.biztrack.invoicing.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.intuit.vkamble.biztrack.invoicing.models.Invoice;
import com.intuit.vkamble.biztrack.invoicing.services.InvoiceService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class InvoiceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InvoiceService invoiceService;


    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void createTest() throws Exception {

        Invoice invoice = new Invoice();
        invoice.setId(1L);
        invoice.setCustomerId(1L);
        invoice.setAmount(new BigDecimal(10));
        when(invoiceService.create(any(Invoice.class))).thenReturn(invoice);
        this.mockMvc.perform(post("/v1/invoices").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(invoice))).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(CoreMatchers.containsString("{\"id\":1,\"customerId\":1,\"amount\":10}")));
    }

    @Test
    public void updateTest() throws Exception {

        Invoice invoice = new Invoice();
        invoice.setId(1L);
        invoice.setCustomerId(1L);
        invoice.setAmount(new BigDecimal(10));
        when(invoiceService.update(any(Invoice.class),anyLong())).thenReturn(invoice);
        this.mockMvc.perform(put("/v1/invoices/1").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(invoice))).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(CoreMatchers.containsString("{\"id\":1,\"customerId\":1,\"amount\":10}")));
    }

    @Test
    public void getByIdTest() throws Exception {

        Invoice invoice = new Invoice();
        invoice.setId(1L);
        invoice.setCustomerId(1L);
        invoice.setAmount(new BigDecimal(10));
        when(invoiceService.getById(anyLong())).thenReturn(invoice);
        this.mockMvc.perform(get("/v1/invoices/1")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(CoreMatchers.containsString("{\"id\":1,\"customerId\":1,\"amount\":10}")));
    }

    @Test
    public void deleteByIdTest() throws Exception {

        this.mockMvc.perform(delete("/v1/invoices/1")).andDo(print()).andExpect(status().isNoContent());
    }

    @Test
    public void getAllTestNew() throws Exception {

        Invoice invoice = new Invoice();
        invoice.setId(1L);
        invoice.setCustomerId(1L);
        invoice.setAmount(new BigDecimal(10));
        when(invoiceService.getAll(any(),any(),any(),any())).thenReturn(Collections.singletonList(invoice));
        this.mockMvc.perform(get("/v1/invoices")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(CoreMatchers.containsString("[{\"id\":1,\"customerId\":1,\"amount\":10}]")));
    }
}

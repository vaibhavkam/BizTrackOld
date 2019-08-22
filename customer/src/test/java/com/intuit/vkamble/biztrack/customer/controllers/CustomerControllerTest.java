package com.intuit.vkamble.biztrack.customer.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.intuit.vkamble.biztrack.customer.models.Customer;
import com.intuit.vkamble.biztrack.customer.services.CustomerService;
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
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void createTest() throws Exception {

        Customer customer = new Customer();
        customer.setId(1L);
        customer.setFirstName("Vaibhav");
        customer.setLastName("Kamble");
        customer.setEmailId("test@test.com");
        when(customerService.create(any(Customer.class))).thenReturn(customer);
        this.mockMvc.perform(post("/v1/customers").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(customer))).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(CoreMatchers.containsString("{\"id\":1,\"firstName\":\"Vaibhav\",\"lastName\":\"Kamble\",\"emailId\":\"test@test.com\"}")));
    }

    @Test
    public void updateTest() throws Exception {

        Customer customer = new Customer();
        customer.setId(1L);
        customer.setFirstName("Vaibhav");
        customer.setLastName("Kamble");
        customer.setEmailId("test@test.com");
        when(customerService.update(any(Customer.class),anyLong())).thenReturn(customer);
        this.mockMvc.perform(put("/v1/customers/1").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(customer))).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(CoreMatchers.containsString("{\"id\":1,\"firstName\":\"Vaibhav\",\"lastName\":\"Kamble\",\"emailId\":\"test@test.com\"}")));
    }

    @Test
    public void getByIdTest() throws Exception {

        Customer customer = new Customer();
        customer.setId(1L);
        customer.setFirstName("Vaibhav");
        customer.setLastName("Kamble");
        customer.setEmailId("test@test.com");
        when(customerService.getById(anyLong())).thenReturn(customer);
        this.mockMvc.perform(get("/v1/customers/1")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(CoreMatchers.containsString("{\"id\":1,\"firstName\":\"Vaibhav\",\"lastName\":\"Kamble\",\"emailId\":\"test@test.com\"}")));
    }

    @Test
    public void getAllTest() throws Exception {

        Customer customer = new Customer();
        customer.setId(1L);
        customer.setFirstName("Vaibhav");
        customer.setLastName("Kamble");
        customer.setEmailId("test@test.com");
        when(customerService.getAll(any(),any())).thenReturn(Collections.singletonList(customer));
        this.mockMvc.perform(get("/v1/customers")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(CoreMatchers.containsString("[{\"id\":1,\"firstName\":\"Vaibhav\",\"lastName\":\"Kamble\",\"emailId\":\"test@test.com\"}]")));
    }
}

package com.intuit.vkamble.biztrack.biztracksdk.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="biztrack.endpoint")
//EndPointConfig. Try builders
public class EndPoint {

    //naming conventions
    private String customers;

    private String invoices;

    private String payments;

    public String getCustomers() {
        return customers;
    }

    public void setCustomers(String customers) {
        this.customers = customers;
    }

    public String getInvoices() {
        return invoices;
    }

    public void setInvoices(String invoices) {
        this.invoices = invoices;
    }

    public String getPayments() {
        return payments;
    }

    public void setPayments(String payments) {
        this.payments = payments;
    }
}

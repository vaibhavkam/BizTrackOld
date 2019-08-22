package com.intuit.vkamble.biztrack.payment.models;

import enums.PaymentType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Entity
public class Payment {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Please provide a invoice id")
    private Long invoiceId;

    @NotNull(message = "Please provide a customer id")
    private Long customerId;

    @NotNull(message = "Please provide amount")
    private BigDecimal amount;

    @Enumerated(EnumType.ORDINAL)
    private PaymentType paymentType;

    private Date createDate;
    private Date lastModifydDate;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Long invoiceId) {
        this.invoiceId = invoiceId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getLastModifydDate() {
        return lastModifydDate;
    }

    public void setLastModifydDate(Date lastModifydDate) {
        this.lastModifydDate = lastModifydDate;
    }
}

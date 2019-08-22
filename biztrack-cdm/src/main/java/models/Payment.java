package models;


import enums.PaymentType;

import java.math.BigDecimal;
import java.util.Date;

public class Payment {


    private Long id;

    private Long invoiceId;

    private Long customerId;

    private BigDecimal amount;

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

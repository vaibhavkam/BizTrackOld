package com.intuit.vkamble.biztrack.invoicing.models;


import enums.InvoiceStatus;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class Invoice {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Please provide a customer id")
    private Long customerId;

    @NotNull(message = "Please provide a amount")
    private BigDecimal amount;

    private String description;

    @Enumerated(EnumType.ORDINAL)
    private InvoiceStatus status;

    private Date dueDate;
    private Date createDate;
    private Date lastModifydDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public InvoiceStatus getStatus() {
        return status;
    }

    public void setStatus(InvoiceStatus status) {
        this.status = status;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
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

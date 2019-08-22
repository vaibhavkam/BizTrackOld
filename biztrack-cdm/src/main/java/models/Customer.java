package models;

import java.math.BigDecimal;
import java.util.Date;

public class Customer {

    private Long id;

    private String firstName;

    private String lastName;

    private String emailId;

    private BigDecimal amountDue;

    private Date createDate;

    private Date lastModifydDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public BigDecimal getAmountDue() {
        return amountDue;
    }

    public void setAmountDue(BigDecimal amountDue) {
        this.amountDue = amountDue;
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

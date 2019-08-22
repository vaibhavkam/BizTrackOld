package com.intuit.vkamble.biztrack.invoicing.repositories;

import com.intuit.vkamble.biztrack.invoicing.models.Invoice;
import enums.InvoiceStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface InvoiceRepository extends PagingAndSortingRepository<Invoice, Long> {

    List<Invoice> findByCustomerId(long customerId, Pageable pageable);

    List<Invoice> findByStatus(InvoiceStatus invoiceStatus, Pageable pageable);

    List<Invoice> findByCustomerIdAndStatus(long customerId, InvoiceStatus invoiceStatus, Pageable pageable);

    @Query("SELECT SUM(amount) from Invoice where customerId= :customerId")
    public BigDecimal findTotalAmountExpectedByCustomerId(@Param("customerId") long customerId);

    @Query("SELECT SUM(amount) from Invoice")
    public BigDecimal findTotalAmountExpected();



}

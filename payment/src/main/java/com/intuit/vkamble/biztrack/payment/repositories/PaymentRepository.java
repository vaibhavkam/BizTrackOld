package com.intuit.vkamble.biztrack.payment.repositories;

import com.intuit.vkamble.biztrack.payment.models.Payment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface PaymentRepository extends PagingAndSortingRepository<Payment, Long> {

    List<Payment> findByCustomerId(long customerId, Pageable pageable);

    List<Payment> findByInvoiceId(long invoiceId, Pageable pageable);

    @Query("SELECT SUM(amount) from Payment where customerId= :customerId")
    public BigDecimal findTotalAmountPaidByCustomerId(@Param("customerId") long customerId);

    @Query("SELECT SUM(amount) from Payment")
    public BigDecimal findTotalAmountPaid();

}

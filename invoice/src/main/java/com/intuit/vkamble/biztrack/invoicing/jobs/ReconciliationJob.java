package com.intuit.vkamble.biztrack.invoicing.jobs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ReconciliationJob {

    private static Logger log = LoggerFactory.getLogger(ReconciliationJob.class);

    @Scheduled(fixedRate = 300000)
    public void reconcileInvoiceAndPayment(){

        log.info("Still reconciling - " + System.currentTimeMillis() / 1000);
    }
}


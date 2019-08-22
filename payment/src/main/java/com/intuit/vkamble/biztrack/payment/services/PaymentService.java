package com.intuit.vkamble.biztrack.payment.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intuit.vkamble.biztrack.biztracksdk.client.BizTrackService;

import com.intuit.vkamble.biztrack.biztracksdk.core.Response;
import com.intuit.vkamble.biztrack.payment.messaging.KafkaClient;
import com.intuit.vkamble.biztrack.payment.messaging.KafkaConstants;
import com.intuit.vkamble.biztrack.payment.models.Payment;
import com.intuit.vkamble.biztrack.payment.performance.MeasurementPoint;
import com.intuit.vkamble.biztrack.payment.repositories.PaymentRepository;
import com.intuit.vkamble.biztrack.payment.utils.BiztrackSDKExceptionMapper;
import com.intuit.vkamble.biztrack.payment.utils.CustomBeanUtils;
import constants.EntityType;
import constants.Pagination;
import constants.Stats;
import enums.InvoiceStatus;
import exceptions.EntityNotFoundException;
import exceptions.InternalException;
import exceptions.InvalidOperationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Service for payment operations
 */
@Service// Same as @Component but Spring may add special functionalities for @Service in future
public class PaymentService {
    private static Logger log = LoggerFactory.getLogger(PaymentService.class);
    
    private BizTrackService bizTrackService;

    private PaymentRepository paymentRepository;

    private KafkaClient kafkaClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired //Imp - Constructor DI for mandatory dependency and setter DI for optional. Don't use field level DI
    public PaymentService(PaymentRepository paymentRepository, BizTrackService bizTrackService,KafkaClient kafkaClient) {
        this.paymentRepository = paymentRepository;
        this.bizTrackService = bizTrackService;
        this.kafkaClient = kafkaClient;
    }

    /**
     * Method to create new payment
     * @param payment
     * @return
     */
    @MeasurementPoint
    public Payment create(Payment payment){
        try {
            Response response = bizTrackService.isCustomerExists(payment.getCustomerId());
            if (response.getData() != null) {
                if ((Boolean) response.getData()) {
                    Response invoiceResponse = bizTrackService.isInvoiceExists(payment.getInvoiceId());
                    if (invoiceResponse.getData() != null) {
                        if ((Boolean) invoiceResponse.getData()) {
                            payment.setCreateDate(new Date());
                            bizTrackService.updateInvoiceStatus(payment.getInvoiceId(), payment.getAmount(), InvoiceStatus.PAID);
                            bizTrackService.adjustDueAmount(payment.getCustomerId(), payment.getAmount().negate());
                            payment = paymentRepository.save(payment);
                            kafkaClient.sendMessage(KafkaConstants.PAYMENT_TOPIC, objectMapper.writeValueAsString(payment));
                            return payment;
                        } else {
                            log.error("Invoice not found. id={}", payment.getInvoiceId());
                            throw new EntityNotFoundException(EntityType.INVOICE, payment.getInvoiceId());
                        }
                    } else {
                        throw BiztrackSDKExceptionMapper.mapSDKException(response.getError());
                    }
                } else {
                    log.error("Customer not found. id={}", payment.getCustomerId());
                    throw new EntityNotFoundException(EntityType.CUSTOMER, payment.getCustomerId());
                }
            } else {
                throw BiztrackSDKExceptionMapper.mapSDKException(response.getError());

            }
        }catch (JsonProcessingException e){
            throw new InternalException(EntityType.PAYMENT);
        }
    }

    /**
     * Method to update payment
     * @param updatedPayment
     * @param id
     * @return
     */
    public Payment update(Payment updatedPayment, long id){
        log.debug("update with id {}",id);

        Response response = bizTrackService.isCustomerExists(updatedPayment.getCustomerId());
        if(response.getData()!=null) {
            if ((Boolean) response.getData()) {

                Response invoiceResponse = bizTrackService.isInvoiceExists(updatedPayment.getInvoiceId());
                if(invoiceResponse.getData()!=null) {
                    if ((Boolean) invoiceResponse.getData()) {

                        if (paymentRepository.existsById(id)) {
                            Payment payment = getById(id);
                            if (payment.getAmount() != updatedPayment.getAmount()) {
                                log.error("Payment amount update is not allowed. id={}", id);
                                throw new InvalidOperationException("Payment amount update is not allowed. id=" + id);
                            }
                            CustomBeanUtils.copyProperties(updatedPayment, payment);
                            payment.setLastModifydDate(new Date());
                            return paymentRepository.save(payment);
                        } else {
                            log.error("Payment not found. id={}", id);
                            throw new EntityNotFoundException(EntityType.PAYMENT, updatedPayment.getCustomerId());
                        }
                    } else {
                        log.error("Invoice not found. id={}", updatedPayment.getInvoiceId());
                        throw new EntityNotFoundException(EntityType.INVOICE, updatedPayment.getInvoiceId());
                    }
                } else {
                    throw BiztrackSDKExceptionMapper.mapSDKException(response.getError());
                }
            } else {
                log.error("Customer not found. id={}", updatedPayment.getCustomerId());
                throw new EntityNotFoundException(EntityType.CUSTOMER, updatedPayment.getCustomerId());
            }
        } else {
            throw BiztrackSDKExceptionMapper.mapSDKException(response.getError());

        }
    }

    /**
     * Method to delete payment by id
     * @param id
     */
    public void deleteById(long id){
        log.debug("deleteById with id {}",id);

        Optional<Payment> optionalPayment = paymentRepository.findById(id);
        if(!optionalPayment.isPresent()) {
            log.error("Payment not found. id={}",id);
            throw new EntityNotFoundException(EntityType.PAYMENT, id);
        }
        Payment payment = optionalPayment.get();
        bizTrackService.updateInvoiceStatus(payment.getInvoiceId(),payment.getAmount(),InvoiceStatus.OPEN);
        bizTrackService.adjustDueAmount(payment.getCustomerId(),payment.getAmount());
        paymentRepository.deleteById(id);

    }

    /**
     * Method to fetch payment by id
     * @param id
     * @return
     */
    public Payment getById(long id){
        log.debug("getById with id {}",id);

        Optional<Payment> optionalPayment = paymentRepository.findById(id);
        if(!optionalPayment.isPresent()) {
            log.error("Payment not found. id={}",id);
            throw new EntityNotFoundException(EntityType.PAYMENT, id);
        }
        return optionalPayment.get();
    }

    /**
     * Method to check if payment exists or not
     * @param id
     * @return
     */
    public boolean existsById(long id){
        log.debug("existsById with id {}",id);

        return paymentRepository.existsById(id);
    }

    /**
     * Method to get payments by customerId
     * @param customerId
     * @return
     */
    public List<Payment> getByCustomerId(long customerId, int offset,int limit){
        log.debug("getByCustomerId with customerId={},offset={},limit={}",customerId, offset, limit);

        Response response = bizTrackService.isCustomerExists(customerId);
        if(response.getData()!=null){
            if((Boolean)response.getData()){
                return paymentRepository.findByCustomerId(customerId,PageRequest.of(offset,limit));
            }
            else{
                log.error("Customer not found. id={}",customerId);
                throw new EntityNotFoundException(EntityType.CUSTOMER,customerId);
            }
        }else{
            throw BiztrackSDKExceptionMapper.mapSDKException(response.getError());
        }
    }

    /**
     * Method to get payments by invoiceId
     * @param invoiceId
     * @return
     */
    public List<Payment> getByInvoiceId(long invoiceId,int offset,int limit){
        log.debug("getByInvoiceId with invoiceId={},offset={},limit={}",invoiceId, offset, limit);

        return paymentRepository.findByInvoiceId(invoiceId, PageRequest.of(offset,limit));
    }

    /**
     * Method to get all payments
     * @return
     */
    @MeasurementPoint
    public List<Payment> getAll(Long customerId, Long invoiceId, Integer offset, Integer limit){
        log.debug("getAll with customerId={}, invoiceId={},offset={},limit={}",customerId, invoiceId, offset, limit);

        int finalOffset = offset!=null?offset: Pagination.OFFSET;
        int finalLimit = limit!=null?limit:Pagination.LIMIT;

        if(invoiceId!=null){
            return getByInvoiceId(invoiceId,finalOffset,finalLimit);
        } else if(customerId!=null){
            return getByCustomerId(customerId,finalOffset,finalLimit);
        }

        Page<Payment> paymentPage =  paymentRepository.findAll(PageRequest.of(finalOffset,finalLimit));
        return paymentPage.getContent();
    }

    /**
     * Method to fetch total paid amount by customer
     * @return
     */
    public BigDecimal getTotalAmountPaidByCustomer(long customerId){
        log.debug("getTotalAmountPaidByCustomer with id {}",customerId);

        Response response = bizTrackService.isCustomerExists(customerId);
        if(response.getData()!=null) {
            if ((Boolean) response.getData()) {
                return paymentRepository.findTotalAmountPaidByCustomerId(customerId);
            } else {
                log.error("Customer not found. id={}", customerId);
                throw new EntityNotFoundException(EntityType.CUSTOMER, customerId);
            }
        } else{
            throw BiztrackSDKExceptionMapper.mapSDKException(response.getError());
        }
    }

    /**
     * Method to fetch total paid amount
     * @return
     */
    public BigDecimal getTotalAmountPaid(){
        return paymentRepository.findTotalAmountPaid();
    }

    public BigDecimal getStat(String stat, Long customerId){

        if(stat.equalsIgnoreCase(Stats.PAID_AMOUNT)){

            if(customerId!=null){
                return getTotalAmountPaidByCustomer(customerId);
            } else {
                return getTotalAmountPaid();
            }
        } else {
            throw new InvalidOperationException("Input stat is not supported. Stat:"+stat);
        }
    }

}

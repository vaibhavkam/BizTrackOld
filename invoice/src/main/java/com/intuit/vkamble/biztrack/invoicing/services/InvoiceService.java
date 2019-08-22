package com.intuit.vkamble.biztrack.invoicing.services;

import com.intuit.vkamble.biztrack.biztracksdk.client.BizTrackService;

import com.intuit.vkamble.biztrack.biztracksdk.core.Response;
import com.intuit.vkamble.biztrack.invoicing.models.Invoice;

import com.intuit.vkamble.biztrack.invoicing.performance.MeasurementPoint;
import com.intuit.vkamble.biztrack.invoicing.repositories.InvoiceRepository;
import com.intuit.vkamble.biztrack.invoicing.utils.BiztrackSDKExceptionMapper;
import com.intuit.vkamble.biztrack.invoicing.utils.CustomBeanUtils;
import constants.EntityType;
import constants.Pagination;
import constants.Stats;
import enums.InvoiceStatus;
import exceptions.EntityNotFoundException;
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
 * Service for Invoice operations
 */
@Service// Same as @Component but Spring may add special functionalities for @Service in future
public class InvoiceService {

    private static Logger log = LoggerFactory.getLogger(InvoiceService.class);

    private InvoiceRepository invoiceRepository;

    private BizTrackService bizTrackService;

    @Autowired //Imp - Constructor DI for mandatory dependency and setter DI for optional. Don't use field level DI
    public InvoiceService(InvoiceRepository invoiceRepository, BizTrackService bizTrackService) {
        this.invoiceRepository = invoiceRepository;
        this.bizTrackService=bizTrackService;
    }

    /**
     * Method to create new invoice
     * @param invoice
     * @return Invoice
     */
    @MeasurementPoint
    public Invoice create(Invoice invoice){

        Response response = bizTrackService.isCustomerExists(invoice.getCustomerId());
        if(response.getData()!=null) {
            if ((Boolean) response.getData()) {

                invoice.setStatus(InvoiceStatus.OPEN);
                invoice.setCreateDate(new Date());
                Invoice newInvoice = invoiceRepository.save(invoice);
                bizTrackService.adjustDueAmount(invoice.getCustomerId(), invoice.getAmount());
                return newInvoice;
            } else {

                log.error("Customer not found. id={}", invoice.getCustomerId());
                throw new EntityNotFoundException(EntityType.CUSTOMER,invoice.getCustomerId());
            }
        }else {
            throw BiztrackSDKExceptionMapper.mapSDKException(response.getError());

        }
    }

    /**
     * Method to update invoice
     * @param updatedInvoice
     * @param id
     * @return
     */
    public Invoice update(Invoice updatedInvoice, long id){
        log.debug("update for id={}",id);

        Response response = bizTrackService.isCustomerExists(updatedInvoice.getCustomerId());
        if(response.getData()!=null) {
            if ((Boolean) response.getData()) {

                if (invoiceRepository.existsById(id)) {
                    Invoice invoice = getById(id);
                    if (invoice.getAmount().longValue() != updatedInvoice.getAmount().longValue() && invoice.getStatus().equals(InvoiceStatus.PAID)) {
                        log.error("Invoice amount update is not allowed, if invoice is paid. id={}", id);
                        throw new InvalidOperationException("Invoice amount update is not allowed, if invoice is paid. id=" + id);
                    }
                    BigDecimal changeInDueAmount = updatedInvoice.getAmount().subtract(invoice.getAmount());
                    CustomBeanUtils.copyProperties(updatedInvoice, invoice);
                    invoice.setLastModifydDate(new Date());
                    bizTrackService.adjustDueAmount(invoice.getCustomerId(), changeInDueAmount);
                    return invoiceRepository.save(invoice);
                } else {
                    log.error("Invoice not found. id={}", id);
                    throw new EntityNotFoundException(EntityType.INVOICE, id);
                }
            } else {
                log.error("Customer not found. id={}", id);
                throw new EntityNotFoundException(EntityType.CUSTOMER,id);
            }
        }else{
            throw BiztrackSDKExceptionMapper.mapSDKException(response.getError());
        }
    }


    /**
     * Method to delete invoice by id
     * @param id
     */
    public void deleteById(long id){
        log.debug("deleteById with id={}",id);

        Optional<Invoice> optionalInvoice = invoiceRepository.findById(id);
        if(!optionalInvoice.isPresent()) {
            log.error("Invoice not found. id={}",id);
            throw new EntityNotFoundException(EntityType.INVOICE, id);
        }

        Invoice invoice = optionalInvoice.get();
        if(invoice.getStatus().equals(InvoiceStatus.PAID)){
            log.error("Invoice is paid. Can not delete Invoice. id={}",id);
            throw new InvalidOperationException("Can not delete Invoice. Invoice is paid. Delete payment first");
        }

        invoiceRepository.deleteById(id);
        bizTrackService.adjustDueAmount(invoice.getCustomerId(),invoice.getAmount().negate());
    }

    /**
     * Method to fetch invoice by id
     * @param id
     * @return
     */
    public Invoice getById(long id){
        log.debug("getById with id={}",id);

        Optional<Invoice> optionalInvoice = invoiceRepository.findById(id);
        if(!optionalInvoice.isPresent()) {
            log.error("Invoice not found. id={}",id);
            throw new EntityNotFoundException(EntityType.INVOICE, id);
        }
        return optionalInvoice.get();
    }

    /**
     * Method to check if invoice exists or not
     * @param id
     * @return
     */
    public boolean existsById(long id){
        log.debug("existsById with id={}",id);
        return invoiceRepository.existsById(id);
    }

    /**
     * Method to fetch all invoices
     * @return
     */
    @MeasurementPoint
    public List<Invoice> getAll(Long customerId, InvoiceStatus invoiceStatus, Integer offset,Integer limit){
        log.debug("getAll with customerId={},offset={},limit={}",customerId, offset, limit);

        int finalOffset = offset!=null?offset: Pagination.OFFSET;
        int finalLimit = limit!=null?limit:Pagination.LIMIT;

        if(customerId!=null) {

            if(invoiceStatus!=null){
                return getByCustomerIdAndStatus(customerId, invoiceStatus, finalOffset, finalLimit);
            } else {
                return getByCustomerId(customerId, finalOffset, finalLimit);
            }
        }

        if(invoiceStatus!=null){
            return getByStatus(invoiceStatus, finalOffset, finalLimit);
        }

        Page<Invoice> invoicePage = invoiceRepository.findAll(PageRequest.of(finalOffset,finalLimit));
        return invoicePage.getContent();
    }

    /**
     * Method to fetch invoices of a customer
     * @param customerId
     * @return
     */
    public List<Invoice> getByCustomerId(long customerId, int offset,int limit){
        log.debug("getByCustomerId with customerId={},offset={},limit={}",customerId, offset, limit);

        Response response = bizTrackService.isCustomerExists(customerId);
        if(response.getData()!=null){
            if((Boolean)response.getData()){
                return invoiceRepository.findByCustomerId(customerId,PageRequest.of(offset,limit));
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
     * Method to fetch invoices of a customer by status
     * @param customerId
     * @param invoiceStatus
     * @return
     */
    public List<Invoice> getByCustomerIdAndStatus(long customerId, InvoiceStatus invoiceStatus, int offset,int limit){
        log.debug("findByCustomerIdAndStatus with customerId={},invoiceStatus={},offset={},limit={}",customerId,invoiceStatus, offset, limit);

        Response response = bizTrackService.isCustomerExists(customerId);
        if(response.getData()!=null){
            if((Boolean)response.getData()) {
                return invoiceRepository.findByCustomerIdAndStatus(customerId,invoiceStatus,PageRequest.of(offset,limit));
            }
            else{
                log.error("Customer not found. id={}",customerId);
                throw new EntityNotFoundException(EntityType.CUSTOMER,customerId);
            }
        } else{
                throw BiztrackSDKExceptionMapper.mapSDKException(response.getError());
        }
    }

    /**
     * Method to fetch invoices of a invoiceStatus
     * @param invoiceStatus
     * @return
     */
    public List<Invoice> getByStatus(InvoiceStatus invoiceStatus, int offset,int limit){
        log.debug("getByStatus with invoiceStatus={},offset={},limit={}",invoiceStatus, offset, limit);

        return invoiceRepository.findByStatus(invoiceStatus,PageRequest.of(offset,limit));
    }

    /**
     * Method to fetch total expected amount from customer
     * @return
     */
    public BigDecimal getTotalAmountExpectedByCustomerId(long customerId){
        log.debug("getTotalAmountExpectedByCustomerId with id={}",customerId);

        Response response = bizTrackService.isCustomerExists(customerId);
        if(response.getData()!=null){
            if((Boolean)response.getData()) {
                return invoiceRepository.findTotalAmountExpectedByCustomerId(customerId);
            }
            else{
                log.error("Customer not found. id={}",customerId);
                throw new EntityNotFoundException(EntityType.CUSTOMER,customerId);
            }
        } else{
            throw BiztrackSDKExceptionMapper.mapSDKException(response.getError());
        }
    }

    /**
     * Method to fetch total expected amount
     * @return
     */
    public BigDecimal getTotalAmountExpected(){
        return invoiceRepository.findTotalAmountExpected();
    }


    public BigDecimal getStat(String stat, Long customerId){
        log.debug("getStat with stat={}, customerId={}",stat,customerId);

        if(stat.equalsIgnoreCase(Stats.EXPECTED_AMOUNT)){

            if(customerId!=null){
                return getTotalAmountExpectedByCustomerId(customerId);
            } else {
                return getTotalAmountExpected();
            }
        } else {
            throw new InvalidOperationException("Input stat is not supported. Stat:"+stat);
        }
    }

    public Invoice updateInvoiceStatus(long id, BigDecimal paidAmount, InvoiceStatus invoiceStatus){
        log.debug("updateInvoiceStatus with id {}",id);

        Optional<Invoice> optionalInvoice = invoiceRepository.findById(id);
        if(!optionalInvoice.isPresent()) {
            log.error("Customer not found. id={}",id);
            throw new EntityNotFoundException(EntityType.CUSTOMER, id);
        }
        Invoice invoice = optionalInvoice.get();

        if(invoice.getAmount().longValue()==paidAmount.longValue()){
            log.error("Invoice need to be fully paid. id={}",id);
            throw new InvalidOperationException("Invoice need to be fully paid. Payment can not be completed. id="+id);
        }

        invoice.setLastModifydDate(new Date());
        invoice.setStatus(invoiceStatus);
        return invoiceRepository.save(invoice);
    }
}
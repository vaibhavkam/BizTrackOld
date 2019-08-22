package com.intuit.vkamble.biztrack.payment.utils;

import com.intuit.vkamble.biztrack.biztracksdk.core.Error;
import com.intuit.vkamble.biztrack.biztracksdk.enums.ErrorCode;
import exceptions.BusinessRuleViolationException;
import exceptions.EntityNotFoundException;
import exceptions.InternalException;
import exceptions.ServiceUnavailableException;

public class BiztrackSDKExceptionMapper {

    public static RuntimeException mapSDKException(Error error){

        if(error.getCode().equalsIgnoreCase(ErrorCode.BUSINESS_ERROR_ENTITY_NOT_FOUND.toString())){
            return new EntityNotFoundException(error.getCauses().get(0).getMessage());
        } else if(error.getCode().equalsIgnoreCase(ErrorCode.BUSINESS_RULE_VIOLATION.toString())){
            return new BusinessRuleViolationException(error.getCauses().get(0).getMessage());
        } else if(error.getCode().equalsIgnoreCase(ErrorCode.INTERNAL_ERROR.toString())){
            return new InternalException(error.getMessage());
        } else if(error.getCode().equalsIgnoreCase(ErrorCode.SERVICE_UNAVAILABLE.toString())){
            return new ServiceUnavailableException(error.getMessage());
        }
        return new InternalException();
    }
}

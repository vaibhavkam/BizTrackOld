package com.intuit.vkamble.biztrack.biztracksdk.enums;

public enum ErrorCode {

    SERVICE_UNAVAILABLE("SYSTEM-404"),
    INTERNAL_ERROR("SYSTEM-500"),
    BUSINESS_ERROR_ENTITY_NOT_FOUND("BUSINESS-404"),
    BUSINESS_RULE_VIOLATION("BUSINESS-400");

    private String code;

    ErrorCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}

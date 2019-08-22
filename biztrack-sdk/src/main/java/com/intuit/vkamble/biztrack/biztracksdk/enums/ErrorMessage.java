package com.intuit.vkamble.biztrack.biztracksdk.enums;

public enum ErrorMessage {

    SERVICE_UNAVAILABLE("Service is unavailable"),
    INTERNAL_ERROR("Internal error occurred"),
    BUSINESS_ERROR_ENTITY_NOT_FOUND("Entity not found"),
    BUSINESS_RULE_VIOLATION("Invalid business operation");



    private String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

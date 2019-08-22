package com.intuit.vkamble.biztrack.biztracksdk.core;


import java.util.ArrayList;
import java.util.List;

public class Error {

    private String message;
    private String code;
    private List<Cause> causes;

    public Error(String message, String code) {
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<Cause> getCauses() {
        return causes;
    }

    public void addCause(String message) {
        if(causes==null) {
            causes=new ArrayList<>();
        }
        Cause cause = new Cause(message);
        this.causes.add(cause);
    }
}

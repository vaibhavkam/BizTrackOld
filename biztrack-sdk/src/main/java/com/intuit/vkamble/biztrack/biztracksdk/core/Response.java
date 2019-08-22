package com.intuit.vkamble.biztrack.biztracksdk.core;

import java.util.List;

//BIzTrackResponse
public class Response {

    private Object data;
    private boolean hasError;
    private Error error;

    public Response(Object data) {
        this.data = data;
        this.hasError = false;

    }

    public Response(String errorMessage, String errorCode, List<String> causes) {
        this.hasError = true;
        this.error = new Error(errorMessage,errorCode);
        if(causes!=null && !causes.isEmpty()){
            for(String cause:causes){
                this.error.addCause(cause);
            }
        }
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public boolean hasError() {
        return hasError;
    }

    public void setHasError(boolean hasError) {
        this.hasError = hasError;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }
}

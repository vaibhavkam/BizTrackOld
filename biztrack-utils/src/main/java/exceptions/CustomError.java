package exceptions;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CustomError {

    private Date timestamp;
    private String message;
    private String details;
    private String status;
    private List<Cause> causes;

    public CustomError(Date timestamp, String message, String details, String status) {
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
        this.status = status;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Cause> getCauses() {
        return causes;
    }

    public void setCauses(String field, String message) {

        if(this.causes==null){
            this.causes = new ArrayList<Cause>();
        }
        this.causes.add(new Cause(field,message));
    }

    class Cause {

        private String field;
        private String message;

        public Cause(String field, String message) {
            this.field = field;
            this.message = message;
        }

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}

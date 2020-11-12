package ssp.marketplace.app.exceptions.response;

import java.io.Serializable;
import java.util.HashMap;

public class ValidationErrorResponse implements Serializable {
    private final int status;
    private final String message;
    private final HashMap<String,String> errors = new HashMap<>();

    public ValidationErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public void addFieldError(String path, String errorMessage) {
        errors.put(path, errorMessage);
    }

    public HashMap<String,String> getErrors() {
        return errors;
    }
}

package ssp.marketplace.app.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ConfirmationTokenInvalidException extends RuntimeException {

    public ConfirmationTokenInvalidException(String message){
        super(message);
    }

}

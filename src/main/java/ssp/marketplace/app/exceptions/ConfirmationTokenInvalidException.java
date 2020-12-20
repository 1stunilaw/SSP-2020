package ssp.marketplace.app.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ConfirmationTokenInvalidException extends RuntimeException {

    // TODO: 20.12.2020 Удалить, переделать через BadRequest
    public ConfirmationTokenInvalidException(String message){
        super(message);
    }

}

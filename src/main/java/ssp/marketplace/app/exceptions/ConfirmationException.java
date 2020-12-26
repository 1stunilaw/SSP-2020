package ssp.marketplace.app.exceptions;

import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@Setter
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ConfirmationException extends RuntimeException {

    private String field;

    public ConfirmationException(String field, String msg) {
        super(msg);
        this.field = field;
    }
}

package ssp.marketplace.app.exceptions.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;
import ssp.marketplace.app.dto.SimpleResponse;

@ControllerAdvice
public class HttpMessageNotReadableExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public SimpleResponse badCredentialsException(HttpMessageNotReadableException ex) {
        return new SimpleResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage().split(":")[0]);
    }
}

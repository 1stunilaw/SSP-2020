package ssp.marketplace.app.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;
import ssp.marketplace.app.exceptions.response.ErrorResponse;

@ControllerAdvice
public class HttpMessageNotReadableExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ErrorResponse badCredentialsException(HttpMessageNotReadableException ex) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage().split(":")[0]);
    }
}

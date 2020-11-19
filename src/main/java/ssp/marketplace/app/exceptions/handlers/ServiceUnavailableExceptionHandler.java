package ssp.marketplace.app.exceptions.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ssp.marketplace.app.exceptions.*;
import ssp.marketplace.app.exceptions.response.ErrorResponse;

@ControllerAdvice
public class ServiceUnavailableExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(ServiceUnavailableException.class)
    public ErrorResponse ServiceUnavailableException(ServiceUnavailableException ex){
        return new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
    }
}

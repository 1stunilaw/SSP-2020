package ssp.marketplace.app.exceptions.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ssp.marketplace.app.dto.SimpleResponse;
import ssp.marketplace.app.exceptions.ServiceUnavailableException;

@ControllerAdvice
public class ServiceUnavailableExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(ServiceUnavailableException.class)
    public SimpleResponse ServiceUnavailableException(ServiceUnavailableException ex){
        return new SimpleResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
    }
}

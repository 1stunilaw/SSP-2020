package ssp.marketplace.app.exceptions.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ssp.marketplace.app.dto.SimpleResponse;
import ssp.marketplace.app.exceptions.NotFoundException;

@ControllerAdvice
public class NotFoundExceptionHandler {
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    @ExceptionHandler(NotFoundException.class)
    public SimpleResponse NotFoundException(NotFoundException ex){
        return new SimpleResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
    }
}

package ssp.marketplace.app.exceptions.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ssp.marketplace.app.dto.SimpleResponse;
import ssp.marketplace.app.exceptions.AlreadyExistsException;

@ControllerAdvice
public class AlreadyExistsExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(AlreadyExistsException.class)
    public SimpleResponse AlreadyExistException(AlreadyExistsException ex){
        return new SimpleResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
    }
}

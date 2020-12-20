package ssp.marketplace.app.exceptions.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ssp.marketplace.app.dto.SimpleResponse;
import ssp.marketplace.app.exceptions.BadRequestException;

@ControllerAdvice
public class BadRequestHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(BadRequestException.class)
    public SimpleResponse BadRequestHandler(BadRequestException ex){
        return new SimpleResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
    }
}

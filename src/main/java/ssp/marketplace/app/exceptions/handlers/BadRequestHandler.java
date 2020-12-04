package ssp.marketplace.app.exceptions.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ssp.marketplace.app.exceptions.*;
import ssp.marketplace.app.exceptions.response.ErrorResponse;

@ControllerAdvice
public class BadRequestHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(BadRequestException.class)
    public ErrorResponse BadRequestHandler(BadRequestException ex){
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
    }
}

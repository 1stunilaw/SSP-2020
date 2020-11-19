package ssp.marketplace.app.exceptions.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.validation.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ssp.marketplace.app.exceptions.NotFoundException;
import ssp.marketplace.app.exceptions.response.*;
import ssp.marketplace.app.security.jwt.JwtAuthenticationException;

import java.util.List;

@ControllerAdvice
public class NotFoundExceptionHandler {
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    @ExceptionHandler(NotFoundException.class)
    public ErrorResponse NotFoundException(NotFoundException ex){
        return new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
    }
}

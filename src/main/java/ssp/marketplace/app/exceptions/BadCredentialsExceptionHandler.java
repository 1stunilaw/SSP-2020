package ssp.marketplace.app.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;
import ssp.marketplace.app.exceptions.response.ErrorResponse;

@ControllerAdvice
public class BadCredentialsExceptionHandler {

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    @ExceptionHandler(BadCredentialsException.class)
    public ErrorResponse badCredentialsException(BadCredentialsException ex){
        return new ErrorResponse(HttpStatus.FORBIDDEN.value(), "Неправильный логин и/или пароль");
    }

}

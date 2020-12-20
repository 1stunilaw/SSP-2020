package ssp.marketplace.app.exceptions.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;
import ssp.marketplace.app.dto.SimpleResponse;

@ControllerAdvice
public class BadCredentialsExceptionHandler {

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    @ExceptionHandler(BadCredentialsException.class)
    public SimpleResponse badCredentialsException(BadCredentialsException ex){
        return new SimpleResponse(HttpStatus.FORBIDDEN.value(), "Неправильный логин и/или пароль");
    }

}
